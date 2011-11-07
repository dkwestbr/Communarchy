package communarchy.facts.interfaces;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

public interface IArgument {
	public Key getArgId();
	public Key getPosterId();
	public String getTitle();
	public String getWebFriendlyTitle();
	public String getContent();
	public Date getPostDate();
	public Date getUpdateDate();
}