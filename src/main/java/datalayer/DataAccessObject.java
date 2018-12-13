package datalayer;

import java.util.ArrayList;

import mainlayer.EntityNotFoundException;
import servicelayer.receiving.telegramobjects.TgmChat;
import servicelayer.receiving.telegramobjects.TgmUser;

public class DataAccessObject {
	private static ArrayList<DbChat> allChats = new ArrayList<DbChat>();
	private static ArrayList<DbUser> allUsers = new ArrayList<DbUser>();
	public static ArrayList<DbHousehold> allHouseholds = new ArrayList<DbHousehold>();

	public boolean isAlreadyInDialog(int id) {
		return false;
	}

	public DbChat getChatById(int id) throws EntityNotFoundException {
		for (DbChat chat : allChats) {
			if (chat.getId() == id) {
				return chat;
			}
		}
		throw new EntityNotFoundException();
	}

	public DbUser getDbUserById(int id) throws EntityNotFoundException {

		for (DbUser user : allUsers) {
			if (user.getId() == id) {
				return user;
			}
		}

		throw new EntityNotFoundException();
	}

	public DbHousehold getHouseholdById(int id) throws EntityNotFoundException {
		for (DbHousehold household : allHouseholds) {
			if (household.getId() == id) {
				return household;
			}
		}

		throw new EntityNotFoundException();
	}

	public void addNewUser(TgmUser unpersistedTgmUser) {
		DbUser newUserInDb = new DbUser();

		newUserInDb.setId(unpersistedTgmUser.getId());
		newUserInDb.setFirstname(unpersistedTgmUser.getFirst_name());

		allUsers.add(newUserInDb);
	}

	public void addNewChat(TgmChat unpersistedTgmChat) {
		DbChat newChatInDb = new DbChat();

		newChatInDb.setId(unpersistedTgmChat.getId());

		allChats.add(newChatInDb);
	}

	public int addNewHousehold() {
		DbHousehold newHouseholdInDb = new DbHousehold();

		int idOfNewHousehold = getNewHouseholdId();

		newHouseholdInDb.setId(idOfNewHousehold);

		allHouseholds.add(newHouseholdInDb);

		return idOfNewHousehold;
	}

	public boolean addRoomToHousehold(int householdId, String room) {
		try {
			DbHousehold household = this.getHouseholdById(householdId);
			household.getRooms().add(room);
			return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

	private int getNewHouseholdId() {
		if (allHouseholds == null || allHouseholds.isEmpty()) {
			return 1;
		} else {
			return allHouseholds.size() + 1;
		}
	}

	public ArrayList<DbChat> getAllChatsAsArrayList() {
		return allChats;
	}

	public ArrayList<DbUser> getAllUsersAsArrayList() {
		return allUsers;
	}

	public boolean roomIsInHousehold(String room, int householdId) {
		DbHousehold household;
		try {
			household = this.getHouseholdById(householdId);
		} catch (EntityNotFoundException e) {
			return false;
		}
		
		for(String roomInHousehold : household.getRooms()) {
			if(roomInHousehold.equals(room)) {
				return true;
			}
		}
		return false;
	}

}
