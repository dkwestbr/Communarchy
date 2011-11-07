package communarchy.facts.mappers.interfaces;

import com.google.appengine.api.datastore.Key;
import communarchy.facts.implementations.Argument;
import communarchy.facts.results.PageSet;

public abstract interface IPostMapper<T> {
	T selectPostById(Key id);
	void insertNewPost(T post);
	PageSet<Argument> buildPostFeeed(int numArgs, String startCursor);
}