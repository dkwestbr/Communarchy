package communarchy.facts.actions.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IVote {
	public Key getPointKey();
	public Key getPovKey();
	public Key getKey();
	Key getUserKey();
}
