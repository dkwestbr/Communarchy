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

import communarchy.facts.interfaces.IPointOfView;

@PersistenceCapable
public class PointOfView implements IPointOfView<PointOfView>, Serializable {
	
	private static transient final long serialVersionUID = 1L;

	public static final String P_POV_ID = "povId";
	public static final String P_POINT_ID = "parentPointId";
	public static final String P_POSTER_ID = "posterId";
	public static final String P_STANCE = "stance";
	
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
	
	@NotPersistent
	private List<String> checkOutKeys;
	
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
		
		InitCheckOutKeys(this);
	}
	
	private static void InitCheckOutKeys(PointOfView pov) {
		pov.checkOutKeys = new ArrayList<String>();
		pov.checkOutKeys.add(String.format("%s(%s)", PointOfView.class.getName(),
				pov.getPosterId().toString()));
		pov.checkOutKeys.add(String.format("%s(%s)",
				PointOfView.class.getName(), pov.getParentPointId().toString()));
		pov.checkOutKeys.add(String.format("%s(%s_%s)",
				PointOfView.class.getName(), pov.getParentPointId().toString(), pov.getPosterId().toString()));
		pov.checkOutKeys.add(String.format("%s(%s_%d)",
				PointOfView.class.getName(), pov.getParentPointId().toString(), pov.getStance()));
		pov.checkOutKeys.add(String.format("%s(%s_%s_%d)",
				PointOfView.class.getName(), pov.getPosterId().toString(), pov.getParentPointId().toString(), pov.getStance()));
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

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			InitCheckOutKeys(this);
		}
		
		return this.checkOutKeys;
	}


	@Override
	public Text getRawPov() {
		return this.pov;
	}

	@Override
	public Date getUpdateDate() {
		return this.updateDate;
	}
	
	@Override
	public void update(PointOfView updateValue) {
		this.updateDate = updateValue.getUpdateDate();
		this.pov = updateValue.getRawPov();
		this.checkOutKeys = updateValue.getCheckOutKeys();
	}

	@Override
	public Date getCreatedDate() {
		return this.createDate;
	}
}