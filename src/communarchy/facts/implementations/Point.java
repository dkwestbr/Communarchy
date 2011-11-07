package communarchy.facts.implementations;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IPoint;

@PersistenceCapable
public class Point implements IPoint, Serializable {
	private static transient final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key pointId;
	
	@Persistent
	private Key posterId;
	
	@Persistent
	private Key parentArgId;
	
	@Persistent
	private String point;
	
	@Persistent
	private Date createDate;
	
	@Persistent
	private Date updateDate;
	
	public Point(){}
	
	public Point(Key parent_arg_id, Key poster_id, String point) {
		if(parent_arg_id == null || poster_id == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.posterId = poster_id;
		this.parentArgId = parent_arg_id;
		this.point = point;
		this.createDate = new Date();
		this.updateDate = createDate;
	}
	
	public Key getPosterId() {
		return posterId;
	}
	
	public Key getParentId() {
		return parentArgId;
	}
	
	public String getPoint() {
		return point;
	}

	@Override
	public Key getPointId() {
		return pointId;
	}
}