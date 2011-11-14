package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface IPoint extends Serializable, IEntity {
	public String getPoint();
	public Key getParentId();
	public Key getPosterId();
}