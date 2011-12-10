package communarchy.facts.mappers;

import javax.jdo.JDOHelper;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.utils.caching.MemcacheWrapper;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class BasicMapper extends AbstractMapper<BasicMapper> {
	public <T extends IEntity<T>> void insert(T val) throws CommunarchyPersistenceException {
		if(val == null || val.getKey() != null) {
			throw new CommunarchyPersistenceException("Insertion value may not be null and entity key MUST be null");
		}
		
		MemcacheWrapper.get().checkOut(val);
		pmSession.getPM().makePersistent(val);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IEntity<T>> void update(T val) throws CommunarchyPersistenceException {
		if(val == null || val.getKey() == null) {
			throw new CommunarchyPersistenceException("Update value may not be null and entity key must NOT be null");
		}
		
		MemcacheWrapper.get().checkOut(val);
		String key = MemcacheWrapper.buildKey(val.getClass(), "get", val.getKey().toString());
		MemcacheWrapper.get().remove(key);
		
		if(!JDOHelper.isPersistent(val) && !JDOHelper.isTransactional(val)) {
			T persistentObject = (T) pmSession.getPM().getObjectById(val.getClass(), val.getKey());
			if(persistentObject == null) {
				throw new CommunarchyPersistenceException("For an entity to be updated, it must exist in the first place");
			}
			
			persistentObject.update(val);
			val = persistentObject;
		}
		
		pmSession.getPM().makePersistent(val);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IEntity<T>> void delete(T val) throws CommunarchyPersistenceException {
		if(val == null || val.getKey() == null) {
			throw new CommunarchyPersistenceException("Cannot delete null entities or entities without keys");
		}
	
		MemcacheWrapper.get().checkOut(val);
		String key = MemcacheWrapper.buildKey(val.getClass(), "get", val.getKey().toString());
		MemcacheWrapper.get().remove(key);
		
		if(!JDOHelper.isPersistent(val) && !JDOHelper.isTransactional(val)) {
			T persistentObject = (T) pmSession.getPM().getObjectById(val.getClass(), val.getKey());
			if(persistentObject == null) {
				throw new CommunarchyPersistenceException("For an entity to be updated, it must exist in the first place");
			}
			
			persistentObject.update(val);
			val = persistentObject;
		}
		
		pmSession.getPM().deletePersistent(val);
	}
	
	public <T extends IEntity<T>> T select(Class<T> type, Key id) throws CommunarchyPersistenceException {
		if(type == null || id == null) {
			throw new CommunarchyPersistenceException("Values cannot be selected wihtout a non-null type and key");
		}
		
		String key = MemcacheWrapper.buildKey(type, "get", id.toString());
		T val = MemcacheWrapper.get().get(key);
		if(val == null && !MemcacheWrapper.get().contains(key)) {
			val = pmSession.getPM().getObjectById(type, id);
			MemcacheWrapper.get().put(key, val);
			if(val != null) {
				MemcacheWrapper.get().checkIn(val, key);
			}
		}
		
		return val;
	}
}