package communarchy.facts.mappers.interfaces;

import java.util.List;

import com.google.appengine.api.datastore.Key;

public abstract interface IPostHasChildrenMapper<T> {
	List<T> selectChildrenPosts(Key id);
}
