package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface IUserStance extends Serializable, IStance {
	
	public Key getUser();
	public void setStance(Integer newStance);
}
