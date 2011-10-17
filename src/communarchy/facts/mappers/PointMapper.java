package communarchy.facts.mappers;

import java.util.Collections;
import java.util.List;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.exceptions.CommunarchyPersistenceException;
import communarchy.facts.actions.interfaces.IVote;
import communarchy.facts.counters.StanceCounter;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IPointMapper;
import communarchy.rankingStrategies.TopPointStrategy;

@SuppressWarnings("unchecked")
public class PointMapper extends AbstractMapper<PointMapper> implements IPointMapper {
	
	public PointMapper() {}

	@Override
	public void insertNewPost(IPoint post) {
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
		return getCountByStance(StanceCounter.buildKey(pointId, UserStance.STANCE_AGREE));
	}

	@Override
	public Integer getPointNeutralCount(Key pointId) {
		return getCountByStance(StanceCounter.buildKey(pointId, UserStance.STANCE_NEUTRAL));
	}

	@Override
	public Integer getPointDisagreeCount(Key pointId) {
		return getCountByStance(StanceCounter.buildKey(pointId, UserStance.STANCE_DISAGREE));
	}
	
	private Integer getCountByStance(String resource) {
		return pmSession.getMapper(ShardMapper.class).getCount(resource, StanceCounter.class);
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
	public void updateStance(IUserStance stance) throws CommunarchyPersistenceException {
		
		// TODO: Make into transaction
		pmSession.getPM().makePersistent(stance);
		pmSession.getMapper(ShardMapper.class).decrement(StanceCounter.buildKey(stance.getPointId(), stance.getStance()), StanceCounter.class);
	}

	@Override
	public void reclaimAllVotes(IUserStance stance) throws CommunarchyPersistenceException {
		List<IVote> votes = pmSession.getMapper(PovMapper.class).selectAllVotes(stance.getPointId(), stance.getUserId());
		for(IVote vote : votes) {
			pmSession.getMapper(PovMapper.class).reclaimVote(vote.getPovKey(), vote.getUserKey());
		}
		
		pmSession.getPM().makePersistent(stance);
	}

	@Override
	public void insertNewStance(IUserStance stance) throws CommunarchyPersistenceException {
		pmSession.getPM().makePersistent(stance);
		pmSession.getMapper(ShardMapper.class).increment(StanceCounter.buildKey(stance.getPointId(), stance.getStance()), StanceCounter.class);
	}

	@Override
	public IPoint selectPostById(Key id) {
		IPoint point;
		Query q = pmSession.getPM().newQuery(PointOfView.class);
		try {
			q.setFilter("pointId == idParam");
			q.declareParameters(Key.class.getName() + " idParam");
			point = (IPoint) q.execute(id);	
		} finally {
			q.closeAll();
		}
		
		return point;
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
	public IUserStance selectStance(Key pointKey, Key userKey) {
		IUserStance userStance;
		Query q = pmSession.getPM().newQuery(PointOfView.class);
		try {
			q.setFilter("pointId == pointIdParam && posterId == userIdParam");
			q.declareParameters(String.format("%s pointIdParam, %s userIdParam", Key.class.getName(), Key.class.getName()));
			userStance = (IUserStance) q.execute(pointKey, userKey);	
		} finally {
			q.closeAll();
		}
		
		return userStance;
	}

	@Override
	public Integer getPointCountByStance(Key pointKey, Integer stance) {
		return getCountByStance(StanceCounter.buildKey(pointKey, stance));
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
		int count = pmSession.getMapper(ShardMapper.class).getCount(StanceCounter.buildKey(pointKey, stance), StanceCounter.class);
		double firstRoot = Math.sqrt(count);
		
		return (int) Math.floor(firstRoot);
	}
}