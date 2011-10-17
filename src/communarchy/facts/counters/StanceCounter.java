package communarchy.facts.counters;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StanceCounter extends AbstractCounter {
	
	@PrimaryKey
	private String resourceId;
	
	@Persistent
	private Key point;
	
	@Persistent
	private Integer stance;
	
	public StanceCounter(Key pointKey, Integer stance, Integer shardNum) {
		super(shardNum, 20, StanceCounter.class);
		this.resourceId = String.format("%s_%d", pointKey.toString(), stance);
		this.point = pointKey;
		this.stance = stance;
	}
	
	public String getKey() {
		return this.resourceId;
	}
	
	public Key getPointKey() {
		return this.point;
	}
	
	public Integer getStance() {
		return this.stance;
	}
	
	public static String buildKey(Key point, Integer stance) {
		return String.format("%s_%d", point.toString(), stance);
	}
}