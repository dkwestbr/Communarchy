package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.counters.RepCounter;
import communarchy.facts.counters.StanceCounter;

public class GetRepCount implements IListQuery<RepCounter> {
	
	private Key userId;
	private String memcacheKey;
	
	@SuppressWarnings("unused")
	private GetRepCount() {}
	
	private List<String> checkInKeys;
	
	public GetRepCount(Key userId) {
		this.userId = userId;
		
		this.memcacheKey = String.format("%s_%s", 
				GetRepCount.class.getName(), userId);
		
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s)", StanceCounter.class.getName(), userId.toString()));
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RepCounter> runListQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(RepCounter.class);
		query.setFilter("userKey == userKeyParam");
		query.declareParameters(String.format("%s userKeyParam",
												Key.class.getName()));
		
		List<RepCounter> results = null;
	
	    
	    try {
	    	results = (List<RepCounter>) query.execute(userId);
	    } finally {
	    	query.closeAll();
	    }
	    
	    return results;
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}

	@Override
	public Class<RepCounter> getResourceType() {
		return RepCounter.class;
	}
}