package communarchy.facts.mappers.interfaces;

import com.google.appengine.api.datastore.Key;

public abstract interface IPostMapper<T> {
	T selectPostById(Key id);
	void insertNewPost(T post);
}