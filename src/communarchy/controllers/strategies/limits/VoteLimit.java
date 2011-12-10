package communarchy.controllers.strategies.limits;

import com.google.appengine.api.datastore.Key;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.mappers.BasicMapper;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.GetStanceCount;
import communarchy.facts.queries.list.VotesCastQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class VoteLimit {
	private VoteLimit() {}
	
	public static int GetVoteThreshold(PMSession pmSession, Key pointKey, int stance) throws CommunarchyPersistenceException {
		Integer stanceCount = pmSession.getMapper(CountMapper.class).getCount(new GetStanceCount(pointKey, stance));
		if(stanceCount > 1) {
			return (int) Math.floor(Math.sqrt(stanceCount));
		} else {
			return 0;
		}
	}
	
	public static int GetVotesRemaining(PMSession pmSession, Key pointKey, int stance, Key user) throws CommunarchyPersistenceException {
		int votesCast = pmSession.getMapper(QueryMapper.class).runListQuery(new VotesCastQuery(pointKey, user)).size();
		return GetVoteThreshold(pmSession, pointKey, stance) - votesCast;
	}
	
	public static boolean AllowVote(PMSession pmSession, Key povKey, Key userKey) throws CommunarchyPersistenceException {
		PointOfView pov = pmSession.getMapper(BasicMapper.class).select(PointOfView.class, povKey);
		return GetVotesRemaining(pmSession, pov.getParentPointId(), pov.getStance(), userKey) <= 0;
	}
}