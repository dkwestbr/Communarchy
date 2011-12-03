package communarchy.facts.mappers;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.list.IListQuery;
import communarchy.utils.caching.MemcacheWrapper;

public class QueryMapper extends AbstractMapper<QueryMapper> {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <U extends IEntity, T extends IListQuery<U>> List<U> runListQuery(IListQuery<U> queryObject) {
		if(queryObject == null) {
			return null;
		}
		
		String key = MemcacheWrapper.BuildKey(QueryMapper.class, "runListQuery", queryObject.getMemcacheInnerKey());
		List<U> results = queryObject.runListQuery(pmSession);
		
		String ids = MemcacheWrapper.get().get(key);
		if(ids != null) {
			List<Key> keys = MemcacheWrapper.keyStringToKeyList(ids, queryObject.getClass().getSimpleName());
			results = new ArrayList<U>();
			for(Key keyVal : keys) {
				U result = pmSession.getMapper(BasicMapper.class).getById(queryObject.getType(), keyVal);
				results.add(result);
			}
		} else if(!MemcacheWrapper.get().contains(key)) {
			results = queryObject.runListQuery(pmSession);
			
			if(results != null && !results.isEmpty()) {
				ids = MemcacheWrapper.joinKeys((List) results);
				MemcacheWrapper.get().put(key, ids);
				for(U result : results) {
					MemcacheWrapper.get().checkIn(result.getMemcacheCheckinKey(), key);
				}
				
				MemcacheWrapper.get().checkIn(queryObject.getType().toString(), key);
				
				if(queryObject.isRanked()) {
					MemcacheWrapper.get().checkIn(queryObject.getRankChangeKeys(), key);
				}
			}
		}
		
		return results;
	}
}