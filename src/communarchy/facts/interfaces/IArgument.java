package communarchy.facts.interfaces;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public interface IArgument<T extends IEntity<T>> extends Serializable, IEntity<T> {
	public Key getArgId();
	public Key getPosterId();
	public String getTitle();
	public Text getRawTitle();
	public String getWebFriendlyTitle();
	public String getContent();
	public Text getRawContent();
	public Date getCreatedDate();
	public Date getUpdateDate();
	public int getVotes();
	public void upVote();
	public void downVote();
}