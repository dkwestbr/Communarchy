package communarchy.controllers.strategies.displayRank;

import java.util.Comparator;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetStanceCount;

public class PointRankStrategy implements Comparator<IPoint> {

	private PMSession pmSession;
	
	@SuppressWarnings("unused")
	private PointRankStrategy() {}
	
	public PointRankStrategy(PMSession pmSession) {
		this.pmSession = pmSession;
	}
	
	@Override
	public int compare(IPoint p1, IPoint p2) {
		
		GetStanceCount query1 = new GetStanceCount(p1.getKey(), UserStance.STANCE_AGREE);
		GetStanceCount query2 = new GetStanceCount(p2.getKey(), UserStance.STANCE_AGREE);
		
		CountMapper mapper = pmSession.getMapper(CountMapper.class);
		
		Integer p1Rank = mapper.getCount(query1);
		Integer p2Rank = mapper.getCount(query2);
		
		if(p1Rank > p2Rank) {
			return -1;
		} else if(p1Rank < p2Rank) {
			return 1;
		}
		
		query1 = new GetStanceCount(p1.getKey(), UserStance.STANCE_NEUTRAL);
		query2 = new GetStanceCount(p2.getKey(), UserStance.STANCE_NEUTRAL);
		
		p1Rank = mapper.getCount(query1);
		p2Rank = mapper.getCount(query2);
		
		if(p1Rank > p2Rank) {
			return -1;
		} else if(p1Rank < p2Rank) {
			return 1;
		}
		
		query1 = new GetStanceCount(p1.getKey(), UserStance.STANCE_DISAGREE);
		query2 = new GetStanceCount(p2.getKey(), UserStance.STANCE_DISAGREE);
		
		p1Rank = mapper.getCount(query1);
		p2Rank = mapper.getCount(query2);
		
		// Notice the direction change of the comparison...
		if(p1Rank < p2Rank) {
			return -1;
		} else if(p1Rank > p2Rank) {
			return 1;
		}
		
		return 0;
	}
}