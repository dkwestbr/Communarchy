package communarchy.facts.mappers;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IUserMapper;

public class UserMapper extends AbstractMapper<UserMapper> implements IUserMapper {
	
	public UserMapper() {}

	@SuppressWarnings("unchecked")
	@Override
	public ApplicationUser getUserByDisplayName(String displayName) {
		List<ApplicationUser> users;
		Query q = pmSession.getPM().newQuery(ApplicationUser.class);
		try {
			q.setFilter("displayName == displayNameParam");
			q.declareParameters("String displayNameParam");
			users = (List<ApplicationUser>) q.execute(displayName);	
		} finally {
			q.closeAll();
		}
		
		return users.isEmpty() ? null : users.get(0);
	}

	@Override
	public void updateUser(ApplicationUser user) {
		pmSession.getPM().makePersistent(user);
	}

	@Override
	public ApplicationUser getUserById(Key id) {
		return pmSession.getPM().getObjectById(ApplicationUser.class, id);
	}
}