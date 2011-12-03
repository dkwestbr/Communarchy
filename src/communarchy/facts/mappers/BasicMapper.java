package communarchy.facts.mappers;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.utils.caching.MemcacheWrapper;

public class BasicMapper extends AbstractMapper<BasicMapper> {

	public <T extends IEntity> void persist(T val) {
		pmSession.getPM().makePersistent(val);
		MemcacheWrapper.get().checkOut(val.getMemcacheCheckinKey());
	}
	
	public <T extends IEntity> void delete(T val) {
		MemcacheWrapper.get().checkOut(val.getMemcacheCheckinKey());
		pmSession.getPM().deletePersistent(val);
	}
	
	public <T extends IEntity> T getById(Class<T> type, Key id) {
		if(id == null) {
			return null;
		}
		
		String key = MemcacheWrapper.BuildKey(type, "get", id.toString());
		T entity = MemcacheWrapper.get().get(key);
		if(entity == null && !MemcacheWrapper.get().contains(key)) {
			entity = pmSession.getPM().getObjectById(type, id);
			MemcacheWrapper.get().put(key, entity);
			if(entity != null) {
				MemcacheWrapper.get().checkIn(entity.getMemcacheCheckinKey(), key);
			}
		}
		
		return entity;
	}
}