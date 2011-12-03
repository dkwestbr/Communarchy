package communarchy.facts.queries.list;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Point;

public class PointsByArgument implements IListQuery<Point> {

	@SuppressWarnings("unused")
	private PointsByArgument() {}
	
	private Key argId;
	private String memcacheKey;
	
	public PointsByArgument(Key argId) {
		this.argId = argId;
		
		this.memcacheKey = String.format("%s_%s", PointsByArgument.class.getName(), argId);
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
	public String getRankChangeKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRanked() {
		return true;
	}
}