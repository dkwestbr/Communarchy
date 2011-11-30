package communarchy.facts.queries.list;

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
	
	public GetVoteCountQuery(Key povId) {
		this.povId = povId;
		
		this.memCacheKey = String.format("%s_%s", GetVoteCountQuery.class.getName(), povId.toString());
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
	
	    Integer count = 0;
	    try {
	    	results = (List<VoteCounter>) query.execute(povId);
	    	
	    	if(results != null) {
	    		for(VoteCounter result : results) {
	    			count += result.getCount();
	    		}
	    	}
	    } finally {
	    	query.closeAll();
	    }
		
		return results;
	}

	@Override
	public Class<VoteCounter> getType() {
		return VoteCounter.class;
	}

	@Override
	public String getExpiryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRankChangeKey() {
		return null;
	}

	@Override
	public boolean isRanked() {
		return false;
	}
}