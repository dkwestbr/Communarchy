package communarchy.facts.counters;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class VoteCounter extends AbstractCounter {
	
	@PrimaryKey
	private Key resourceId;
	
	private Key key;
	
	public VoteCounter(Key povKey, Integer shardNum) {
		super(shardNum, 20, VoteCounter.class);
		this.key = resourceId;
	}
	
	public Key getPovId() {
		return key;
	}
}