package communarchy.facts.counters;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.ICounter;
import communarchy.facts.interfaces.IEntity;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractCounter<T extends IEntity<T>, U> implements ICounter, IEntity<U> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Key resourceId;
	
	@Persistent
	protected Integer count;
	
	@Persistent
	protected Integer shardNum;
	
	public AbstractCounter() {}
	
	protected AbstractCounter(int shardNum) {
		this.count = 0;
		this.shardNum = shardNum;
	}
	
	public Integer getShardNum() {
		return shardNum;
	}
	
	public int getCount() {
		return count;
	}
	
	public void increment() {
		++count;
	}
	
	public void decrement() {
		--count;
	}
	
	@Override
	public Key getKey() {
		return resourceId;
	}
}