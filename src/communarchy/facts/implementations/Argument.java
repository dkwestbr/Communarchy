package communarchy.facts.implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import communarchy.facts.interfaces.IArgument;

@PersistenceCapable
public class Argument implements IArgument<Argument>, Serializable {

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
	private Text title;
	
	@Persistent
	private Text content;
	
	@Persistent
	private Date createDate;
	
	@Persistent
	private Date updateDate;
	
	@NotPersistent
	private List<String> checkOutKeys;
	
	public Argument(){}
	
	public Argument(Key poster_id, String title, String content) {
		if(poster_id == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.posterId = poster_id;
		this.title = new Text(title);
		this.content = new Text(content);
		this.createDate = new Date();
		this.updateDate = createDate;
		
		InitCheckOutKeys(this);
	}
	
	private static void InitCheckOutKeys(Argument arg) {
		arg.checkOutKeys = new ArrayList<String>();
		arg.checkOutKeys.add(String.format("%s(%s)", Argument.class.getName(), arg.getPosterId().toString()));
		arg.checkOutKeys.add(String.format("%s(%s)", Argument.class.getName(), arg.getTitle().toString()));
	}

	@Override
	public String getContent() {
		return content.getValue();
	}

	@Override
	public String getTitle() {
		return title.getValue();
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
	public Date getCreatedDate() {
		return createDate;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public String getWebFriendlyTitle() {
		return title.getValue().replaceAll("[^A-Za-z0-9 ]", "").replaceAll(" ", "-");
	}

	@Override
	public Key getKey() {
		return this.argId;
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			InitCheckOutKeys(this);
		}
		
		return this.checkOutKeys;
	}

	@Override
	public Text getRawTitle() {
		return this.title;
	}
	
	@Override
	public Text getRawContent() {
		return this.content;
	}
	
	@Override
	public void update(Argument updateValue) {
		this.content = updateValue.getRawContent();
		this.checkOutKeys = updateValue.getCheckOutKeys();
		this.title = updateValue.getRawTitle();
		this.updateDate = updateValue.getUpdateDate();
	}
}