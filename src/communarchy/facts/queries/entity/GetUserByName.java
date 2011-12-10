package communarchy.facts.queries.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;

public class GetUserByName implements IEntityQuery<ApplicationUser> {

	private String displayName;
	private String memcacheKey;
	
	@SuppressWarnings("unused")
	private GetUserByName() {}
	
	public GetUserByName(String name) {
		this.displayName = name;
		
		this.memcacheKey = String.format("%s_%s", GetUserByName.class.getName(), displayName);
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return this.memcacheKey;
	}

	@Override
	public ApplicationUser getNewEntity() {
		return new ApplicationUser(displayName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ApplicationUser runQuery(PMSession pmSession) {
		Query q = pmSession.getPM().newQuery(ApplicationUser.class);
		List<ApplicationUser> users;
		try {
			q.setFilter("displayName == displayNameParam");
			q.declareParameters("String displayNameParam");
			users = (List<ApplicationUser>) q.execute(displayName);	
		} finally {
			q.closeAll();
		}
		
		return users == null || users.isEmpty() ? null : users.get(0);
	}

	
	private static List<String> EMPTY_LIST = new ArrayList<String>();
	@Override
	public List<String> getCheckInKeys() {
		return EMPTY_LIST;
	}
}