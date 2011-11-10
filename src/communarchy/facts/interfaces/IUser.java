package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface IUser extends Serializable {
	Key getUserId();
	Integer getUserRoleId();
	String getDisplayName();
	String getHref();
	
	boolean isAuthenticated();
	
	void setUserRoleId(Integer id);
	void setHref(String href);
}
