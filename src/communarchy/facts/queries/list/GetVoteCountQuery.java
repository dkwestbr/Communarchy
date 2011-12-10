package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.counters.VoteCounter;

public class GetVoteCountQuery implements IListQuery<VoteCounter> {

	@SuppressWarnings("unused")
	private GetVoteCountQuery() {}
	private String memCacheKey;
	
	private Key povId;
	
	private List<String> checkInKeys;
	
	public GetVoteCountQuery(Key povId) {
		this.povId = povId;
		
		this.memCacheKey = String.format("%s_%s", GetVoteCountQuery.class.getName(), povId.toString());
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s)", VoteCounter.class.getName(), povId.toString()));
	}

	@Override
	public final String getMemcacheInnerKey() {
		return memCacheKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VoteCounter> runListQuery(PMSession pmSession) {
		Query query = pmSession.getPM().newQuery(VoteCounter.class);
		query.setFilter("povKey == povKeyParam");
		query.declareParameters(String.format("%s povKeyParam", Key.class.getName()));
		
		List<VoteCounter> results = null;
	
	    try {
	    	results = (List<VoteCounter>) query.execute(povId);
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
	public Class<VoteCounter> getResourceType() {
		return VoteCounter.class;
	}
}