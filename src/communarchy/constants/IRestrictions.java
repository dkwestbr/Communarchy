package communarchy.constants;

import java.util.Arrays;
import java.util.List;

import communarchy.facts.interfaces.IUserStance;

public interface IRestrictions {
	public static int MAX_VOTE_COUNT = 3;
	public static List<Integer> POSSIBLE_STANCES = Arrays.asList(IUserStance.STANCE_AGREE, IUserStance.STANCE_NEUTRAL, IUserStance.STANCE_DISAGREE);
}
