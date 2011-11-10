package communarchy.utils.constants;

import java.util.Arrays;
import java.util.List;

import communarchy.facts.implementations.UserStance;

public interface IRestrictions {
	public static int MAX_VOTE_COUNT = 3;
	public static List<Integer> POSSIBLE_STANCES = Arrays.asList(UserStance.STANCE_AGREE, UserStance.STANCE_NEUTRAL, UserStance.STANCE_DISAGREE);
}
