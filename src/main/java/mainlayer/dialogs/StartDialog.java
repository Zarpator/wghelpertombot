package mainlayer.dialogs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import datalayer.DataAccessObject;
import datalayer.Day;
import datalayer.DbChat;
import datalayer.DbHousehold;
import datalayer.DbUser;
import mainlayer.EntityNotFoundException;
import mainlayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class StartDialog extends AbstractFullDialog {

	public StartDialog(TgmMessage message, DataAccessObject dao, DbChat chat, DbUser user) {
		super(message, dao, chat, user);
		this.mySpecificDialogStates = new DialogState[] { new RoomAskingDialogState(), new YourNextRoomDialogState(),
				new CleaningFrequencyDialogState(), new StartDayDialogState(), new FinishSetupDialogState() };
	}

	private class RoomAskingDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

			messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
			messageForDialogHandler.setText("Hallo! Welche Räume müsst ihr in eurer WG putzen?\n"
					+ "Schreib mir einfach in jeder Nachricht ein Zimmer, bestätige mit \"Fertig\"");

			return messageForDialogHandler;
		}
	}

	private class YourNextRoomDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {

			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();
			String userInput = messageToProcess.getText();

			int householdId;
			if (dbUserWhoSentMessage.getHouseholdId() == 0) {
				householdId = myDAO.addNewHousehold();
				dbUserWhoSentMessage.setHouseholdId(householdId);
			} else {
				householdId = dbUserWhoSentMessage.getHouseholdId();
			}

			if (!userInput.equals("Fertig")) {

				myDAO.addRoomToHousehold(householdId, messageToProcess.getText());

				stateIncrement = 0;
				return MiddlelayerHttpAnswerForTelegram.noMessage;
			} else {

				DbHousehold userHousehold;
				try {
					userHousehold = myDAO.getHouseholdById(householdId);
					List<String> rooms = userHousehold.getRooms();
					if (rooms.isEmpty()) {
						messageForDialogHandler.setText(
								"Deine WG muss schon Räume haben wenn das hier zwischen uns funktionieren soll");
						stateIncrement = 0;
					} else {
						messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
						messageForDialogHandler.setText("Welchen dieser Räume musst du als nächstes putzen?");
					}
				} catch (EntityNotFoundException e) {
					messageForDialogHandler.setText(e.getMessage());
				}
				return messageForDialogHandler;
			}
		}
	}

	private class CleaningFrequencyDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {

			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();
			String userInput = messageToProcess.getText();
			int householdId = dbUserWhoSentMessage.getHouseholdId();
			boolean roomIsInHousehold;

			roomIsInHousehold = myDAO.roomIsInHousehold(userInput, householdId);

			if (roomIsInHousehold) {

				dbUserWhoSentMessage.addToDoRoom(userInput);

				messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
				messageForDialogHandler.setText("Wie lang ist die Frist bei euch, bis ein Raum geputzt werden muss?\n"
						+ "Schreibe mir die Zeit in Tagen");
				return messageForDialogHandler;
			} else {
				stateIncrement = 0;

				messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
				String messageText = "Diesen Raum müsst ihr in eurer WG nicht putzen. Nenne einen Raum, den du putzen musst:\n\n";

				DbHousehold household;
				try {
					household = myDAO.getHouseholdById(householdId);
				} catch (EntityNotFoundException e) {
					messageForDialogHandler.setText("Haushalt nicht gefunden. Interner Fehler");
					return messageForDialogHandler;
				}

				List<String> roomsInHousehold = household.getRooms();

				for (String room : roomsInHousehold) {
					messageText += room + ", ";
				}

				messageText = messageText.substring(0, messageText.length() - 3);

				return messageForDialogHandler;
			}
		}
	}

	private class StartDayDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();
			String userInput = messageToProcess.getText();

			if (!StringUtils.isNumeric(userInput)) {
				messageForDialogHandler
						.setText("Das habe ich nicht verstanden\n" + "Schreibe mir die Anzahl der Tage als Zahl");
				stateIncrement = 0;
				return messageForDialogHandler;
			}

			int cleaningPeriod = Integer.parseInt(userInput);
			DbHousehold householdOfUser;

			try {
				householdOfUser = myDAO.getHouseholdById(dbUserWhoSentMessage.getId());
				householdOfUser.setCleaningPeriod(cleaningPeriod);
			} catch (EntityNotFoundException e) {
				System.out.println("Haushalt nicht gefunden. Interner Fehler");
				return messageForDialogHandler;
			}

			messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
			messageForDialogHandler
					.setText("An welchem Wochentag soll immer geputzt sein?\n\n" + "\"MO, DI, MI, DO, FR, SA, SO\"");

			dialogFinished = true;

			return messageForDialogHandler;
		}
	}

	private class FinishSetupDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();
			messageForDialogHandler.setChatId(dbUserWhoSentMessage.getId());
			String userInput = messageToProcess.getText();

			DbHousehold household;
			try {
				household = myDAO.getHouseholdById(dbUserWhoSentMessage.getId());
			} catch (EntityNotFoundException e) {
				messageForDialogHandler.setText("Haushalt nicht gefunden. Interner Fehler");
				dialogFinished = true;
				return messageForDialogHandler;
			}

			Day lastDayOfPeriod;

			switch (userInput) {
			case "MO":
				lastDayOfPeriod = Day.MO;
				break;
			case "DI":
				lastDayOfPeriod = Day.TU;
				break;
			case "MI":
				lastDayOfPeriod = Day.WE;
				break;
			case "DO":
				lastDayOfPeriod = Day.TU;
				break;
			case "FR":
				lastDayOfPeriod = Day.FR;
				break;
			case "SA":
				lastDayOfPeriod = Day.SA;
				break;
			case "SO":
				lastDayOfPeriod = Day.SU;
				break;
			default:
				messageForDialogHandler.setText("Ich habe deine Eingabe nicht erkannt. Gib noch mal ein.");
				stateIncrement = 0;
				return messageForDialogHandler;
			}

			household.setLastDayOfPeriod(lastDayOfPeriod);

			messageForDialogHandler.setText("Danke! Es ist jetzt alles eingerichtet. Frag mich doch mal was :)");
			return messageForDialogHandler;
		}
	}
}
