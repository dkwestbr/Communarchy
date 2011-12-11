package communarchy.controllers.strategies.limits;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.mappers.BasicMapper;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.GetPovsByStance;
import communarchy.facts.queries.list.GetStanceCount;
import communarchy.facts.queries.list.VotesCastQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class VoteLimit {
	private VoteLimit() {}
	
	private static int GetVoteThreshold(PMSession pmSession, Key pointKey, int stance) throws CommunarchyPersistenceException {
		Integer stanceCount = pmSession.getMapper(CountMapper.class).getCount(new GetStanceCount(pointKey, stance));
		if(stanceCount > 1) {
			return (int) Math.floor(Math.sqrt(stanceCount));
		} else {
			return 0;
		}
	}
	
	/**
	 * 
	 * Returns -1 if nothing should be shown, 0 or greater otherwise
	 * 
	 * @param pmSession
	 * @param pointKey
	 * @param stance
	 * @param user
	 * @return
	 * @throws CommunarchyPersistenceException
	 */
	public static Integer GetVotesRemaining(PMSession pmSession, Key pointKey, Integer stance, Key user) throws CommunarchyPersistenceException {
		
		int votesCast = pmSession.getMapper(QueryMapper.class).runListQuery(new VotesCastQuery(pointKey, user)).size();
	
		List<PointOfView> povs = pmSession.getMapper(QueryMapper.class).runListQuery(new GetPovsByStance(pointKey, stance));
		int authorCount = 0;
		for(PointOfView pov : povs) {
			if(pov.getPosterId().toString().equals(user.toString())) {
				++authorCount;
			}
		}
		
		int threshold = GetVoteThreshold(pmSession, pointKey, stance);
		int remaining = povs.size() - authorCount;
		
		int returnVal = -1;
		if(povs.size() > 0 && remaining > 0) {
			if(threshold > remaining) {
				returnVal = remaining;
			} else {
				returnVal = threshold;
			}
			
			returnVal -= votesCast;
		}
		
		return returnVal;
	}
	
	public static boolean AllowVote(PMSession pmSession, Key povKey, Key userKey) throws CommunarchyPersistenceException {
		PointOfView pov = pmSession.getMapper(BasicMapper.class).select(PointOfView.class, povKey);
		return GetVotesRemaining(pmSession, pov.getParentPointId(), pov.getStance(), userKey) <= 0;
	}
}