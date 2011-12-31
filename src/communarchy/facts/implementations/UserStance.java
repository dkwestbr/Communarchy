package communarchy.facts.implementations;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.interfaces.IUserStance;

@PersistenceCapable
public class UserStance extends Stance implements IUserStance, IEntity<UserStance> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private Key user;
	
	@Persistent
	private Key point;
	
	@Persistent
	private Integer stance;
	
	@NotPersistent
	private List<String> checkOutKeys;
	
	public UserStance(Key user, Key point, Integer stance) {
		super(point, stance);
		
		if(user == null || point == null || stance == null) {
			throw new NullPointerException("No params may be null in UserStance constructor");
		}
		
		this.user = user;
		this.point = point;
		this.stance = stance;
		
		InitCheckOutKeys(this);
	}
	
	private static void InitCheckOutKeys(UserStance stance) {
		stance.checkOutKeys = new ArrayList<String>();
		stance.checkOutKeys.add(String.format("%s(%s)", 
				UserStance.class.getName(), stance.getUser().toString()));
		stance.checkOutKeys.add(String.format("%s(%s)", 
				UserStance.class.getName(), stance.getPoint().toString()));
		stance.checkOutKeys.add(String.format("%s(%s_%s)", 
				UserStance.class.getName(), stance.getUser().toString(), stance.getPoint().toString()));
		stance.checkOutKeys.add(String.format("%s(%s_%d)", 
				UserStance.class.getName(), stance.getPoint().toString(), stance.getStance()));
	}
	
	@Override
	public Key getUser() {
		return user;
	}

	@Override
	public Key getPoint() {
		return point;
	}

	@Override
	public Key getKey() {
		return key;
	}
	
	@Override
	public Integer getStance() {
		return stance;
	}
		
	private UserStance() {
		super(null, null);
	}

	@Override
	public void setStance(Integer newStance) {
		this.stance = newStance;
		InitCheckOutKeys(this);
	}
	
	public static String BuildVoteQueryKey(Key pointId, Key userId) {
		return String.format("UserStance(%s_%s)", pointId.toString(), userId.toString());
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			InitCheckOutKeys(this);
		}
		
		return this.checkOutKeys;
	}

	@Override
	public void update(UserStance updateValue) {
		this.stance = updateValue.getStance();
		this.point = updateValue.getPoint();
		this.user = updateValue.getUser();
		this.checkOutKeys = updateValue.getCheckOutKeys();
	}
}