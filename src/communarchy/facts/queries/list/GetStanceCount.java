package communarchy.facts.queries.list;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.counters.StanceCounter;
import communarchy.facts.implementations.Stance;

public class GetStanceCount implements IListQuery<StanceCounter> {
	
	private Key pointId;
	private Integer stance;
	private String memcacheKey;
	
	@SuppressWarnings("unused")
	private GetStanceCount() {}
	
	public GetStanceCount(Stance stance) {
		init(stance.getPoint(), stance.getStance());
	}
	
	public GetStanceCount(Key pointId, Integer stance) {
		init(pointId, stance);
	}
	
	private void init(Key pointId, Integer stance) {
		this.pointId = pointId;
		this.stance = stance;
		
		this.memcacheKey = String.format("%s_%s_%d", 
				GetStanceCount.class.getName(), pointId, stance);
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StanceCounter> runListQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(StanceCounter.class);
		query.setFilter("point == pointParam && stance == stanceParam");
		query.declareParameters(String.format("%s pointParam, %s stanceParam",
												Key.class.getName(), Integer.class.getName()));
		
		List<StanceCounter> results = null;
	
	    
	    try {
	    	results = (List<StanceCounter>) query.execute(pointId,
					stance);
	    } finally {
	    	query.closeAll();
	    }
	    
	    return results;
	}

	@Override
	public Class<StanceCounter> getType() {
		return StanceCounter.class;
	}

	@Override
	public String getExpiryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRankChangeKey() {
		return null;
	}

	@Override
	public boolean isRanked() {
		return false;
	}
}