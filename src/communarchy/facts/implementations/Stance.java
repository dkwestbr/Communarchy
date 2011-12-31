package communarchy.facts.implementations;

import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IStance;

public class Stance implements IStance {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Persistent
	private Key point;
	
	@Persistent 
	private Integer stance;

	@Override
	public Key getPoint() {
		return point;
	}

	@Override
	public Integer getStance() {
		return stance;
	}
	
	public Stance(Key point, Integer stance) {
		this.point = point;
		this.stance = stance;
	}
	
	@SuppressWarnings("unused")
	private Stance() {}
	
	public static String getPovStringByStance(int id) {
		if(id == STANCE_AGREE) {
			return POV_STRING_AGREE;
		} else if (id == STANCE_NEUTRAL) {
			return POV_STRING_NEUTRAL;
		} else {
			return POV_STRING_DISAGREE;
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

	public static String getStanceAsString(int id) {
		if(id == STANCE_AGREE) {
			return STANCE_AGREE_STRING;
		} else if (id == STANCE_NEUTRAL) {
			return STANCE_NEUTRAL_STRING;
		} else {
			return STANCE_DISAGREE_STRING;
		}
	}
	
	public static int getRepForStance(int id) {
		if(id == STANCE_AGREE) {
			return 3;
		} else if (id == STANCE_NEUTRAL) {
			return 1;
		} else {
			return 0;
		}
	}

	public static int getStanceAsId(String id) {
		if(id.equals(STANCE_AGREE_URL)) {
			return STANCE_AGREE;
		} else if(id.equals(STANCE_NEUTRAL_URL)) {
			return STANCE_NEUTRAL;
		} else {
			return STANCE_DISAGREE;
		}
	}
}
