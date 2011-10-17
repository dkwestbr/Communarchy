package communarchy.facts;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import communarchy.facts.mappers.interfaces.AbstractMapper;

public class PMSession {
	private static Map<PersistenceManager, PMSession> INSTANCE_MAP;
	
	private PersistenceManager pm;
	private Map<Type, AbstractMapper<?>> mapperMap;
	
	private static final Logger log = Logger.getLogger(PMSession.class.getName());
	
	private PMSession() {}
	private PMSession(PersistenceManager pm) {
		this.pm = pm;
		mapperMap = new HashMap<Type, AbstractMapper<?>>();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractMapper<T>> T getMapper(Class<T> mapperClass) {
		
		T mapper = null;
		if(mapperMap.containsKey(mapperClass)) {
			mapper = (T) mapperMap.get(mapperClass);
		} else {
			try {
				mapper = mapperClass.newInstance();
				mapper.init(this);
				mapperMap.put(mapperClass, mapper);
			} catch (InstantiationException e) {
				log.severe(String.format("Problem instantiating mapper: %s", e.getMessage()));
			} catch (IllegalAccessException e) {
				log.severe(String.format("Problem instantiating mapper: %s", e.getMessage()));
			}			
		}
		
		return mapper;
	}
	
	public PersistenceManager getPM() {
		return this.pm;
	}
	
	public static PMSession getOpenSession(PersistenceManager pm) {
		if(INSTANCE_MAP == null) {
			INSTANCE_MAP = new HashMap<PersistenceManager, PMSession>();
		}
		
		if(INSTANCE_MAP.containsKey(pm)) {
			return INSTANCE_MAP.get(pm);
		} else {
			PMSession session = new PMSession(pm);
			INSTANCE_MAP.put(pm, session);
			return session;
		}
	}
	
	public void closeIt() {
		INSTANCE_MAP.remove(pm);
		pm.close();
	}
}