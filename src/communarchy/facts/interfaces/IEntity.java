package communarchy.facts.interfaces;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.Key;

public interface IEntity extends Serializable {
	public Key getKey();
	//public List<String> getMemcacheCheckinKey();
}