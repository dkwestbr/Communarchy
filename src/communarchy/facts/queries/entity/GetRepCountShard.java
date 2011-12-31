package communarchy.facts.queries.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.counters.RepCounter;
import communarchy.facts.counters.StanceCounter;

public class GetRepCountShard implements IEntityQuery<RepCounter> {

	private static final Random RANDOM = new Random();
	private static final Integer NUM_RANDOMS = 4;
	
	private Key userId;
	private Integer shardNum;
	private String memcacheKey;
	
	private List<String> checkInKeys;
	
	@SuppressWarnings("unused")
	private GetRepCountShard() {}
	
	public GetRepCountShard(Key userId) {
		this.userId = userId;
		
		this.shardNum = RANDOM.nextInt(NUM_RANDOMS);
		this.memcacheKey = String.format("%s_%s_%d", 
				GetRepCountShard.class.getName(), userId, shardNum);
		
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s)", StanceCounter.class.getName(), userId.toString()));
		this.checkInKeys.add(String.format("%s(%s_%d)", StanceCounter.class.getName(), userId.toString(), shardNum));
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	public RepCounter getNewEntity() {
		return new RepCounter(userId, shardNum);
	}

	@Override
	@SuppressWarnings("unchecked")
	public RepCounter runQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(RepCounter.class);
		query.setFilter("userKey == userKeyParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s userKeyParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName()));
		
		List<RepCounter> results = null;
	    try {
	    	results = (List<RepCounter>) query.execute(userId, shardNum);
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