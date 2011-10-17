package communarchy.facts.mappers.interfaces;

import communarchy.facts.PMSession;

public abstract class AbstractMapper<T> {
	
	protected PMSession pmSession;
	
	public void init(PMSession pmSession) {
		this.pmSession = pmSession;
	}	
}