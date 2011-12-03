package communarchy.facts.mappers;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.entity.IEntityQuery;
import communarchy.utils.caching.MemcacheWrapper;

public class UniqueEntityMapper extends AbstractMapper<UniqueEntityMapper> {
	public <U extends IEntity, T extends IEntityQuery<U>> U getUnique(T queryObject) {
		if(queryObject == null) {
			return null;
		}
		
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "runEntityQuery", queryObject.getMemcacheInnerKey());
		U result = MemcacheWrapper.get().get(key);
		if(result == null && !MemcacheWrapper.get().contains(key)) {
			result = queryObject.runQuery(pmSession);
			//MemcacheWrapper.get().put(key, result);
			//MemcacheWrapper.get().checkIn(result.getKey().toString(), key);
		}
		
		return result;
	}
	
	public <T extends IEntity> T persistUnique(IEntityQuery<T> existsQuery) {
		if(existsQuery == null) {
			throw new NullPointerException("Null entities cannot be persisted");
		}
		
		T existingVal = getUnique(existsQuery);
		
		if(existingVal != null) {
			return existingVal;
		}
		
		existingVal = existsQuery.getNewEntity();

		pmSession.getPM().makePersistent(existingVal);
		String key = MemcacheWrapper.buildKey(BasicMapper.class, "get", existingVal.getKey().toString());
		//MemcacheWrapper.get().put(key, existingVal);
		//MemcacheWrapper.get().checkOut(existingVal.getNewObjectKey());
		//MemcacheWrapper.get().checkIn(existingVal.getKey().toString(), key);
		
		return existingVal;
	}
	
	public <T extends IEntity> void deleteUnique(IEntityQuery<T> existsQuery) {
		if(existsQuery == null) {
			throw new NullPointerException("Null entities cannot be persisted");
		}
		
		T existingVal = getUnique(existsQuery);
		
		if(existingVal == null) {
			return;
		}
		
		String key = MemcacheWrapper.buildKey(BasicMapper.class, "get", existingVal.getKey().toString());
		//MemcacheWrapper.get().remove(key);
		//MemcacheWrapper.get().checkOut(existingVal.getKey().toString());
		//MemcacheWrapper.get().checkOut(existingVal.getNewObjectKey());
		pmSession.getPM().deletePersistent(existingVal);
	}
}
