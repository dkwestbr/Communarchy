package communarchy.facts.mappers.interfaces;

import java.util.Iterator;
import java.util.List;

import javax.jdo.Query;

import communarchy.facts.PMSession;

public abstract class AbstractMapper<T> {
	
	protected PMSession pmSession;
	
	public void init(PMSession pmSession) {
		this.pmSession = pmSession;
	}	
	
	@SuppressWarnings("unchecked")
	protected Iterator<T> asIterator(Query q) {
		return ((List<T>) q.execute()).iterator();
	}
}