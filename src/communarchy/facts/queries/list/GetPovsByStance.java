package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.controllers.strategies.displayRank.PovRankStrategy;
import communarchy.facts.PMSession;
import communarchy.facts.counters.VoteCounter;
import communarchy.facts.implementations.PointOfView;

public class GetPovsByStance implements IListQuery<PointOfView> {

	private Key pointId;
	private Integer stance;
	private String memCacheKey;
	
	@SuppressWarnings("unused")
	private GetPovsByStance() {}
	
	private List<String> checkInKeys;
	
	public GetPovsByStance(Key pointId, Integer stance) {
		this.pointId = pointId;
		this.stance = stance;
		
		this.memCacheKey = String.format("%s_%s_%d", GetPovsByStance.class.getName(), pointId.toString(), stance);
		
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s_%d)", PointOfView.class.getName(), pointId.toString(), stance));
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PointOfView> runListQuery(PMSession pmSession) {
		List<PointOfView> povs = null;
		
		Query q = pmSession.getPM().newQuery(PointOfView.class);
		try {
			q.setFilter("parentPointId == parentIdParam && stance == stanceParam");
			q.declareParameters(String.format("%s parentIdParam, int stanceParam", Key.class.getName()));
			povs = (List<PointOfView>) q.execute(pointId, stance);
		} finally {
			q.closeAll();
		}
		
		if(povs != null && !povs.isEmpty()) {
			Collections.sort(povs, new PovRankStrategy(pmSession));
			for(PointOfView pov : povs) {
				this.checkInKeys.add(String.format("%s(%s)", VoteCounter.class.getName(), pov.getKey().toString()));
			}
		}
		
		return povs;
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memCacheKey;
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}

	@Override
	public Class<PointOfView> getResourceType() {
		return PointOfView.class;
	}
}