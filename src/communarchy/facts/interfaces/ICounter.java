package communarchy.facts.interfaces;

import java.io.Serializable;

import communarchy.facts.PMSession;

public interface ICounter extends Serializable {
	public int getCount();
	public void increment(Integer amount, PMSession pmSession);
	public void decrement(Integer amount, PMSession pmSession);
}
