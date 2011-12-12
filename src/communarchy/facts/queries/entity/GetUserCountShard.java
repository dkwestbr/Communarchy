package communarchy.facts.queries.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jdo.Query;

import communarchy.facts.PMSession;
import communarchy.facts.counters.UserCounter;

public class GetUserCountShard implements IEntityQuery<UserCounter> {

	private static final Random RANDOM = new Random();
	private static final Integer NUM_SHARDS = 100;
	
	private Integer shardNum;
	private String memcacheKey;
	
	public GetUserCountShard() {
		this.shardNum = RANDOM.nextInt(NUM_SHARDS);
		this.memcacheKey = String.format("%s_%d", GetUserCountShard.class.getName(), this.shardNum);
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return this.memcacheKey;
	}

	@Override
	public UserCounter getNewEntity() {
		return new UserCounter(shardNum);
	}

	@Override
	@SuppressWarnings("unchecked")
	public UserCounter runQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(UserCounter.class);
		query.setFilter("shardNum == shardNumParam");
		query.declareParameters(String.format("%s shardNumParam", Integer.class.getName()));
		
		List<UserCounter> results = null;
	    try {
	    	results = (List<UserCounter>) query.execute(shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    return results == null || results.isEmpty() ? null : results.get(0);
	}
	
	private static List<String> EMPTY_LIST = new ArrayList<String>();
	@Override
	public List<String> getCheckInKeys() {
		return EMPTY_LIST;
	}
}