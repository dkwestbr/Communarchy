package communarchy.facts.mappers.interfaces;

import communarchy.exceptions.CommunarchyPersistenceException;
import communarchy.facts.counters.AbstractCounter;

public interface IShardMapper {

	<T extends AbstractCounter, U> void decrement(U resourceId, Class<T> counter) throws CommunarchyPersistenceException;
	<T extends AbstractCounter, U> int getCount(U resourceId, Class<T> type);
	<T extends AbstractCounter, U> void increment(U resourceId, Class<T> counter);
}