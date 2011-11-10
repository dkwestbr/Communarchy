package communarchy.controllers.rankingStrategies;

import java.util.Comparator;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.PointMapper;
import communarchy.facts.mappers.interfaces.IPointMapper;

public class PointRankStrategy implements Comparator<IPoint> {

	private PMSession pmSession;
	
	@SuppressWarnings("unused")
	private PointRankStrategy() {}
	
	public PointRankStrategy(PMSession pmSession) {
		this.pmSession = pmSession;
	}
	
	@Override
	public int compare(IPoint p1, IPoint p2) {
		
		IPointMapper pointMapper = pmSession.getMapper(PointMapper.class);
		
		Integer p1Rank = pointMapper.getPointAgreeCount(p1.getPointId());
		Integer p2Rank = pointMapper.getPointAgreeCount(p2.getPointId());
		
		if(p1Rank > p2Rank) {
			return -1;
		} else if(p1Rank < p2Rank) {
			return 1;
		}
		
		p1Rank = pointMapper.getPointNeutralCount(p1.getPointId());
		p2Rank = pointMapper.getPointNeutralCount(p2.getPointId());
		
		if(p1Rank > p2Rank) {
			return -1;
		} else if(p1Rank < p2Rank) {
			return 1;
		}
		
		p1Rank = pointMapper.getPointDisagreeCount(p1.getPointId());
		p2Rank = pointMapper.getPointDisagreeCount(p2.getPointId());
		
		// Notice the direction change of the comparison...
		if(p1Rank < p2Rank) {
			return -1;
		} else if(p1Rank > p2Rank) {
			return 1;
		}
		
		return 0;
	}
}