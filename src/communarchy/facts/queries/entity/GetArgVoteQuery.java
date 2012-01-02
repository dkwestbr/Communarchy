package communarchy.facts.queries.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.actions.ArgVote;

public class GetArgVoteQuery implements IEntityQuery<ArgVote> {

	@SuppressWarnings("unused")
	private GetArgVoteQuery() {}
	private String memCacheKey;
	
	private Key argId;
	private Key userId;
	private Integer state;
	
	private List<String> checkInKeys;
	
	public GetArgVoteQuery(Key argId, Key userId, Integer state) {
		this.argId = argId;
		this.userId = userId;
		this.state = state;
		
		this.checkInKeys = new ArrayList<String>();
		
		this.memCacheKey = String.format("%s_%s_%s", GetArgVoteQuery.class.getName(), argId.toString(), userId.toString());
		this.checkInKeys.add(String.format("%s(%s_%s)", ArgVote.class.getName(), argId.toString(), userId.toString()));
	}

	@Override
	public final String getMemcacheInnerKey() {
		if(userId != null) {
			
		}
		
		return memCacheKey;
	}

	@Override
	public ArgVote getNewEntity() {
		if(state.equals(Integer.MIN_VALUE)) {
			throw new NullPointerException("Before creating new entities, " +
					"must be explicitly set.");
		}
		
		return new ArgVote(argId, userId, state);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArgVote runQuery(PMSession pmSession) {
		Query q = pmSession.getPM().newQuery(ArgVote.class);
		List<ArgVote> results = null;
		try {
			String argParam = String.format(PARAM_FORMAT, ArgVote.P_ARG_KEY);
			String userParam = String.format(PARAM_FORMAT, ArgVote.P_USER_KEY);
			q.setFilter(String.format("%s == %s && %s == %s", ArgVote.P_ARG_KEY, argParam, ArgVote.P_USER_KEY, userParam));
			q.declareParameters(String.format("%s %s, %s %s", Key.class.getName(), argParam, Key.class.getName(), userParam));
			results = (List<ArgVote>) q.execute(argId, userId);
		} finally {
			q.closeAll();
		}
		
		return results == null || results.isEmpty() ? null : results.get(0);
	}

	@Override
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
}