package communarchy.facts.actions;

import communarchy.facts.actions.interfaces.IFlag;
import communarchy.facts.interfaces.IPointOfView;

public class Flag implements IFlag {

	private int povId = -1;
	private int userId = -1;
	private int reasonId = -1;
	
	public Flag (int povId, int userId, int reasonId) {
		this.povId = povId;
		this.userId = userId;
		this.reasonId = reasonId;
	}
	
	@Override
	public int getPovId() {
		return povId;
	}

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public int getReasonId() {
		return reasonId;
	}

	@Override
	public String getFlagReason() {
		if(reasonId == IPointOfView.FLAG_PROFANITY) {
			return "This statement has been flagged for profanity";
		} else if(reasonId == IPointOfView.FLAG_OFFENSIVE) {
			return "This statement has been flagged as offensive";
		} else {
			return "";
		}
	}
	
	@Override
	public String getFlagReasonUrlPath() {
		if(reasonId == IPointOfView.FLAG_PROFANITY) {
			return "profanity";
		} else if(reasonId == IPointOfView.FLAG_OFFENSIVE) {
			return "offensive";
		} else {
			return "";
		}
	}

}
