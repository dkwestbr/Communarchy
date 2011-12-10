package communarchy.controllers.strategies.displayRank;

import java.util.Comparator;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetVoteCountQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

@SuppressWarnings("rawtypes")
public class PovRankStrategy implements Comparator<IPointOfView> {

	private PMSession pmSession;
	
	@SuppressWarnings("unused")
	private PovRankStrategy() {}
	
	public PovRankStrategy(PMSession pmSession) {
		this.pmSession = pmSession;
	}
	
	@Override
	public int compare(IPointOfView pov1, IPointOfView pov2) {
		CountMapper voteCountMapper = pmSession.getMapper(CountMapper.class);
		
		try {
			GetVoteCountQuery query1 = new GetVoteCountQuery(pov1.getKey());
			Integer pov1Votes = voteCountMapper.getCount(query1);
			
			if(pov1Votes == null) {
				pov1Votes = 0;
			}
			
	
			GetVoteCountQuery query2 = new GetVoteCountQuery(pov2.getKey());
			Integer pov2Votes = voteCountMapper.getCount(query2);
			
			if(pov2Votes == null) {
				pov2Votes = 0;
			}
	
			if(pov1Votes > pov2Votes) {
				return -1;
			} else if (pov2Votes > pov1Votes) {
				return 1;
			} else {
				return 0;
			} 
		} catch(CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}