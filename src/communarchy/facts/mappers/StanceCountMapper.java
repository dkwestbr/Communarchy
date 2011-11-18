package communarchy.facts.mappers;

import java.util.List;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.counters.StanceCounter;
import communarchy.facts.interfaces.IStance;
import communarchy.facts.mappers.interfaces.AbstractMapper;

public class StanceCountMapper extends AbstractMapper<StanceCountMapper> {
	
	private static final Integer SHARD_NUM = 4;
	private static final Random RANDOM = new Random();

	@SuppressWarnings("unchecked")
	public void increment(IStance resource) {
		
	    PersistenceManager pm = pmSession.getPM();
		Query query = pmSession.getPM().newQuery(StanceCounter.class);
		query.setFilter("point == pointParam && stance == stanceParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s pointParam, %s stanceParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName(), 
												Integer.class.getName()));
		
		Integer shardNum = RANDOM.nextInt(SHARD_NUM);
		List<StanceCounter> results = null;
	
	    
	    try {
	    	results = (List<StanceCounter>) query.execute(resource.getPoint(),
					resource.getStance(), shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    StanceCounter shard = null;
	    if(results == null || results.isEmpty()) {
	    	shard = new StanceCounter(resource, shardNum);
	    } else {
	    	shard = results.get(0);
	    }
	    
	    shard.increment();
	    pm.makePersistent(shard);
	}

	@SuppressWarnings("unchecked")
	public void decrement(IStance resource) {
		
	    PersistenceManager pm = pmSession.getPM();
		Query query = pmSession.getPM().newQuery(StanceCounter.class);
		query.setFilter("point == pointParam && stance == stanceParam && shardNum == shardNumParam");
		query.declareParameters(String.format("%s pointParam, %s stanceParam, %s shardNumParam",
												Key.class.getName(), Integer.class.getName(), 
												Integer.class.getName()));
		
		Integer shardNum = RANDOM.nextInt(SHARD_NUM);
		List<StanceCounter> results = null;
	
	    
	    try {
	    	results = (List<StanceCounter>) query.execute(resource.getPoint(),
					resource.getStance(), shardNum);
	    } finally {
	    	query.closeAll();
	    }
	    
	    StanceCounter shard = null;
	    if(results == null || results.isEmpty()) {
	    	shard = new StanceCounter(resource, shardNum);
	    } else {
	    	shard = results.get(0);
	    }
	    
	    shard.decrement();
	    pm.makePersistent(shard);
	}

	@SuppressWarnings("unchecked")
	public int getCount(IStance resource) {

		Query query = pmSession.getPM().newQuery(StanceCounter.class);
		query.setFilter("point == pointParam && stance == stanceParam");
		query.declareParameters(String.format("%s pointParam, %s stanceParam",
												Key.class.getName(), Integer.class.getName()));
		
		List<StanceCounter> results = null;
	
	    Integer count = 0;
	    try {
	    	results = (List<StanceCounter>) query.execute(resource.getPoint(),
					resource.getStance());
	    	
	    	if(results != null) {
	    		for(StanceCounter result : results) {
	    			count += result.getCount();
	    		}
	    	}
	    } finally {
	    	query.closeAll();
	    }

	    return count;    
	}
}