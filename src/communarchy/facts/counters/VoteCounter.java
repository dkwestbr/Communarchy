package communarchy.facts.counters;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class VoteCounter extends AbstractCounter<VoteCounter> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Key povKey;
	
	@NotPersistent
	private List<String> checkOutKeys;
	
	public VoteCounter(Key povKey, Integer shardNum) {
		super(shardNum);
		this.povKey = povKey;
	}
	
	public Key getPovId() {
		return povKey;
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			this.checkOutKeys = new ArrayList<String>();
			this.checkOutKeys.add(String.format("%s(%s)", VoteCounter.class.getName(), povKey.toString()));
			this.checkOutKeys.add(String.format("%s(%s_%d)", VoteCounter.class.getName(), povKey.toString(), shardNum));
		}
		
		return this.checkOutKeys;
	}

	@Override
	public void update(VoteCounter updateValue) {
		this.count = updateValue.getCount();
	}
}