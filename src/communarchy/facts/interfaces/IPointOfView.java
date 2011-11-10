package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface IPointOfView extends Serializable {
	public static int FLAG_PROFANITY = 0;
	public static int FLAG_OFFENSIVE = 1;
	public Key getPovId();
	public Key getPosterId();
	public int getStance();
	public Key getParentPointId();
	public String getPov();
}
