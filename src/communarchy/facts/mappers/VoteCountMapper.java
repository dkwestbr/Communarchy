package communarchy.facts.mappers;

import java.util.List;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.counters.VoteCounter;
import communarchy.facts.mappers.interfaces.AbstractMapper;

public class VoteCountMapper extends AbstractMapper<VoteCountMapper> {
	
	private static final Integer SHARD_NUM = 4;
	private static final Random RANDOM = new Random();

	@SuppressWarnings("unchecked")
	public void increment(Key povKey) {
		
	    PersistenceManager pm = pmSession.getPM();
		Query query = pmSession.getPM().newQuery(VoteCounter.class);
		query.setFilter("povKey == povKeyParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s povKeyParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName()));
		
		Integer shardNum = RANDOM.nextInt(SHARD_NUM);
		List<VoteCounter> results = null;
	
	    
	    try {
	    	results = (List<VoteCounter>) query.execute(povKey, shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    VoteCounter shard = null;
	    if(results == null || results.isEmpty()) {
	    	shard = new VoteCounter(povKey, shardNum);
	    } else {
	    	shard = results.get(0);
	    }
	    
	    shard.increment();
	    pm.makePersistent(shard);
	}

	@SuppressWarnings("unchecked")
	public void decrement(Key povKey) {
		
	    PersistenceManager pm = pmSession.getPM();
		Query query = pmSession.getPM().newQuery(VoteCounter.class);
		query.setFilter("povKey == povKeyParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s povKeyParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName()));
		
		Integer shardNum = RANDOM.nextInt(SHARD_NUM);
		List<VoteCounter> results = null;
	
	    
	    try {
	    	results = (List<VoteCounter>) query.execute(povKey, shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    VoteCounter shard = null;
	    if(results == null || results.isEmpty()) {
	    	shard = new VoteCounter(povKey, shardNum);
	    } else {
	    	shard = results.get(0);
	    }
	    
	    shard.decrement();
	    pm.makePersistent(shard);
	}

	@SuppressWarnings("unchecked")
	public int getCount(Key povKey) {

		Query query = pmSession.getPM().newQuery(VoteCounter.class);
		query.setFilter("povKey == povKeyParam");
		query.declareParameters(String.format("%s povKeyParam", Key.class.getName()));
		
		List<VoteCounter> results = null;
	
	    Integer count = 0;
	    try {
	    	results = (List<VoteCounter>) query.execute(povKey);
	    	
	    	if(results != null) {
	    		for(VoteCounter result : results) {
	    			count += result.getCount();
	    		}
	    	}
	    } finally {
	    	query.closeAll();
	    }

	    return count;    
	}
}