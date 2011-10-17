package communarchy.facts.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IUser {
	Key getUserId();
	Integer getUserRoleId();
	String getDisplayName();
	String getHref();
	
	boolean isAuthenticated();
	
	void setUserRoleId(Integer id);
	void setHref(String href);
}
