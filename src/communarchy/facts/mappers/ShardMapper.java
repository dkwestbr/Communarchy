package communarchy.facts.mappers;

import java.util.List;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import communarchy.exceptions.CommunarchyPersistenceException;
import communarchy.facts.counters.AbstractCounter;
import communarchy.facts.counters.VoteCounter;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IShardMapper;

public class ShardMapper extends AbstractMapper<ShardMapper> implements IShardMapper {
	
	public ShardMapper() {}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AbstractCounter, U> void increment(U resourceId, Class<T> counter) {
		
	    PersistenceManager pm = pmSession.getPM();

	    Random generator = new Random();
	    int shardNum = generator.nextInt(AbstractCounter.getNumShards(counter));

	    Query shardQuery = pm.newQuery(counter);
	    shardQuery.setFilter("resourceId == keyVal");
	    shardQuery.declareParameters(String.format("%s keyVal", resourceId.getClass().getName()));
	
	    List<T> shards; 
	    
	    try {
	    	shards = (List<T>) shardQuery.execute(shardNum);
	    } finally {
	    	shardQuery.closeAll();
	    }
	    	
	    T shard = shards.get(0);
	    shard.increment();
	    
	    pm.makePersistent(shard);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AbstractCounter, U> void decrement(U resourceId, Class<T> counter) throws CommunarchyPersistenceException {
		
	    PersistenceManager pm = pmSession.getPM();

	    Random generator = new Random();
	    int shardNum = generator.nextInt(AbstractCounter.getNumShards(counter));

	    Query shardQuery = pm.newQuery(counter);
	    shardQuery.setFilter("resourceId == keyVal");
	    shardQuery.declareParameters(String.format("%s keyVal", resourceId.getClass().getName()));
	
	    List<VoteCounter> shards; 
	    
	    try {
	    	shards = (List<VoteCounter>) shardQuery.execute(shardNum);
	    } finally {
	    	shardQuery.closeAll();
	    }
	    	
	    VoteCounter shard = null;
	    shard = shards.get(0);
	    int count = 1;
	    while(shard.getCount() == 0) {
	    	shard = shards.get(count++);
	    }
	
	    if (shards != null && !shards.isEmpty()) {
	    	shard.decrement();
	    } else {
	    	throw new CommunarchyPersistenceException("No shards were found during reclaim vote decrement; this should be impossible to reach");
	    }
	    
	    pm.makePersistent(shard);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AbstractCounter, U> int getCount(U resourceId, Class<T> counter) {
		Query q = pmSession.getPM().newQuery(counter);
		q.setFilter("resourceId == resourceIdParam");
		q.declareParameters(String.format("%s resourceIdParam", resourceId.getClass().getName()));
		
		int sum = 0;

	    try {
	    	List<T> shards =
	          (List<T>) q.execute(resourceId);
	    	if (shards != null && !shards.isEmpty()) {
	    		for (T shard : shards) {
	    			sum += shard.getCount();
	    		}
	        }
	    } finally {
	    	q.closeAll();
	    }

	    return sum;    
	}
}