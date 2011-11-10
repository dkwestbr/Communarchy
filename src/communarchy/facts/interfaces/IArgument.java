package communarchy.facts.interfaces;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public interface IArgument extends Serializable {
	public Key getArgId();
	public Key getPosterId();
	public String getTitle();
	public String getWebFriendlyTitle();
	public Text getContent();
	public Date getPostDate();
	public Date getUpdateDate();
}