package communarchy.facts.counters;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IStance;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class StanceCounter extends AbstractCounter<StanceCounter> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Persistent
	private Key point;
	
	@Persistent
	private Integer stance;
	
	@NotPersistent
	private List<String> checkOutKeys;
	
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
		
		InitCheckOutKeys(this);
	}
	
	private static void InitCheckOutKeys(StanceCounter counter) {
		counter.checkOutKeys = new ArrayList<String>();
		counter.checkOutKeys.add(String.format("%s(%s)", StanceCounter.class.getName(), counter.point.toString()));
		counter.checkOutKeys.add(String.format("%s(%s_%d)", StanceCounter.class.getName(), counter.point.toString(), counter.stance));
		counter.checkOutKeys.add(String.format("%s(%s_%d_%d)", StanceCounter.class.getName(), counter.point.toString(), counter.stance, counter.shardNum));
	}
	
	public Key getPointKey() {
		return this.point;
	}
	
	public Integer getStance() {
		return this.stance;
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			InitCheckOutKeys(this);
		}
		
		return this.checkOutKeys;
	}

	@Override
	public void update(StanceCounter updateValue) {
		this.shardNum = updateValue.getShardNum();
		this.stance = updateValue.getStance();
		this.point = updateValue.getPointKey();
	}
}