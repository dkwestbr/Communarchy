package communarchy.facts;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import communarchy.facts.mappers.interfaces.AbstractMapper;

public class PMSession {

	private static PersistenceManagerFactory pmfInstance = null;
	private PersistenceManager pm = null;
	private Map<Type, AbstractMapper<?>> mapperMap = null;
	
	private static final Logger log = Logger.getLogger(PMSession.class.getName());
	
	private PMSession() {
		if(pmfInstance == null) {
			pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		}
		
		pm = pmfInstance.getPersistenceManager();
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
	
	public static PMSession getOpenSession() {
		return new PMSession();
	}
	
	public void closeIt() {
		if(!pm.isClosed()) {
			pm.close();
		}
	}
}