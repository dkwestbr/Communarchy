package communarchy.facts.mappers;

import java.util.Collections;
import java.util.List;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import communarchy.controllers.rankingStrategies.TopPointStrategy;
import communarchy.facts.actions.interfaces.IVote;
import communarchy.facts.implementations.Argument;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.implementations.Stance;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IPointMapper;
import communarchy.facts.results.PageSet;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

@SuppressWarnings("unchecked")
public class PointMapper extends AbstractMapper<PointMapper> implements IPointMapper {
	
	public PointMapper() {}

	@Override
	public void insertNewPost(Point post) {
		pmSession.getPM().makePersistent(post);
	}

	@Override
	public List<IPointOfView> selectChildrenPosts(Key id) {
		List<IPointOfView> povs;
		Query q = pmSession.getPM().newQuery(PointOfView.class);
		try {
			q.setFilter("parentPointId == parentIdParam");
			q.declareParameters(Key.class.getName() + " parentIdParam");
			povs = (List<IPointOfView>) q.execute(id);	
		} finally {
			q.closeAll();
		}
		
		return povs;
	}

	@Override
	public Integer getPointAgreeCount(Key pointId) {
		return getCountByStance(new Stance(pointId, UserStance.STANCE_AGREE));
	}

	@Override
	public Integer getPointNeutralCount(Key pointId) {
		return getCountByStance(new Stance(pointId, UserStance.STANCE_NEUTRAL));
	}

	@Override
	public Integer getPointDisagreeCount(Key pointId) {
		return getCountByStance(new Stance(pointId, UserStance.STANCE_DISAGREE));
	}
	
	private Integer getCountByStance(Stance stance) {
		return pmSession.getMapper(StanceCountMapper.class).getCount(stance);
	}

	@Override
	public Key getTopPoint(Key argId) {
		
		List<IPoint> points = pmSession.getMapper(ArgumentMapper.class).selectChildrenPosts(argId);
		Collections.sort(points, new TopPointStrategy(pmSession));
		
		if(points.get(0) == null) {
			return null;
		} else {
			return points.get(0).getPointId();
		}
	}

	@Override
	public void updateStance(IUserStance stance, Integer oldStance) throws CommunarchyPersistenceException {
		
		Transaction tx = pmSession.getPM().currentTransaction();
		
		try {	
			tx.begin();
			pmSession.getPM().makePersistent(stance);
			pmSession.getMapper(StanceCountMapper.class).decrement(new Stance(stance.getPoint(), oldStance));
			pmSession.getMapper(StanceCountMapper.class).increment(stance);
			tx.commit();
		} finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public void reclaimAllVotes(IUserStance stance) throws CommunarchyPersistenceException {
		List<IVote> votes = pmSession.getMapper(PovMapper.class).selectAllVotes(stance.getPoint(), stance.getUser());
		for(IVote vote : votes) {
			pmSession.getMapper(PovMapper.class).reclaimVote(vote.getPovKey(), vote.getUserKey());
		}
		
		pmSession.getPM().makePersistent(stance);
	}

	@Override
	public void insertNewStance(IUserStance stance) throws CommunarchyPersistenceException {
		Transaction tx = pmSession.getPM().currentTransaction();
		
		try {
			tx.begin();
			pmSession.getPM().makePersistent(stance);
			pmSession.getMapper(StanceCountMapper.class).increment(stance);
			tx.commit();
		} finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public Point selectPostById(Key id) {
		return pmSession.getPM().getObjectById(Point.class, id);
	}

	@Override
	public IArgument selectParentPost(Key id) {
		return pmSession.getMapper(ArgumentMapper.class).selectPostById(id);
	}

	@Override
	public List<IPoint> selectAllChildrenByParent(Key id) {
		return pmSession.getMapper(ArgumentMapper.class).selectChildrenPosts(id);
	}

	@Override
	public UserStance selectStance(Key pointKey, Key userKey) {
		if(pointKey == null || userKey == null) {
			return null;
		}
		
		Query q = pmSession.getPM().newQuery(UserStance.class);
		List<UserStance> results = null;
		try {
			q.setFilter("user == userParam && point == pointParam");
			q.declareParameters(String.format("%s userParam, %s pointParam", Key.class.getName(), Key.class.getName()));
			results = (List<UserStance>) q.execute(userKey, pointKey);	
		} finally {
			q.closeAll();
		}
		
		return results == null ? null : (results.isEmpty() ? null : results.get(0));
	}

	@Override
	public Integer getPointCountByStance(Key pointKey, Integer stance) {
		return getCountByStance(new Stance(pointKey, stance));
	}

	@Override
	public Integer getVoteCount(Key pointKey, Key userKey) {
		return pmSession.getMapper(PovMapper.class).selectAllVotes(pointKey, userKey).size();
	}

	@Override
	public List<IPointOfView> getPovsByStance(Key pointKey, int stance) {
		return pmSession.getMapper(PovMapper.class).selectAllChildrenByStance(pointKey, stance);
	}

	@Override
	public Integer getMaxVoteCount(Key pointKey, int stance) {
		int count = pmSession.getMapper(StanceCountMapper.class).getCount(new Stance(pointKey, stance));
		double firstRoot = Math.sqrt(count);
		
		return (int) Math.floor(firstRoot);
	}

	@Override
	public PageSet<Argument> buildPostFeeed(int numArgs, String startCursor) {
		// TODO Auto-generated method stub
		return null;
	}
}