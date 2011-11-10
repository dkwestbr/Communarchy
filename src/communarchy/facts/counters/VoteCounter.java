package communarchy.facts.counters;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;
import communarchy.facts.actions.Vote;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class VoteCounter extends AbstractCounter<Vote> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Key povKey;
	
	public VoteCounter(Key povKey, Integer shardNum) {
		super(shardNum);
		this.povKey = povKey;
	}
	
	public Key getPovId() {
		return povKey;
	}
}