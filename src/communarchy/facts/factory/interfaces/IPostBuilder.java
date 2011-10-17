package communarchy.facts.factory.interfaces;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import communarchy.exceptions.CommunarchyPersistenceException;

public interface IPostBuilder {
	Key createPost(Key posterId, Map<String, String> requiredFields,
			Key parentKey) throws CommunarchyPersistenceException;
	Key getParentId(Key resourceKey);
}