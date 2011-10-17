package communarchy.facts.counters;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.Persistent;

import communarchy.facts.interfaces.ICounter;

public abstract class AbstractCounter implements ICounter {
	
	@Persistent
	private Integer count;
	
	@Persistent
	private Integer shardNum;
	
	private static Map<Class<?>, Integer> shardCountRegistry;
	
	@SuppressWarnings("unused")
	private AbstractCounter() {}
	
	protected AbstractCounter(Integer shardNum, int numShards, Class<?> type) {
		this.shardNum = shardNum;
		this.count = 0;
		
		if(AbstractCounter.shardCountRegistry == null) {
			AbstractCounter.shardCountRegistry = new HashMap<Class<?>, Integer>();
		}
		
		AbstractCounter.shardCountRegistry.put(type, numShards);
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
	
	public static int getNumShards(Class<?> type) {
		
		if(shardCountRegistry == null) {
			throw new NullPointerException("Don't forget to register your shard...");
		}
		
		if(shardCountRegistry.get(type) == null) {
			throw new NullPointerException("Don't forget to register your shard number...");
		}
		
		return shardCountRegistry.get(type);
	}
}