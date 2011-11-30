package communarchy.facts.mappers;

import java.util.List;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.queries.list.IListQuery;

public class QueryMapper extends AbstractMapper<QueryMapper> {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <U extends IEntity, T extends IListQuery<U>> List<U> runListQuery(IListQuery<U> queryObject) {
		if(queryObject == null) {
			return null;
		}
		
		//String key = MemcacheWrapper.buildKey(QueryMapper.class, "runListQuery", queryObject.getMemcacheInnerKey());
		List<U> results = queryObject.runListQuery(pmSession);
		
		/*
		String ids = MemcacheWrapper.get().get(key);
		if(ids != null) {
			List<Key> keys = ListUtils.keyStringToKeyList(ids, queryObject.getClass().getSimpleName());
			results = new ArrayList<U>();
			for(Key keyVal : keys) {
				U result = (U) pmSession.getMapper(BasicMapper.class).getById(queryObject.getType(), keyVal);
				results.add(result);
			}
		} else if(!MemcacheWrapper.get().contains(key)) {
			results = queryObject.runListQuery(pmSession);
			
			if(results != null && !results.isEmpty()) {
				ids = ListUtils.joinKeys((List) results);
				MemcacheWrapper.get().put(key, ids);
				MemcacheWrapper.get().checkIn(results.get(0).getNewObjectKey(), key);
				if(queryObject.isRanked()) {
					MemcacheWrapper.get().checkIn(queryObject.getRankChangeKey(), key);
				}
			}
		}
		*/
		
		return results;
	}
}