package communarchy.facts.mappers.interfaces;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.implementations.ApplicationUser;

public interface IUserMapper {
	ApplicationUser getUserById(Key id);
	ApplicationUser getUserByDisplayName(String displayName);
	void updateUser(ApplicationUser user);
}
