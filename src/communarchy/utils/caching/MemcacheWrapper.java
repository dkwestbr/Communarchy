package communarchy.utils.caching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import communarchy.facts.interfaces.IEntity;

public class MemcacheWrapper {
	private static MemcacheWrapper INSTANCE = null;
	private Map<String, List<String>> cacheKeyRegistry;
	private final ReentrantLock lock = new ReentrantLock();
	
	private static final Logger log =
		      Logger.getLogger(MemcacheWrapper.class.getName());
	
	private MemcacheWrapper() {
		cacheKeyRegistry = new ConcurrentHashMap<String, List<String>>();
	}
	
	public void checkIn(String listen, String key) {
		if(cacheKeyRegistry.containsKey(listen)) {
			cacheKeyRegistry.get(listen).add(key);
		} else {
			lock.lock();
			try {
				if(!cacheKeyRegistry.containsKey(key)) {
					List<String> newList = new ArrayList<String>();
					newList.add(key);
					cacheKeyRegistry.put(listen, newList);
				}
			} finally {
				lock.unlock();
			}
		}
	}
	
	public void checkIn(List<String> listeners, String key) {
		for(String listener : listeners) {
			checkIn(listener, key);
		}
	}
	
	public void checkOut(String listen) {
		if(cacheKeyRegistry.containsKey(listen)) {
			AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
			Future<Set<String>> futureVal = asyncCache.deleteAll(cacheKeyRegistry.get(listen));
			while(!futureVal.isDone() && !futureVal.isCancelled()) {}
			cacheKeyRegistry.remove(listen);
		}
	}
	
	public static synchronized MemcacheWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new MemcacheWrapper();
		}
		
		return INSTANCE;
	}
	
	public boolean contains(String key) {
		boolean returnVal = false;
		try {	
			Future<Boolean> future = MemcacheServiceFactory.getAsyncMemcacheService().contains(key);
			while(!future.isDone() && !future.isCancelled()) {}
			Boolean val = future.get();
			returnVal = val == null ? false : val;
		} catch (InterruptedException e) {
			log.warning("Unable to confirm object exists in memcache");
		} catch (ExecutionException e) {
			log.warning("Unable to confirm object exists in memcache");
		}
		
		return returnVal;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		T val = null;
		try {
			Future<Object> future = MemcacheServiceFactory.getAsyncMemcacheService().get(key);	
			while(!future.isDone() && !future.isCancelled()) {}	
			val = (T) future.get();
		} catch (InterruptedException e) {
			log.warning("Unable to retrieve object from memcache");
		} catch (ExecutionException e) {
			log.warning("Unable to retrieve object from memcache");
		}
		
		return val;
	}
	
	public static String BuildKey(Class<?> type, String method, String innerKey) {
		return String.format("%s_%s(%s)", type.getName(), method, innerKey);
	}
	
	public static String BuildParentKey(Class<?> type, String parent) {
		return String.format("PARENT_%s(%s)", type.getName(), parent);
	}
	
	public void put(String key, Serializable value) {
		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
	    Future<Void> futureValue = asyncCache.put(key, value);
	    while(!futureValue.isDone() && !futureValue.isCancelled()) {}
	}
	
	public void remove(String key) {
		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
		Future<Boolean> futureVal = asyncCache.delete(key);	
		while(!futureVal.isDone() && !futureVal.isCancelled()) {}	
	}

	public static <T extends IEntity> String joinKeys(List<T> entities) {
		if(entities == null || entities.isEmpty()) {
			return null;
		}
		
		StringBuilder out = new StringBuilder();
		for(IEntity entity : entities) {
			out.append(String.format("%d;", entity.getKey().getId()));
		}
		return out.toString();
	}

	public static List<Key> keyStringToKeyList(String idString, String keyType) {
		if(idString == null || !idString.contains(";")) {
			return null;
		}
		
		String[] ids = idString.split(";");
		List<Key> keyList = new ArrayList<Key>();
		for(String id : ids) {
			keyList.add(KeyFactory.createKey(keyType, Long.parseLong(id)));
		}
		
		return keyList;
	}
}