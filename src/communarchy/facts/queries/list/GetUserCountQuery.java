package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import communarchy.facts.PMSession;
import communarchy.facts.counters.UserCounter;

public class GetUserCountQuery implements IListQuery<UserCounter> {

	private String memcacheKey;
	private List<String> checkInKeys;
	
	public GetUserCountQuery() {
		this.memcacheKey = GetUserCountQuery.class.getName();
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(UserCounter.class.getName());
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return this.memcacheKey;
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserCounter> runListQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(UserCounter.class);
		List<UserCounter> results = null;
	
	    try {
	    	results = (List<UserCounter>) query.execute();
	    } finally {
	    	query.closeAll();
	    }
		
		return results;
	}

	@Override
	public Class<UserCounter> getResourceType() {
		return UserCounter.class;
	}
}