package communarchy.facts.mappers;

import java.util.List;

import communarchy.facts.counters.AbstractCounter;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.entity.IEntityQuery;
import communarchy.facts.queries.list.IListQuery;

public class CountMapper extends AbstractMapper<CountMapper> {
	
	public <U extends AbstractCounter<?>, T extends IEntityQuery<U>> void increment(T query) {
		U shard = query.runQuery(pmSession);
		if(shard == null) {
			shard = query.getNewEntity();
		}
		
	    shard.increment();
	    pmSession.getMapper(BasicMapper.class).persist(shard);
	}
	
	public <U extends AbstractCounter<?>, T extends IEntityQuery<U>> void decrement(T query) {
		U shard = query.runQuery(pmSession);
		if(shard == null) {
			shard = query.getNewEntity();
		}
		
	    shard.decrement();
	    pmSession.getMapper(BasicMapper.class).persist(shard);
	}
	
	public <U extends AbstractCounter<?>, T extends IListQuery<U>> int getCount(T query) {
		List<U> shards = pmSession.getMapper(QueryMapper.class).runListQuery(query);
		Integer count = 0;
		if(shards != null && !shards.isEmpty()) {
			for(U shard : shards) {
				count += shard.getCount();
			}
		}
		
		return count;
	}
}