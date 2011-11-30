package communarchy.facts.queries.list;

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
	
	public VotesCastQuery(Key pointId, Key userId) {
		this.pointId = pointId;
		this.userId = userId;
		
		this.memcacheKey = String.format("%s_%s_%s", VotesCastQuery.class, pointId.toString(), userId.toString());
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
	public Class<Vote> getType() {
		return Vote.class;
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