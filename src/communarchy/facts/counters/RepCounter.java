package communarchy.facts.counters;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RepCounter extends AbstractCounter<RepCounter> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Key userKey;
	
	private List<String> checkOutKeys;
	
	@SuppressWarnings("unused")
	private RepCounter() {}
	
	public RepCounter(Key userKey, int shardNum) {
		super(shardNum);
		this.userKey = userKey;
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			this.checkOutKeys = new ArrayList<String>();
			this.checkOutKeys.add(String.format("%s(%s)", RepCounter.class.getName(), userKey.toString()));
			this.checkOutKeys.add(String.format("%s(%s_%d)", RepCounter.class.getName(), userKey.toString(), shardNum));
		}
		
		return checkOutKeys;
	}

	@Override
	public void update(RepCounter updateValue) {
		this.count = updateValue.getCount();
	}
}