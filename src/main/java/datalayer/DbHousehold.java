package datalayer;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DbHousehold {
	private int id;
	@Setter(AccessLevel.NONE)
	private ArrayList<String> rooms = new ArrayList<>();
	private int cleaningPeriod;
	private Day lastDayOfPeriod;
}
