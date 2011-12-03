package communarchy.facts.queries.entity;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.actions.Vote;

public class GetVoteQuery implements IEntityQuery<Vote> {

	@SuppressWarnings("unused")
	private GetVoteQuery() {}
	private String memCacheKey;
	
	private Key povId;
	private Key pointId;
	private Key userId;
	
	public GetVoteQuery(Key pointId, Key povId, Key userId) {
		this.pointId = pointId;
		this.povId = povId;
		this.userId = userId;
		
		this.memCacheKey = String.format("%s_%s_%s", GetVoteQuery.class.getName(), povId.toString(), userId.toString());
	}

	@Override
	public final String getMemcacheInnerKey() {
		return memCacheKey;
	}

	@Override
	public Vote getNewEntity() {
		return new Vote(pointId, povId, userId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Vote runQuery(PMSession pmSession) {
		Query q = pmSession.getPM().newQuery(Vote.class);
		List<Vote> results = null;
		try {
			String povParam = String.format(PARAM_FORMAT, Vote.P_POV_KEY);
			String userParam = String.format(PARAM_FORMAT, Vote.P_USER_KEY);
			q.setFilter(String.format("%s == %s && %s == %s", Vote.P_POV_KEY, povParam, Vote.P_USER_KEY, userParam));
			q.declareParameters(String.format("%s %s, %s %s", Key.class.getName(), povParam, Key.class.getName(), userParam));
			results = (List<Vote>) q.execute(povId, userId);
		} finally {
			q.closeAll();
		}
		
		return results == null || results.isEmpty() ? null : results.get(0);
	}
}