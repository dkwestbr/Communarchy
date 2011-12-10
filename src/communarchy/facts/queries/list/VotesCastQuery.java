package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.actions.Vote;

public class VotesCastQuery implements IListQuery<Vote> {

	@SuppressWarnings("unused")
	private VotesCastQuery() {}
	
	private Key pointId;
	private Key userId;
	private String memcacheKey;
	
	private List<String> checkInKeys;
	
	public VotesCastQuery(Key pointId, Key userId) {
		this.pointId = pointId;
		this.userId = userId;
		
		this.memcacheKey = String.format("%s_%s_%s", VotesCastQuery.class, pointId.toString(), userId.toString());
		
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(String.format("%s(%s_%s)", Vote.class.getName(), pointId.toString(), userId.toString()));
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Vote> runListQuery(PMSession pmSession) {
		List<Vote> votes = null;
		Query q = pmSession.getPM().newQuery(Vote.class);
		try {
			q.setFilter("pointKey == pointKeyParam && userKey == userKeyParam");
			q.declareParameters(String.format("%s pointKeyParam, %s userKeyParam", Key.class.getName(), Key.class.getName()));
			votes = (List<Vote>) q.execute(pointId, userId);
		} finally {
			q.closeAll();
		}
		
		return votes;
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}

	@Override
	public Class<Vote> getResourceType() {
		return Vote.class;
	}
}