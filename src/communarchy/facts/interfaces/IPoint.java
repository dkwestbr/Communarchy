package communarchy.facts.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IPoint {
	public String getPoint();
	public Key getParentId();
	public Key getPointId();
	public Key getPosterId();
}