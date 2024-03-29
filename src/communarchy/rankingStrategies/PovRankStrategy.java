package communarchy.rankingStrategies;

import java.util.Comparator;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.mappers.PovMapper;
import communarchy.facts.mappers.interfaces.IPovMapper;

public class PovRankStrategy implements Comparator<IPointOfView> {

	private Key viewerId;
	private PMSession pmSession;
	
	@SuppressWarnings("unused")
	private PovRankStrategy() {}
	
	public PovRankStrategy(Key viewerId, PMSession pmSession) {
		this.viewerId = viewerId;
		this.pmSession = pmSession;
	}
	
	@Override
	public int compare(IPointOfView pov1, IPointOfView pov2) {
		IPovMapper povMapper = pmSession.getMapper(PovMapper.class);
		Integer pov1Votes = povMapper.getPovVoteCount(pov1.getPovId());
		if(pov1Votes == null)
			pov1Votes = 0;
		
		Integer pov2Votes = povMapper.getPovVoteCount(pov2.getPovId());
		if(pov2Votes == null)
			pov2Votes = 0;
		
		boolean votedFor1 = povMapper.selectVote(pov1.getPovId(), viewerId) != null;
		boolean votedFor2 = povMapper.selectVote(pov2.getPovId(), viewerId) != null;
		if(pov1Votes > pov2Votes || 
				(pov1Votes.equals(pov2Votes) && votedFor1)) {
			return -1;
		} else if (pov2Votes > pov1Votes || 
				(pov1Votes.equals(pov2Votes) && votedFor2)) {
			return 1;
		} else {
			return 0;
		} 
	}

}
