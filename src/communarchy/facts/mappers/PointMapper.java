package communarchy.facts.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

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
import communarchy.utils.caching.CommunarchyCache;
import communarchy.utils.caching.ListUtils;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

@SuppressWarnings("unchecked")
public class PointMapper extends AbstractMapper<PointMapper> implements IPointMapper {

	public PointMapper() {}

	@Override
	public void insertNewPost(Point post) {
		if(post == null) {
			return;
		}
		
		pmSession.getPM().makePersistent(post);
		CommunarchyCache.getInstance().clearList(post.getParentId().toString());
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<IPointOfView> selectChildrenPosts(Key id) {
		
		CommunarchyCache.getInstance();
		String key = CommunarchyCache.buildKey(PointMapper.class, 
				"selectChildrenPosts", id.toString());
		
		List<IPointOfView> povs = null;
		if(CommunarchyCache.getInstance().contains(key)) {
			String povIds = CommunarchyCache.getInstance().get(key);
			if(povIds == null) {
				return null;
			}
			
			List<Key> keys = ListUtils.keyStringToKeyList(povIds, PointOfView.class.getSimpleName());
			povs = new ArrayList<IPointOfView>();
			for(Key povKey : keys) {
				povs.add(pmSession.getMapper(PovMapper.class).selectPostById(povKey));
			}
		} else {
			if(povs == null) {
				Query q = pmSession.getPM().newQuery(PointOfView.class);
				try {
					q.setFilter("parentPointId == parentIdParam");
					q.declareParameters(Key.class.getName() + " parentIdParam");
					povs = (List<IPointOfView>) q.execute(id);	
				} finally {
					q.closeAll();
				}
				
				CommunarchyCache.getInstance().putList(id.toString(), key, ((List)povs));
				if(povs == null || povs.isEmpty()) {
					return new ArrayList<IPointOfView>();
				}
			}
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
	public void updateStance(IUserStance stance, Integer oldStance) throws CommunarchyPersistenceException {
		
		Transaction tx = pmSession.getPM().currentTransaction();
		
		try {	
			tx.begin();
			CommunarchyCache.getInstance().clearList(stance.getPoint().toString());
			CommunarchyCache.getInstance().clearEntity(stance.getKey().toString());
			CommunarchyCache.getInstance().clearEntity(UserStance.BuildVoteQueryKey(stance.getPoint(), stance.getUser()));
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
	}

	@Override
	public void insertNewStance(IUserStance stance) throws CommunarchyPersistenceException {
		Transaction tx = pmSession.getPM().currentTransaction();
		
		try {
			tx.begin();
			CommunarchyCache.getInstance().clearList(selectPostById(stance.getPoint()).getParentId().toString());
			CommunarchyCache.getInstance().clearEntity(UserStance.BuildVoteQueryKey(stance.getPoint(), stance.getUser()));
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
		String key = CommunarchyCache.buildKey(PointMapper.class, "selectPostById", id.toString());
		Point point = null;
		if(CommunarchyCache.getInstance().contains(key)) {
			point = CommunarchyCache.getInstance().get(key);
		} else {
			point = pmSession.getPM().getObjectById(Point.class, id);
			CommunarchyCache.getInstance().putEntity(point.getKey().toString(), key, point);
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
	public UserStance selectStance(Key pointKey, Key userKey) {
		if(pointKey == null || userKey == null) {
			return null;
		}
		
		String key = CommunarchyCache.buildKey(PointMapper.class, "selectStance", 
				String.format("%s_%s", pointKey.toString(), userKey.toString()));
		
		UserStance result = null;
		if(CommunarchyCache.getInstance().contains(key)) {
			result = CommunarchyCache.getInstance().get(key);
		} else {
			List<UserStance> results;
			Query q = pmSession.getPM().newQuery(UserStance.class);
			try {
				q.setFilter("user == userParam && point == pointParam");
				q.declareParameters(String.format("%s userParam, %s pointParam", Key.class.getName(), Key.class.getName()));
				results = (List<UserStance>) q.execute(userKey, pointKey);	
			} finally {
				q.closeAll();
			}
			
			if(results == null || results.isEmpty()) {
				CommunarchyCache.getInstance().putEntity(UserStance.BuildVoteQueryKey(pointKey, userKey), key, null);
				return null;
			}
			result = results.get(0);

			CommunarchyCache.getInstance().putEntity(result.getKey().toString(), key, result);
		}
		
		return result;
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
	public PageSet<Argument> buildPostFeed(int numArgs, String startCursor) {
		// TODO Auto-generated method stub
		return null;
	}
}