package communarchy.facts.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

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


@SuppressWarnings("unchecked")
public class PovMapper extends AbstractMapper<PovMapper> implements IPovMapper {
	
	public PovMapper() {}

	@Override
	public void insertNewPost(IPointOfView post) {
		pmSession.getPM().makePersistent(post);
	}

	@Override
	public IFlag selectFlag(IFlag flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPointOfView selectPostById(Key id) {
		return pmSession.getPM().getObjectById(PointOfView.class, id);
	}

	@Override
	public IPoint selectParentPost(Key id) {
		return pmSession.getMapper(PointMapper.class).selectPostById(id);
	}

	@Override
	public List<IPointOfView> selectAllChildrenByParent(Key id) {
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
	public List<IPointOfView> selectAllChildrenByStance(Key pointId, Integer stance) {
		List<IPointOfView> povs;
		
		Query q = pmSession.getPM().newQuery(PointOfView.class);
		
		try {
			q.setFilter("parentPointId == parentIdParam && stance == stanceParam");
			q.declareParameters(String.format("%s parentIdParam, int stanceParam", Key.class.getName()));
			povs = (List<IPointOfView>) q.execute(pointId, stance);
		} finally {
			q.closeAll();
		}
		
		return povs;
	}

	@Override
	public Integer getPovVoteCount(Key povId) {
		return pmSession.getMapper(VoteCountMapper.class).getCount(povId);
	}

	@Override
	public void insertVote(Key povId, Key userId) {
		
		IVote vote = selectVote(povId, userId);
		if(vote != null) {
			return;
		}
		
		vote = new Vote(povId, userId);
		
		Transaction txn = pmSession.getPM().currentTransaction();
		
		try {
			txn.begin();
			pmSession.getPM().makePersistent(vote);
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
			pmSession.getPM().deletePersistent(vote);
			pmSession.getMapper(VoteCountMapper.class).decrement(povId);
			txn.commit();
		} finally {
			if(txn.isActive()) {
				txn.rollback();
			}
		}
	}

	@Override
	public Vote selectVote(Key povId, Key userId) {
		
		Query q = pmSession.getPM().newQuery(Vote.class);
		Vote vote = null;
		try {
			q.setFilter("povKey == povKeyParam && userKey == userKeyParam");
			q.declareParameters(String.format("%s povKeyParam, %s userKeyParam", Key.class.getName(), Key.class.getName()));
			List<Vote> results = (List<Vote>) q.execute(povId, userId);
			vote = results == null || results.isEmpty() ? null : results.get(0);
		} finally {
			q.closeAll();
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
			votes.add(selectVote(pov.getPovId(), userId));
		}
		
		return votes;
	}

	@Override
	public PageSet<Argument> buildPostFeeed(int numArgs, String startCursor) {
		// TODO Auto-generated method stub
		return null;
	}
}