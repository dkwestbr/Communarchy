package communarchy.facts.mappers;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IUserMapper;
import communarchy.utils.caching.CommunarchyCache;

public class UserMapper extends AbstractMapper<UserMapper> implements IUserMapper {
	
	public UserMapper() {}

	@SuppressWarnings("unchecked")
	@Override
	public ApplicationUser getUserByDisplayName(String displayName) {
		String key = CommunarchyCache.buildKey(UserMapper.class, "getUserByDisplayName", displayName);
		ApplicationUser user = CommunarchyCache.getInstance().get(key);
		
		if(user == null) {
			Query q = pmSession.getPM().newQuery(ApplicationUser.class);
			List<ApplicationUser> users;
			try {
				q.setFilter("displayName == displayNameParam");
				q.declareParameters("String displayNameParam");
				users = (List<ApplicationUser>) q.execute(displayName);	
			} finally {
				q.closeAll();
			}
			
			if(users != null && !users.isEmpty()) {
				user = users.get(0);
				CommunarchyCache.getInstance().putEntity(user.getUserId().toString(), key, user);
			}
		}
		
		return user;
	}

	@Override
	public void updateUser(ApplicationUser user) {
		pmSession.getPM().makePersistent(user);
		CommunarchyCache.getInstance().clearEntity(user.getUserId().toString());
	}

	@Override
	public ApplicationUser getUserById(Key id) {
		String key = CommunarchyCache.buildKey(UserMapper.class, "getUserById", id.toString());
		ApplicationUser user = CommunarchyCache.getInstance().get(id.toString());
		if(user == null) {
			user = pmSession.getPM().getObjectById(ApplicationUser.class, id);
			CommunarchyCache.getInstance().putEntity(user.getUserId().toString(), key, user);
		}
		
		return pmSession.getPM().getObjectById(ApplicationUser.class, id);
	}
}