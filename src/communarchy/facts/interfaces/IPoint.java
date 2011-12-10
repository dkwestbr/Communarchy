package communarchy.facts.interfaces;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public interface IPoint<T extends IEntity<T>> extends Serializable, IEntity<T> {
	public String getPoint();
	public Text getRawPoint();
	public Key getParentId();
	public Key getPosterId();
	public Date getUpdateDate();
}