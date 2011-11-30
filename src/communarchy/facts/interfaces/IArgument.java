package communarchy.facts.interfaces;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

public interface IArgument extends Serializable, IEntity {
	public Key getArgId();
	public Key getPosterId();
	public String getTitle();
	public String getWebFriendlyTitle();
	public String getContent();
	public Date getPostDate();
	public Date getUpdateDate();
}