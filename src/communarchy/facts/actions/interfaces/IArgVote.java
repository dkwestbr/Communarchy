package communarchy.facts.actions.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IArgVote {
	public Key getArgKey();
	public Key getKey();
	public Integer getVoteState();
	Key getUserKey();
}
