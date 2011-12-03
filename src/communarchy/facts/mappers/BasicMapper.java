package communarchy.facts.mappers;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;

public class BasicMapper extends AbstractMapper<BasicMapper> {

	public <T extends IEntity> void persist(T val) {
		pmSession.getPM().makePersistent(val);
	}
	
	public <T extends IEntity> void delete(T val) {
		pmSession.getPM().deletePersistent(val);
	}
	
	public <T extends IEntity> T getById(Class<T> type, Key id) {
		if(id == null) {
			return null;
		}
		
		T entity = pmSession.getPM().getObjectById(type, id);
		/*
		String key = MemcacheWrapper.buildKey(BasicMapper.class, "get", id.toString());
		T entity = MemcacheWrapper.get().get(key);
		if(entity == null && !MemcacheWrapper.get().contains(key)) {
			entity = pmSession.getPM().getObjectById(type, id);
		}
		*/
		
		return entity;
	}
}