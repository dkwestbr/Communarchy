package communarchy.facts.implementations;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IUser;

public final class UnauthenticatedUser implements IUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4056587213200216344L;

	public static IUser getNewUser() {
		return new UnauthenticatedUser();
	}
	
	private UnauthenticatedUser() {}
	
	@Override
	public Key getUserId() {
		return null;
	}

	@Override
	public Integer getUserRoleId() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Login or register";
	}

	@Override
	public String getHref() {
		return "/login";
	}

	/*
	 * Setters do nothing.
	 */
	@Override
	public void setHref(String href) {}

	@Override
	public void setUserRoleId(Integer id) {}

	@Override
	public boolean isAuthenticated() {
		return false;
	}
}
