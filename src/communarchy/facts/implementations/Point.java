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

import communarchy.facts.interfaces.IPoint;

@PersistenceCapable
public class Point implements IPoint<Point>, Serializable {
	private static transient final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key pointId;
	
	@Persistent
	private Key posterId;
	
	@Persistent
	private Key parentArgId;
	
	@Persistent
	private Text point;
	
	@Persistent
	private Date createDate;
	
	@Persistent
	private Date updateDate;
	
	@NotPersistent
	private List<String> checkOutKeys;
	
	public Point(){}
	
	public Point(Key parent_arg_id, Key poster_id, String point) {
		if(parent_arg_id == null || poster_id == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.posterId = poster_id;
		this.parentArgId = parent_arg_id;
		this.point = new Text(point);
		this.createDate = new Date();
		this.updateDate = createDate;
		
		InitCheckOutKeys(this);
	}
	
	private static void InitCheckOutKeys(Point point) {
		point.checkOutKeys = new ArrayList<String>();
		point.checkOutKeys.add(String.format("%s(%s)", 
				Point.class.getName(), point.getPosterId().toString()));
		point.checkOutKeys.add(String.format("%s(%s)", 
				Point.class.getName(), point.getParentId().toString()));
		point.checkOutKeys.add(String.format("%s(%s_%s)", 
				Point.class.getName(), point.getParentId().toString(), point.getPosterId().toString()));
	}
	
	public Key getPosterId() {
		return posterId;
	}
	
	public Key getParentId() {
		return parentArgId;
	}
	
	public String getPoint() {
		return point.getValue();
	}

	@Override
	public Key getKey() {
		return pointId;
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			InitCheckOutKeys(this);
		}
		
		return this.checkOutKeys;
	}

	@Override
	public Text getRawPoint() {
		return this.point;
	}

	@Override
	public Date getUpdateDate() {
		return this.updateDate;
	}
	
	@Override
	public void update(Point updateValue) {
		this.point = updateValue.getRawPoint();
		this.updateDate = updateValue.getUpdateDate();
		this.checkOutKeys = updateValue.getCheckOutKeys();
	}

	@Override
	public Date getCreatedDate() {
		return this.createDate;
	}
}