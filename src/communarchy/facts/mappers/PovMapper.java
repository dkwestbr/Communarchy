package communarchy.facts.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import communarchy.controllers.rankingStrategies.PovRankStrategy;
import communarchy.facts.actions.Vote;
import communarchy.facts.actions.interfaces.IFlag;
import communarchy.facts.actions.interfaces.IVote;
import communarchy.facts.implementations.Argument;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IPovMapper;
import communarchy.facts.results.PageSet;
import communarchy.utils.caching.CommunarchyCache;
import communarchy.utils.caching.ListUtils;


@SuppressWarnings("unchecked")
public class PovMapper extends AbstractMapper<PovMapper> implements IPovMapper {
	
	public PovMapper() {}

	@Override
	public void insertNewPost(IPointOfView post) {
		pmSession.getPM().makePersistent(post);
		CommunarchyCache.getInstance().clearList(post.getParentPointId().toString());
	}

	@Override
	public IFlag selectFlag(IFlag flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPointOfView selectPostById(Key id) {
		String key = CommunarchyCache.buildKey(PovMapper.class, "selectPostById", id.toString());
		IPointOfView pov = CommunarchyCache.getInstance().get(key);
		if(pov == null && !CommunarchyCache.getInstance().contains(key)) {
			pov = pmSession.getPM().getObjectById(PointOfView.class, id);
			CommunarchyCache.getInstance().putEntity(id.toString(), key, pov);
		}
		
		return pov;
	}

	@Override
	public IPoint selectParentPost(Key id) {
		return pmSession.getMapper(PointMapper.class).selectPostById(id);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<IPointOfView> selectAllChildrenByParent(Key id) {
		String key = CommunarchyCache.buildKey(PovMapper.class, "selectAllChildrenByParent", id.toString());
		List<IPointOfView> povs = null;
		String ids = CommunarchyCache.getInstance().get(key);
		if(ids != null) {
			List<Key> keys = ListUtils.keyStringToKeyList(ids, PointOfView.class.getSimpleName());
			povs = new ArrayList<IPointOfView>();
			for(Key povKey : keys) {
				povs.add(pmSession.getMapper(PovMapper.class).selectPostById(povKey));
			}
		} else if(!CommunarchyCache.getInstance().contains(key)) {
			Query q = pmSession.getPM().newQuery(PointOfView.class);
			try {
				q.setFilter("parentPointId == parentIdParam");
				q.declareParameters(Key.class.getName() + " parentIdParam");
				povs = (List<IPointOfView>) q.execute(id);	
			} finally {
				q.closeAll();
			}
			
			CommunarchyCache.getInstance().putList(id.toString(), key, ((List) povs));
		}
		
		return povs == null ? EMPTY_RESULT : povs;
	}

	private static final List<IPointOfView> EMPTY_RESULT = new ArrayList<IPointOfView>();
	@Override
	@SuppressWarnings("rawtypes")
	public List<IPointOfView> selectAllChildrenByStance(Key pointId, Integer stance) {
		String key = CommunarchyCache.buildKey(PovMapper.class, "selectAllChildrenByStance",
				String.format("%s_%d", pointId.toString(), stance));
		
		List<IPointOfView> povs = null;
		String ids = CommunarchyCache.getInstance().get(key);
		if(ids != null) {
			List<Key> keys = ListUtils.keyStringToKeyList(ids, PointOfView.class.getSimpleName());
			povs = new ArrayList<IPointOfView>();
			for(Key povKey : keys) {
				povs.add(pmSession.getMapper(PovMapper.class).selectPostById(povKey));
			}
		} else if(!CommunarchyCache.getInstance().contains(key)) {
			Query q = pmSession.getPM().newQuery(PointOfView.class);
			try {
				q.setFilter("parentPointId == parentIdParam && stance == stanceParam");
				q.declareParameters(String.format("%s parentIdParam, int stanceParam", Key.class.getName()));
				povs = (List<IPointOfView>) q.execute(pointId, stance);
			} finally {
				q.closeAll();
			}
			Collections.sort(povs, new PovRankStrategy(pmSession));
			CommunarchyCache.getInstance().putList(pointId.toString(), key, ((List) povs));
		}
		
		return povs == null ? EMPTY_RESULT : povs;
	}

	@Override
	public Integer getPovVoteCount(Key povId) {
		return pmSession.getMapper(VoteCountMapper.class).getCount(povId);
	}

	@Override
	public void insertVote(Key pointId, Key povId, Key userId) {
		
		IVote vote = selectVote(povId, userId);
		if(vote != null) {
			return;
		}
		
		vote = new Vote(pointId, povId, userId);
		
		Transaction txn = pmSession.getPM().currentTransaction();
		
		try {
			txn.begin();
			pmSession.getPM().makePersistent(vote);
			CommunarchyCache.getInstance().clearList(selectPostById(povId).getParentPointId().toString());
			CommunarchyCache.getInstance().clearEntity(Vote.BuildVoteQueryKey(povId, userId));
			pmSession.getMapper(VoteCountMapper.class).increment(povId);
			txn.commit();
		} finally {
			if(txn.isActive()) {
				txn.rollback();
			}
		}
	}

	@Override
	public void reclaimVote(Key povId, Key userId) {
		
		// TODO: Make this a transaction
		Vote vote = selectVote(povId, userId);
		if(vote == null) {
			return;
		}
		
		Transaction txn = pmSession.getPM().currentTransaction();
		
		try {
			txn.begin();
			CommunarchyCache.getInstance().clearList(povId.toString());
			CommunarchyCache.getInstance().clearEntity(vote.getKey().toString());
			CommunarchyCache.getInstance().clearEntity(Vote.BuildVoteQueryKey(povId, userId));
			pmSession.getPM().deletePersistent(pmSession.getPM().getObjectById(Vote.class, vote.getKey()));
			pmSession.getMapper(VoteCountMapper.class).decrement(povId);
			txn.commit();
		} finally {
			if(txn.isActive()) {
				txn.rollback();
			}
		}
	}
	
	public Integer getVotesCast(Key pointId, Key userId) {
		Integer votesCast = 0;
		String key = CommunarchyCache.buildKey(PovMapper.class, "getVotesCast", 
				String.format("%s_%s", pointId.toString(), userId.toString()));
		
		// STOPPED HERE AND FIX THIS SHIT...................
		Query q = pmSession.getPM().newQuery(Vote.class);
		
		try {
			q.setFilter("pointKey == pointKeyParam && userKey == userKeyParam");
			q.declareParameters(String.format("%s pointKeyParam, %s userKeyParam", Key.class.getName(), Key.class.getName()));
			List<Vote> results = (List<Vote>) q.execute(pointId, userId);
			votesCast = results == null ? 0 : results.size();
		} finally {
			q.closeAll();
		}
		
		if(votesCast.equals(0)) {
			CommunarchyCache.getInstance().putEntity(pointId.toString(), key, votesCast);
		}
		
		return votesCast;
	}

	@Override
	public Vote selectVote(Key povId, Key userId) {
		
		String key = CommunarchyCache.buildKey(PovMapper.class, "selectVote", 
				String.format("%s_%s", povId.toString(), userId.toString()));
		
		Vote vote = CommunarchyCache.getInstance().get(key);
		if(vote == null && !CommunarchyCache.getInstance().contains(key)) {
			Query q = pmSession.getPM().newQuery(Vote.class);
			
			try {
				q.setFilter("povKey == povKeyParam && userKey == userKeyParam");
				q.declareParameters(String.format("%s povKeyParam, %s userKeyParam", Key.class.getName(), Key.class.getName()));
				List<Vote> results = (List<Vote>) q.execute(povId, userId);
				vote = results == null || results.isEmpty() ? null : results.get(0);
			} finally {
				q.closeAll();
			}
			
			if(vote == null) {
				CommunarchyCache.getInstance().putEntity(Vote.BuildVoteQueryKey(povId, userId), key, vote);
			} else {
				CommunarchyCache.getInstance().putEntity(vote.getKey().toString(), key, vote);
			}
		}
		
		return vote;
	}

	@Override
	public void insertFlagIgnored(Key povId, Key userId, int flagType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IVote> selectAllVotes(Key pointId, Key userId) {
		
		List<IPointOfView> povs = pmSession.getMapper(PointMapper.class).selectChildrenPosts(pointId);
		List<IVote> votes = new ArrayList<IVote>();
		for(IPointOfView pov : povs) {
			votes.add(selectVote(pov.getKey(), userId));
		}
		
		return votes;
	}

	@Override
	public PageSet<Argument> buildPostFeed(int numArgs, String startCursor) {
		// TODO Auto-generated method stub
		return null;
	}
}