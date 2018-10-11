package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class StartDialog extends AbstractFullDialog {

	public StartDialog() {
		this.mySpecificDialogStates = new DialogState[] { 
				new RoomAskingDialogState(),
				new CleaningFrequencyDialogState()};
	}

	private class RoomAskingDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic(DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven,
				TgmMessage message) {
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

			messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
			messageForDialogHandler.setText("Hallo! Welche Räume müsst ihr in eurer WG putzen?\n"
					+ "Schreib mir einfach in jeder Nachricht ein Zimmer, bestätige mit \"Fertig\"");

			return messageForDialogHandler;
		}
	}

	private class CleaningFrequencyDialogState extends DialogState {
		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic(DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven,
				TgmMessage message) {
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

			if (!message.getText().equals("Fertig")) {
				stateIncrement = 0;
				return MiddlelayerHttpAnswerForTelegram.noMessage;
			} else {
				messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
				messageForDialogHandler.setText("Wie oft putzt ihr in der Woche?");
				// TODO genauer einschränken welche Antworten möglich sind
				
				dialogFinished = true;

				return messageForDialogHandler;
			}
		}
	}
}
