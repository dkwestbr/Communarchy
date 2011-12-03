package communarchy.utils.caching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import communarchy.facts.interfaces.IEntity;

public class CommunarchyCache {
	private static CommunarchyCache INSTANCE = null;
	private Map<String, List<String>> updateRegistry;
	private Map<String, List<String>> updateListRegistry;
	private Lock lock = null;
	
	private static final Logger log =
		      Logger.getLogger(CommunarchyCache.class.getName());
	
	private CommunarchyCache() {
		updateRegistry = new ConcurrentHashMap<String, List<String>>();
		updateListRegistry = new ConcurrentHashMap<String, List<String>>();
		lock = new ReentrantLock();
	}
	
	public static synchronized CommunarchyCache getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new CommunarchyCache();
		}
		
		return INSTANCE;
	}
	
	public boolean contains(String key) {
		boolean returnVal = false;
		try {
			Future<Boolean> future = MemcacheServiceFactory.getAsyncMemcacheService().contains(key);
			//lock.lock();
			while(!future.isDone() && !future.isCancelled()) {}
			Boolean val = future.get();
			returnVal = val == null ? false : val;
		} catch (InterruptedException e) {
			log.warning("Unable to confirm object exists in memcache");
		} catch (ExecutionException e) {
			log.warning("Unable to confirm object exists in memcache");
		} finally {
			//lock.unlock();
		}
		
		return returnVal;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		T val = null;
		try {
			Future<Object> future = MemcacheServiceFactory.getAsyncMemcacheService().get(key);
			//lock.lock();
			while(!future.isDone() && !future.isCancelled()) {}	
			val = (T) future.get();
		} catch (InterruptedException e) {
			log.warning("Unable to retrieve object from memcache");
		} catch (ExecutionException e) {
			log.warning("Unable to retrieve object from memcache");
		} finally {
			//lock.unlock();
		}
		
		return val;
	}

	public void putList(String catalyst, String key, List<IEntity> entities) {
		String value = ListUtils.joinKeys(entities);
		put(catalyst, key, value, updateListRegistry);
	}
		
	public void putEntity(String catalyst, String key, Serializable value) {
		put(catalyst, key, value, updateRegistry);
	}
	
	public <T> void updateEntityLists(Class<T> type) {
		
	}
	
	public void clearEntity(String catalyst) {
		clear(catalyst, updateRegistry);
	}
	
	public void clearList(String catalyst) {
		clear(catalyst, updateListRegistry);
	}
	
	public static <T> String buildKey(Class<T> type, String method, String innerKey) {
		return String.format("%s_%s(%s)", type.getName(), method, innerKey);
	}
	
	private void put(String catalyst, String key, Serializable value, Map<String, List<String>> registry) {
		registerCatalyst(catalyst, key, registry);
		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
	    Future<Void> futureValue = asyncCache.put(key, value);
	    //lock.lock();
	    try {
	    	while(!futureValue.isDone() && !futureValue.isCancelled()) {}
	    } finally {
	    	//lock.unlock();
	    }
	}
	
	private void clear(String catalyst, Map<String, List<String>> registry) {
		if(registry.containsKey(catalyst)) {
			AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
			Future<Set<String>> futureVal = asyncCache.deleteAll(registry.get(catalyst));
			//lock.lock();
			try {
				while(!futureVal.isDone() && !futureVal.isCancelled()) {}
				registry.remove(catalyst);
			} finally {
				//lock.unlock();
			}
		}
	}

	private void registerCatalyst(String catalyst, String key, Map<String, List<String>> registry) {
		List<String> keys = registry.get(catalyst);
		if(keys == null) {
			//lock.lock();
			try {
				if(keys == null) {
					keys = new ArrayList<String>();
					registry.put(catalyst, keys);
				}
			} finally {
				//lock.unlock();
			}
		}
		keys.add(key);
	}
}