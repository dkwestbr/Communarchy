package communarchy.facts.queries.entity;

import java.util.List;
import java.util.Random;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.counters.VoteCounter;

public class GetVoteCountShard implements IEntityQuery<VoteCounter> {

	private static final Random RANDOM = new Random();
	private static final Integer NUM_SHARDS = 4;
	
	private Key povId;
	private Integer shardNum;
	private String memcacheKey;
	
	@SuppressWarnings("unused")
	private GetVoteCountShard() {}
	
	public GetVoteCountShard(Key povId) {
		this.povId = povId;	
		this.shardNum = RANDOM.nextInt(NUM_SHARDS);
		this.memcacheKey = String.format("%s_%s_%d", 
				GetVoteCountShard.class.getName(), povId, shardNum);
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	public VoteCounter getNewEntity() {
		return new VoteCounter(povId, shardNum);
	}

	@Override
	@SuppressWarnings("unchecked")
	public VoteCounter runQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(VoteCounter.class);
		query.setFilter("povKey == povKeyParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s povKeyParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName()));
		
		List<VoteCounter> results = null;
	
	    
	    try {
	    	results = (List<VoteCounter>) query.execute(povId, shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    return results == null || results.isEmpty() ? null : results.get(0);
	}
}