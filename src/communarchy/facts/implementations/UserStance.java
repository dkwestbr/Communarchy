package communarchy.facts.implementations;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IUserStance;

@PersistenceCapable
public class UserStance implements IUserStance, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String STANCE_PPL_DISAGREE_FORMAT = "%d disagree";
	public static String STANCE_PPL_NEUTRAL_FORMAT = "%d are in the middle";
	public static String STANCE_PPL_AGREE_FORMAT = "%d agree";
	public static String STANCE_DISAGREE_STRING = "Disagree";
	public static String STANCE_NEUTRAL_STRING = "In the middle";
	public static String STANCE_AGREE_STRING = "Agree";
	public static String STANCE_AGREE_URL = "agree";
	public static String STANCE_NEUTRAL_URL = "neutral";
	public static String STANCE_DISAGREE_URL = "disagree";
	public static String POV_STRING_AGREE = "agreePovs";
	public static String POV_STRING_NEUTRAL = "neutralPovs";
	public static String POV_STRING_DISAGREE = "disagreePovs";
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Key userId;
	
	@Persistent
	protected Key pointId;
	
	@Persistent
	protected int stance;
	
	@Persistent
	private int switchCount = 0;
	
	public UserStance(Key pointId, Key userId, Integer switchCount) {
		this(pointId, userId, 1, switchCount);
	}
	
	public UserStance(Key pointId, Key userId, Integer stance, Integer switchCount) {
		this.pointId = pointId;
		this.stance = stance;
		this.userId = userId;
		this.switchCount = switchCount;
	}

	@Override
	public int getStance() {
		return stance;
	}

	@Override
	public Key getUserId() {
		return userId;
	}

	@Override
	public Key getPointId() {
		return pointId;
	}
	
	@Override
	public Key getKey() {
		return key;
	}
	
	@Override
	public int getSwitchCount() {
		return switchCount;
	}
	
	@Override
	public void incrimentSwitchCount() {
		++switchCount;
	}

	@Override
	public void setStance(int newStance) {
		stance = newStance;
	}

	@Override
	public String getStanceAsString() {
		if(stance == STANCE_AGREE) {
			return STANCE_AGREE_STRING;
		} else if (stance == STANCE_NEUTRAL) {
			return STANCE_NEUTRAL_STRING;
		} else {
			return STANCE_DISAGREE_STRING;
		}
	}

	@Override
	public String getStanceUrlPath() {
		if(stance == STANCE_AGREE) {
			return STANCE_AGREE_URL;
		} else if (stance == STANCE_NEUTRAL) {
			return STANCE_NEUTRAL_URL;
		} else {
			return STANCE_DISAGREE_URL;
		}
	}

	public static String getStanceAsString(int id) {
		if(id == STANCE_AGREE) {
			return STANCE_AGREE_STRING;
		} else if (id == STANCE_NEUTRAL) {
			return STANCE_NEUTRAL_STRING;
		} else {
			return STANCE_DISAGREE_STRING;
		}
	}

	public static String getStanceUrlPath(int id) {
		if(id == STANCE_AGREE) {
			return STANCE_AGREE_URL;
		} else if (id == STANCE_NEUTRAL) {
			return STANCE_NEUTRAL_URL;
		} else {
			return STANCE_DISAGREE_URL;
		}
	}

	public static String getPovStringByStance(int id) {
		if(id == STANCE_AGREE) {
			return POV_STRING_AGREE;
		} else if (id == STANCE_NEUTRAL) {
			return POV_STRING_NEUTRAL;
		} else {
			return POV_STRING_DISAGREE;
		}
	}
}
