package communarchy.facts.implementations;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.interfaces.IUserStance;

@PersistenceCapable
public class UserStance extends Stance implements IUserStance, IEntity {
	
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
	
	public UserStance(Key user, Key point, Integer stance) {
		super(point, stance);
		
		if(user == null || point == null || stance == null) {
			throw new NullPointerException("No params may be null in UserStance constructor");
		}
		
		this.user = user;
		this.point = point;
		this.stance = stance;
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
	}
	
	public static String BuildVoteQueryKey(Key pointId, Key userId) {
		return String.format("UserStance(%s_%s)", pointId.toString(), userId.toString());
	}

	@Override
	public String getNewObjectKey() {
		return null;
	}
}