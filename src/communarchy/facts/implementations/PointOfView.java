package communarchy.facts.implementations;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import communarchy.facts.interfaces.IPointOfView;

@PersistenceCapable
public class PointOfView implements IPointOfView, Serializable {
	
	private static transient final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key povId;
	
	@Persistent
	private Key posterId;
	
	@Persistent
	private Key parentPointId;
	
	@Persistent
	private int stance;
	
	@Persistent
	private Text pov;
	
	@Persistent
	private Date createDate;
	
	@Persistent
	private Date updateDate;
	
	public PointOfView(){}
	
	public PointOfView(Key parent_point_id, Key poster_id, String pov, Integer stance) {
		if(parent_point_id == null || poster_id == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.posterId = poster_id;
		this.parentPointId = parent_point_id;
		this.pov = new Text(pov);
		this.stance = stance;
		this.createDate = new Date();
		this.updateDate = createDate;
	}
	
	public Key getParentPointId() {
		return parentPointId;
	}
	
	public int getStance() {
		return stance;
	}
	
	public String getPov() {
		return pov.getValue();
	}

	@Override
	public Key getPosterId() {
		return posterId;
	}

	@Override
	public Key getKey() {
		return povId;
	}
}