package communarchy.facts.queries.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;

public class UserStanceQuery implements IEntityQuery<UserStance> {

	@SuppressWarnings("unused")
	private UserStanceQuery() {}
	
	private Key pointKey;
	private Key userKey;
	private Integer stance;
	private String memcacheKey;
	
	private List<String> checkInKeys;
	
	public UserStanceQuery(Key pointKey, Key userKey, Integer stance) {
		if(pointKey == null || userKey == null) {
			throw new NullPointerException("Parameters may not be null");
		}
		
		this.pointKey = pointKey;
		this.userKey = userKey;
		this.stance = stance;
		
		this.memcacheKey = String.format("%s_%s_%s", UserStanceQuery.class.toString(), pointKey.toString(), userKey.toString());
		
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s_%s)", UserStance.class.getName(), userKey.toString(), pointKey.toString()));
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	public UserStance getNewEntity() {
		return new UserStance(userKey, pointKey, stance);
	}

	@Override
	@SuppressWarnings("unchecked")
	public UserStance runQuery(PMSession pmSession) {
		List<UserStance> results;
		Query q = pmSession.getPM().newQuery(UserStance.class);
		try {
			q.setFilter("user == userParam && point == pointParam");
			q.declareParameters(String.format("%s userParam, %s pointParam", Key.class.getName(), Key.class.getName()));
			results = (List<UserStance>) q.execute(userKey, pointKey);	
		} finally {
			q.closeAll();
		}
		
		return results == null || results.isEmpty() ? null : results.get(0);
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}
}