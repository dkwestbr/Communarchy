package communarchy.rankingStrategies;

import java.util.Comparator;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.PointMapper;
import communarchy.facts.mappers.interfaces.IPointMapper;

public class PointRankStrategy implements Comparator<IPoint> {

	private Key viewerId;
	private PMSession pmSession;
	
	@SuppressWarnings("unused")
	private PointRankStrategy() {}
	
	public PointRankStrategy(Key viewerId, PMSession pmSession) {
		this.viewerId = viewerId;
		this.pmSession = pmSession;
	}
	
	@Override
	public int compare(IPoint p1, IPoint p2) {
		IPointMapper pointMapper = pmSession.getMapper(PointMapper.class);
		
		Integer p1Rank = pointMapper.getPointAgreeCount(p1.getPointId());
		Integer p2Rank = pointMapper.getPointAgreeCount(p2.getPointId());
		
		IUserStance stance1 = viewerId == null ? null : pointMapper.selectStance(p1.getPointId(), viewerId);
		IUserStance stance2 = viewerId == null ? null : pointMapper.selectStance(p2.getPointId(), viewerId);
		boolean agreesWithP1 = stance1 == null ? false : stance1.getStance() == IUserStance.STANCE_AGREE;
		boolean agreesWithP2 = stance2 == null ? false : stance2.getStance() == IUserStance.STANCE_AGREE;
		
		p1Rank = p1Rank == null ? 0 : p1Rank;
		p2Rank = p2Rank == null ? 0 : p2Rank;
		
		if(pointMapper.getTopPoint(p1.getParentId()).equals(p1.getPointId())) {
			return -1;
		} else if(pointMapper.getTopPoint(p2.getParentId()).equals(p2.getPointId())) {
			return 1;
		}
		
		if(agreesWithP1 && !agreesWithP2) {
			return -1;
		} else if(agreesWithP2 && !agreesWithP1) {
			return 1;
		}
		
		if(p1Rank > p2Rank) {
			return -1;
		} else if(p2Rank > p1Rank) {
			return 1;
		}
		
		return 0;
	}
}