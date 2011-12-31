package communarchy.facts.counters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import communarchy.facts.PMSession;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetRepCount;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RepCounter extends AbstractCounter<RepCounter> {

	private static final Logger log =
		      Logger.getLogger(RepCounter.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Key userKey;
	
	@NotPersistent
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
	
	@Override
	public void decrement(Integer amount, PMSession pmSession) {
		Integer currentCount = 0;
		try {
			currentCount = pmSession.getMapper(CountMapper.class).getCount(new GetRepCount(userKey));
		} catch (CommunarchyPersistenceException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		
		if(amount != null) {
			if(currentCount < amount) {
				count -= currentCount;
			} else {
				count -= amount;
			}
		} else {
			--count;
		}
	}
}