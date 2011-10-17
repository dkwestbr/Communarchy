package communarchy.facts.mappers.interfaces;

import java.util.List;

import com.google.appengine.api.datastore.Key;

public abstract interface IPostHasParentMapper<T, U> {
	public T selectParentPost(Key id);
	public List<U> selectAllChildrenByParent(Key id);
}
