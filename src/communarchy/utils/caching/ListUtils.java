package communarchy.utils.caching;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import communarchy.facts.interfaces.IEntity;

public class ListUtils {
	private ListUtils() {}
	
	public static <T extends IEntity<T>> String joinKeys(List<T> entities) {
		if(entities == null || entities.isEmpty()) {
			return null;
		}
		
		StringBuilder out = new StringBuilder();
		for(IEntity<T> entity : entities) {
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