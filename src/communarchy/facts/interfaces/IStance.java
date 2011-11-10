package communarchy.facts.interfaces;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface IStance extends Serializable {
	public Key getPoint();
	public Integer getStance();
	
	public static final String STANCE_PPL_DISAGREE_FORMAT = "%d disagree";
	public static final String STANCE_PPL_NEUTRAL_FORMAT = "%d are in the middle";
	public static final String STANCE_PPL_AGREE_FORMAT = "%d agree";
	public static final String STANCE_DISAGREE_STRING = "Disagree";
	public static final String STANCE_NEUTRAL_STRING = "In the middle";
	public static final String STANCE_AGREE_STRING = "Agree";
	public static final String STANCE_AGREE_URL = "agree";
	public static final String STANCE_NEUTRAL_URL = "neutral";
	public static final String STANCE_DISAGREE_URL = "disagree";
	public static final String POV_STRING_AGREE = "agreePovs";
	public static final String POV_STRING_NEUTRAL = "neutralPovs";
	public static final String POV_STRING_DISAGREE = "disagreePovs";
	
	public static final int STANCE_AGREE = 1;
	public static final int STANCE_NEUTRAL = 0;
	public static final int STANCE_DISAGREE = -1;
	
	public static final int[] STANCE_ARRAY = {-1, 0, 1};
}
