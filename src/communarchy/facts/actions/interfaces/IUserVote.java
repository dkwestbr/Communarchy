package communarchy.facts.actions.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IUserVote extends IVote {
	public Key getUserKey();
}
