package datalayer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter

public class DbChat {
	private int id;
	private String currentOngoingDialog;
	
	public boolean isInDialog() {
		if (getCurrentOngoingDialog() == null) {
			return false;
		} else {
			return true;
		}
	}
}
