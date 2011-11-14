package communarchy.controllers.rankingStrategies;

import java.util.Comparator;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.PointMapper;

public class TopPointStrategy implements Comparator<IPoint> {
	
	@SuppressWarnings("unused")
	private TopPointStrategy() {}
	
	private PMSession pmSession;
	
	public TopPointStrategy(PMSession pmSession) {
		this.pmSession = pmSession;
	}
	
	@Override
	public int compare(IPoint p1, IPoint p2) {
		
		if(pmSession.getMapper(PointMapper.class).getPointAgreeCount(p1.getKey()) 
				> pmSession.getMapper(PointMapper.class).getPointAgreeCount(p2.getKey())) {
			return -1;
		}
		
		return 0;
	}
}
