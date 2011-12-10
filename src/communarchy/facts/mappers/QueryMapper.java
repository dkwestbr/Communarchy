package communarchy.facts.mappers;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.list.IListQuery;
import communarchy.facts.queries.list.IPagedQuery;
import communarchy.facts.results.PageSet;
import communarchy.utils.caching.ListUtils;
import communarchy.utils.caching.MemcacheWrapper;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class QueryMapper extends AbstractMapper<QueryMapper> {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <U extends IEntity<U>, T extends IListQuery<U>> List<U> runListQuery(IListQuery<U> queryObject) throws CommunarchyPersistenceException {
		if(queryObject == null) {
			return null;
		}
		
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "runListQuery", queryObject.getMemcacheInnerKey());
		List<U> results = null;
		
		String ids = MemcacheWrapper.get().get(key);
		if(ids != null) {
			List<Key> keys = ListUtils.keyStringToKeyList(ids, queryObject.getResourceType().getSimpleName());
			results = new ArrayList<U>();
			for(Key keyVal : keys) {
				U result = (U) pmSession.getMapper(BasicMapper.class).select(queryObject.getResourceType(), keyVal);
				results.add(result);
			}
		} else if(!MemcacheWrapper.get().contains(key)) {
			results = queryObject.runListQuery(pmSession);
			
			if(results != null && !results.isEmpty()) {
				ids = ListUtils.joinKeys((List) results);
				MemcacheWrapper.get().put(key, ids);
				for(U result : results) {
					MemcacheWrapper.get().checkIn(result, key);
				}
			} else {
				MemcacheWrapper.get().put(key, null);
			}
			
			MemcacheWrapper.get().checkIn(queryObject, key);
		}
		
		return results == null ? new ArrayList<U>() : results;
	}
	
	public <U extends IEntity<U>, T extends IListQuery<U>> PageSet<U> runPagedListQuery(IPagedQuery<U> queryObject) {
		if(queryObject == null) {
			return null;
		}
				
		String key = MemcacheWrapper.buildKey(QueryMapper.class, "runPagedListQuery", queryObject.getMemcacheInnerKey());
		PageSet<U> pageSet = MemcacheWrapper.get().get(key); 
		
		if(pageSet == null && !MemcacheWrapper.get().contains(key)) {
			List<U> results = queryObject.runListQuery(pmSession);
			
			if(results != null && !results.isEmpty()) {
				pageSet = new PageSet<U>(results, queryObject.getResourceType(),
										 queryObject.getStartCursorString(),
										 JDOCursorHelper.getCursor(results).toWebSafeString());
				
				MemcacheWrapper.get().put(key, pageSet);
			} else {
				MemcacheWrapper.get().put(key, null);
			}
			
			MemcacheWrapper.get().checkIn(queryObject, key);
		}
		
		return pageSet;
	}
}