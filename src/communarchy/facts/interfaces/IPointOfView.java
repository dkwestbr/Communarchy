package communarchy.facts.interfaces;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public interface IPointOfView<T extends IEntity<T>> extends Serializable, IEntity<T> {
	public static int FLAG_PROFANITY = 0;
	public static int FLAG_OFFENSIVE = 1;
	public Key getPosterId();
	public int getStance();
	public Key getParentPointId();
	public String getPov();
	public Text getRawPov();
	public Date getUpdateDate();
}
