package communarchy.facts.counters;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;

import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IStance;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class StanceCounter extends AbstractCounter<UserStance> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Persistent
	private Key point;
	
	@Persistent
	private Integer stance;
	
	public StanceCounter(IStance resource, Integer shardNum) {
		super(shardNum);
		init(resource.getPoint(), resource.getStance());
	}
	
	public StanceCounter(Key pointId, Integer stance, Integer shardNum) {
		super(shardNum);
		init(pointId, stance);
	}
	
	private void init(Key pointId, Integer stance) {
		this.point = pointId;
		this.stance = stance;
	}
	
	public Key getPointKey() {
		return this.point;
	}
	
	public Integer getStance() {
		return this.stance;
	}

	@Override
	public Key getKey() {
		return resourceId;
	}

	@Override
	public String getNewObjectKey() {
		// TODO Auto-generated method stub
		return null;
	}
}