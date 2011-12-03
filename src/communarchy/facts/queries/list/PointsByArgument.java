package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.controllers.strategies.displayRank.PointRankStrategy;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.Point;

public class PointsByArgument implements IListQuery<Point> {

	@SuppressWarnings("unused")
	private PointsByArgument() {}
	
	private Key argId;
	private String memcacheKey;
	private List<String> rankChangeKeys;
	
	public PointsByArgument(Key argId) {
		this.argId = argId;
		
		this.memcacheKey = String.format("%s_%s", PointsByArgument.class.getName(), argId);
		this.rankChangeKeys = new ArrayList<String>();
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Point> runListQuery(PMSession pmSession) {
		List<Point> points = null;
		
		Query q = pmSession.getPM().newQuery(Point.class);
		try {
			q.setFilter("parentArgId == parentIdParam");
			q.declareParameters(String.format("%s parentIdParam", Key.class.getName()));
			points = (List<Point>) q.execute(argId);
			Collections.sort(points, new PointRankStrategy(pmSession));
		} finally {
			q.closeAll();
		}
		
		return points;
	}

	@Override
	public Class<Point> getType() {
		return Point.class;
	}

	@Override
	public String getExpiryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRankChangeKeys() {
		return this.rankChangeKeys;
	}

	@Override
	public boolean isRanked() {
		return true;
	}
}