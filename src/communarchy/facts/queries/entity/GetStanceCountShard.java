package communarchy.facts.queries.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.counters.StanceCounter;
import communarchy.facts.implementations.Stance;

public class GetStanceCountShard implements IEntityQuery<StanceCounter> {

	private static final Random RANDOM = new Random();
	private static final Integer NUM_RANDOMS = 4;
	
	private Key pointId;
	private Integer stance;
	private Integer shardNum;
	private String memcacheKey;
	
	private List<String> checkInKeys;
	
	@SuppressWarnings("unused")
	private GetStanceCountShard() {}
	
	public GetStanceCountShard(Stance stance) {
		init(stance.getPoint(), stance.getStance());
	}
	
	public GetStanceCountShard(Key pointId, Integer stance) {
		init(pointId, stance);
	}
	
	private void init(Key pointId, Integer stance) {
		this.pointId = pointId;
		this.stance = stance;
		
		this.shardNum = RANDOM.nextInt(NUM_RANDOMS);
		this.memcacheKey = String.format("%s_%s_%d_%d", 
				GetStanceCountShard.class.getName(), pointId, stance, shardNum);
		
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s_%d_%d)", StanceCounter.class.getName(), pointId.toString(), stance, shardNum));
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	public StanceCounter getNewEntity() {
		return new StanceCounter(pointId, stance, shardNum);
	}

	@Override
	@SuppressWarnings("unchecked")
	public StanceCounter runQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(StanceCounter.class);
		query.setFilter("point == pointParam && stance == stanceParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s pointParam, %s stanceParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName(), 
												Integer.class.getName()));
		
		List<StanceCounter> results = null;
	    try {
	    	results = (List<StanceCounter>) query.execute(pointId,
					stance, shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    return results == null || results.isEmpty() ? null : results.get(0);
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}
}