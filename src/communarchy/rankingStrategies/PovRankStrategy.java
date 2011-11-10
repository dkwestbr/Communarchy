package communarchy.rankingStrategies;

import java.util.Comparator;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.mappers.PovMapper;
import communarchy.facts.mappers.interfaces.IPovMapper;

public class PovRankStrategy implements Comparator<IPointOfView> {

	private PMSession pmSession;
	
	@SuppressWarnings("unused")
	private PovRankStrategy() {}
	
	public PovRankStrategy(PMSession pmSession) {
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

		if(pov1Votes > pov2Votes) {
			return -1;
		} else if (pov2Votes > pov1Votes) {
			return 1;
		} else {
			return 0;
		} 
	}

}
