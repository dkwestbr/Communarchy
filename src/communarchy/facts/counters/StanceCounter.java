package communarchy.facts.counters;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;

import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IStance;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class StanceCounter extends AbstractCounter<UserStance> {
		
	@Persistent
	private Key point;
	
	@Persistent
	private Integer stance;
	
	public StanceCounter(IStance resource, Integer shardNum) {
		super(shardNum);
		this.point = resource.getPoint();
		this.stance = resource.getStance();
	}
	
	public Key getPointKey() {
		return this.point;
	}
	
	public Integer getStance() {
		return this.stance;
	}
}