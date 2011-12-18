package communarchy.facts.mappers;

import java.util.List;

import communarchy.facts.counters.AbstractCounter;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.entity.IEntityQuery;
import communarchy.facts.queries.list.IListQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class CountMapper extends AbstractMapper<CountMapper> {
	
	public <U extends AbstractCounter<U>, V extends IEntityQuery<U>> void increment(V query) throws CommunarchyPersistenceException {
		if(query == null) {
			throw new CommunarchyPersistenceException("Shard query may not be null");
		}
		
		U shard = pmSession.getMapper(UniqueEntityMapper.class).selectUnique(query);
		if(shard == null) {
			shard = pmSession.getMapper(UniqueEntityMapper.class).insertUnique(query);
		}
		
		shard.increment();
		shard = pmSession.getMapper(UniqueEntityMapper.class).updateUnique(query, shard);
	}
	
	public <U extends AbstractCounter<U>, V extends IEntityQuery<U>> void decrement(V query) throws CommunarchyPersistenceException {
		if(query == null) {
			throw new CommunarchyPersistenceException("Shard query may not be null");
		}
		
		U shard = pmSession.getMapper(UniqueEntityMapper.class).selectUnique(query);
		if(shard == null) {
			shard = pmSession.getMapper(UniqueEntityMapper.class).insertUnique(query);
		}
		
		shard.decrement();
		shard = pmSession.getMapper(UniqueEntityMapper.class).updateUnique(query, shard);
	}
	
	public <U extends AbstractCounter<U>, T extends IListQuery<U>> int getCount(T query) throws CommunarchyPersistenceException {
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