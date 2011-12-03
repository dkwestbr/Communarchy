package communarchy.facts.queries.list;

import java.util.Collections;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.controllers.strategies.displayRank.PovRankStrategy;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.PointOfView;

public class GetPovsByStance implements IListQuery<PointOfView> {

	private Key pointId;
	private Integer stance;
	private String memCacheKey;
	
	@SuppressWarnings("unused")
	private GetPovsByStance() {}
	
	public GetPovsByStance(Key pointId, Integer stance) {
		this.pointId = pointId;
		this.stance = stance;
		
		this.memCacheKey = String.format("%s_%s_%d", GetPovsByStance.class.getName(), pointId.toString(), stance);
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
			Collections.sort(povs, new PovRankStrategy(pmSession));
		} finally {
			q.closeAll();
		}
		
		return povs;
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memCacheKey;
	}

	@Override
	public Class<PointOfView> getType() {
		return PointOfView.class;
	}

	@Override
	public String getExpiryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRankChangeKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRanked() {
		return true;
	}
}