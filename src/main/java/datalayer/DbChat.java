package datalayer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter

public class DbChat {
	private int id;
	private String currentOngoingDialog;
	private int currentStateInOngoingDialog = 0;
	
	public boolean isInDialog() {
		if (getCurrentOngoingDialog() == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void incrementCurrentState(int increment) {
		int newState = this.getCurrentStateInOngoingDialog() + increment;
		this.setCurrentStateInOngoingDialog(newState);
	}
	
	public void resetOngoingDialog() {
		this.setCurrentOngoingDialog(null);
		this.setCurrentStateInOngoingDialog(0);
	}
}
