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
		
		String key = MemcacheWrapper.BuildKey(queryObject.getResourceType(), "getUnique", queryObject.getMemcacheInnerKey());
		U result = MemcacheWrapper.get().get(key);
		if(result == null && !MemcacheWrapper.get().contains(key)) {
			result = queryObject.runQuery(pmSession);
			MemcacheWrapper.get().put(key, result);
			if(result != null) {
				MemcacheWrapper.get().checkIn(result.getMemcacheCheckinKey(), key);
			}
		}
		
		return result;
	}
	
	public <T extends IEntity> T persistUnique(IEntityQuery<T> existsQuery) {
		if(existsQuery == null) {
			throw new NullPointerException("Null entities cannot be persisted");
		}
		
		T existingVal = getUnique(existsQuery);
		
		String key = MemcacheWrapper.BuildKey(existsQuery.getResourceType(), "getUnique", existsQuery.getMemcacheInnerKey());
		MemcacheWrapper.get().remove(key);
		
		if(existingVal != null) {
			return existingVal;
		}
		
		existingVal = existsQuery.getNewEntity();
		/*
		if(existingVal == null && MemcacheWrapper.get().contains(key)) {
			MemcacheWrapper.get().remove(key);
			existingVal = existsQuery.getNewEntity();
		}
		*/

		pmSession.getPM().makePersistent(existingVal);
		MemcacheWrapper.get().checkOut(existingVal.getMemcacheCheckinKey());
		
		return existingVal;
	}
	
	public <T extends IEntity> void deleteUnique(IEntityQuery<T> existsQuery) {
		if(existsQuery == null) {
			throw new NullPointerException("Null entities cannot be persisted");
		}
		
		String key = MemcacheWrapper.BuildKey(existsQuery.getResourceType(), "getUnique", existsQuery.getMemcacheInnerKey());
		MemcacheWrapper.get().remove(key);
		
		T existingVal = getUnique(existsQuery);
		
		if(existingVal == null) {
			return;
		}
		
		MemcacheWrapper.get().checkOut(existingVal.getMemcacheCheckinKey());
		pmSession.getPM().deletePersistent(existingVal);
	}
}