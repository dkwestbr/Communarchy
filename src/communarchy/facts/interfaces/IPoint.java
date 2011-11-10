package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface IPoint extends Serializable {
	public String getPoint();
	public Key getParentId();
	public Key getPointId();
	public Key getPosterId();
}