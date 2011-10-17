package communarchy.facts.interfaces;

import com.google.appengine.api.datastore.Key;

public interface IUserStance {
	public static int STANCE_AGREE = 1;
	public static int STANCE_NEUTRAL = 0;
	public static int STANCE_DISAGREE = -1;
	public int getStance();
	public Key getPointId();
	public String getStanceAsString();
	public String getStanceUrlPath();
	
	public Key getUserId();
	public Key getKey();
	public int getSwitchCount();
	public void setStance(int newStance);
	public void incrimentSwitchCount();
}
