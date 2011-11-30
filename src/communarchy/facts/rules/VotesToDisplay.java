package communarchy.facts.rules;

public class VotesToDisplay {
	private VotesToDisplay() {}
	
	private static VotesToDisplay INSTANCE;
	
	public static synchronized VotesToDisplay get() {
		if(INSTANCE == null) {
			INSTANCE = new VotesToDisplay();
		}
		
		return INSTANCE;
	}
}