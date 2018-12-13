package datalayer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter

public class DbUser {
	private int id;
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private ArrayList<String> roomsToDo;
	private String firstname;
	private int householdId;
	
	/**
	 * 
	 * @return a copied List of all rooms the user needs to clean
	 */
	public List<String> getRoomsToDo(){
		// TODO return not the list itself, but a copy
		return roomsToDo;
	}
	
	public void addToDoRoom(String room) {
		roomsToDo.add(room);
	}
	
	public void removeRoomToDo(String room) {
		for(Iterator<String> iterator = roomsToDo.iterator(); iterator.hasNext(); ) {
			String roomInList = iterator.next();
			if(roomInList.equals(room)) {
				iterator.remove();
			}
		}
	}
}
