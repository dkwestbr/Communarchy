package communarchy.facts.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IArgument {
	public Key getArgId();
	public Key getPosterId();
	public String getTitle();
	public String getContent();
}