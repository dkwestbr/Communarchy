package communarchy.facts.interfaces;

import java.io.Serializable;

public interface ICounter extends Serializable {
	public int getCount();
	public void increment();
	public void decrement();
}
