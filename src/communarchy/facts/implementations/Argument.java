package communarchy.facts.implementations;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IArgument;

@PersistenceCapable
public class Argument implements IArgument, Serializable {

	/**
	 * 
	 */
	private static transient final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key argId;
	
	@Persistent
	private Key posterId;
	
	@Persistent
	private String title;
	
	@Persistent
	private String content;
	
	@Persistent
	private Date createDate;
	
	@Persistent
	private Date updateDate;
	
	public Argument(){}
	
	public Argument(Key poster_id, String title, String content) {
		if(poster_id == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.posterId = poster_id;
		this.title = title;
		this.content = content;
		this.createDate = new Date();
		this.updateDate = createDate;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Key getArgId() {
		return argId;
	}

	@Override
	public Key getPosterId() {
		return posterId;
	}

	@Override
	public Date getPostDate() {
		return createDate;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public String getWebFriendlyTitle() {
		return title.replaceAll("[^A-Za-z0-9 ]", "").replaceAll(" ", "-");
	}
}