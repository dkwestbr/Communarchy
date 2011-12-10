package communarchy.facts.mappers;

import javax.jdo.JDOHelper;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.entity.IEntityQuery;
import communarchy.utils.caching.MemcacheWrapper;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class UniqueEntityMapper extends AbstractMapper<UniqueEntityMapper> {

	public <U extends IEntity<U>, T extends IEntityQuery<U>> U selectUnique(T query) throws CommunarchyPersistenceException {
		if(query == null) {
			throw new CommunarchyPersistenceException("Query must not be null");
		}
		
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "getUnique", query.getMemcacheInnerKey());
		U result = MemcacheWrapper.get().get(key);
		if(result == null && !MemcacheWrapper.get().contains(key)) {
			result = query.runQuery(pmSession);
			MemcacheWrapper.get().put(key, result);
			MemcacheWrapper.get().checkIn(query, key);
			if(result != null) {
				MemcacheWrapper.get().checkIn(result, key);
			}
		}
		
		return result;
	}
	
	public <T extends IEntity<T>> T insertUnique(IEntityQuery<T> query) throws CommunarchyPersistenceException {
		
		T originalValue = selectUnique(query);
		if(originalValue != null) {
			throw new CommunarchyPersistenceException("To insert new unique entities, there " +
					"must be no existing ones matching the query you specified");
		}
		
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "getUnique", query.getMemcacheInnerKey());
		MemcacheWrapper.get().remove(key);
		
		originalValue = query.getNewEntity();
		MemcacheWrapper.get().checkOut(originalValue);
		pmSession.getPM().makePersistent(originalValue);
		
		return originalValue;
	}

	public <T extends IEntity<T>> void deleteUnique(IEntityQuery<T> query) throws CommunarchyPersistenceException {
		if(query == null) {
			throw new CommunarchyPersistenceException("Cannot resolve delete target with a null query");
		}
		
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "getUnique", query.getMemcacheInnerKey());
		MemcacheWrapper.get().remove(key);
		
		T existingVal = query.runQuery(pmSession);
		if(existingVal == null) {
			throw new CommunarchyPersistenceException("For an entity to be deleted, it must exist in the first place");
		}
		
		MemcacheWrapper.get().checkOut(existingVal);
		pmSession.getPM().deletePersistent(existingVal);
	}
	
	public <T extends IEntity<T>> T updateUnique(IEntityQuery<T> query, T newVal) throws CommunarchyPersistenceException {
	
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "getUnique", query.getMemcacheInnerKey());
		MemcacheWrapper.get().remove(key);
		MemcacheWrapper.get().checkOut(newVal);
		
		if(!JDOHelper.isPersistent(newVal) && !JDOHelper.isTransactional(newVal)) {
			T existingVal = query.runQuery(pmSession);	
			if(existingVal == null) {
				throw new CommunarchyPersistenceException("Cannot update an entity that doesn't exist in the first place.");
			}
	
			existingVal.update(newVal);
			newVal = existingVal;
		}
		
		pmSession.getPM().makePersistent(newVal);
		return newVal;
	}
}