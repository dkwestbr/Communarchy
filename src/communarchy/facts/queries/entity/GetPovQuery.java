package communarchy.facts.queries.entity;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.actions.Vote;
import communarchy.facts.implementations.PointOfView;

public class GetPovQuery implements IEntityQuery<PointOfView> {

	@SuppressWarnings("unused")
	private GetPovQuery() {}
	private String memCacheKey;
	
	private Key pointId;
	private Key userId;
	private String pov;
	private Integer stance;
	
	public GetPovQuery(Key pointId, Key userId, String pov, Integer stance) {
		this.pointId = pointId;
		this.userId = userId;
		this.pov = pov;
		this.stance = stance;
		
		this.memCacheKey = String.format("%s_%s_%s_%d", GetPovQuery.class.getName(), pointId.toString(), userId.toString(), stance);
	}

	@Override
	public final String getMemcacheInnerKey() {
		return memCacheKey;
	}

	@Override
	public PointOfView getNewEntity() {
		return new PointOfView(pointId, userId, pov, stance);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PointOfView runQuery(PMSession pmSession) {
		Query q = pmSession.getPM().newQuery(Vote.class);
		List<PointOfView> results = null;
		try {
			String pointParam = String.format(PARAM_FORMAT, PointOfView.P_POINT_ID);
			String userParam = String.format(PARAM_FORMAT, PointOfView.P_POSTER_ID);
			String stanceParam = String.format(PARAM_FORMAT, PointOfView.P_STANCE);
			q.setFilter(String.format("%s == %s && %s == %s && %s == %d", 
					PointOfView.P_POINT_ID, pointParam, PointOfView.P_POSTER_ID, userParam, PointOfView.P_STANCE, stanceParam));
			q.declareParameters(String.format("%s %s, %s %s, %s %d", 
					Key.class.getName(), pointParam, Key.class.getName(), userParam, Integer.class.getName(), stanceParam));
			results = (List<PointOfView>) q.execute(pointId, userId, stance);
		} finally {
			q.closeAll();
		}
		
		return results == null || results.isEmpty() ? null : results.get(0);
	}
}