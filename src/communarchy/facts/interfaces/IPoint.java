package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public interface IPoint extends Serializable {
	public Text getPoint();
	public Key getParentId();
	public Key getPointId();
	public Key getPosterId();
}