package middlelayer;

import datalayer.DbChat;
import servicelayer.receiving.telegramobjects.TgmMessage;
import servicelayer.receiving.telegramobjects.TgmUpdate;

public class DialogHandler {

	DataAccessObject myDAO = new DataAccessObject();

	public MiddlelayerHttpAnswerForTelegram processUpdateAndReturnAppropriateAnswer(TgmUpdate update) {

		TgmMessage message = update.getMessage();

		MiddlelayerHttpAnswerForTelegram messageToReturnToInspector;

		DbUser dbUserWhoSentMessage;
		int idOfUserWhoSentMessage = message.getFrom().getId();
		try {
			dbUserWhoSentMessage = myDAO.getDbUserById(idOfUserWhoSentMessage);
		} catch (EntityNotFoundException e) {
			messageToReturnToInspector = new MiddlelayerHttpAnswerForTelegram();
			messageToReturnToInspector.setText(
					"Ich konnte dich nicht im System finden, da ist wohl etwas schief gelaufen. Hast du schon mit /start alles eingerichtet? Sonst frag mal meinen Entwickler :)");
			messageToReturnToInspector.setChatId(idOfUserWhoSentMessage);
			return messageToReturnToInspector;
		}
		
		DbChat dbChatWhereCommandWasGiven;
		int idOfChatWhereCommandWasGiven = message.getChat().getId();
		try {
			dbChatWhereCommandWasGiven = myDAO.getChatById(idOfChatWhereCommandWasGiven);
		} catch (EntityNotFoundException e) {
			messageToReturnToInspector = new MiddlelayerHttpAnswerForTelegram();
			messageToReturnToInspector.setText(
					"Ich konnte deinen Chat nicht im System finden, da ist wohl etwas schief gelaufen. Hast du schon mit /start alles eingerichtet? Sonst frag mal meinen Entwickler :)");
			messageToReturnToInspector.setChatId(idOfChatWhereCommandWasGiven);
			return messageToReturnToInspector;
		}
		
		
		if (chatIsAlreadyInDialog(message.getChat().getId())) {
			messageToReturnToInspector = getNextMessageInPresentDialog(message);
		} else {
			messageToReturnToInspector = getFirstMessageInNewDialog(message, dbUserWhoSentMessage, dbChatWhereCommandWasGiven);
		}

		return messageToReturnToInspector;
	}

	private MiddlelayerHttpAnswerForTelegram getFirstMessageInNewDialog(TgmMessage message, DbUser userWhoAsked, DbChat chatWhereCommandWasGiven) {

		switch (message.getText()) {
		case "/start":
			return doStartRoutine(userWhoAsked, chatWhereCommandWasGiven);
		case "/mytask":
			return getNextTask(userWhoAsked, chatWhereCommandWasGiven);
		default:
			return answerWithUsersOwnMessage(message);
		}
	}

	private MiddlelayerHttpAnswerForTelegram getNextMessageInPresentDialog(TgmMessage incomingMessage) {
		int senderId = incomingMessage.getFrom().getId();

		try {
			DbUser currentUser = myDAO.getDbUserById(senderId);

			return new MiddlelayerHttpAnswerForTelegram(); // hier noch eine Logik einbauen zum Abrufen der nächsten
															// Nachricht im Dialog in dem spezifischen Chat

		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	private boolean chatIsAlreadyInDialog(int chatId) {
		DbChat chatToCheck;
		try {
			chatToCheck = myDAO.getChatById(chatId);
		} catch (EntityNotFoundException e) {
			System.out.println("Chat not found in Database");
			return false;
		}
		return chatToCheck.getIsInDialog();
	}

	private MiddlelayerHttpAnswerForTelegram getNextTask(DbUser userWhoAsked, DbChat chatWhereCommandWasGiven) {
		MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

		messageForDialogHandler.setChatId(chatWhereCommandWasGiven.getId());

		String nextTask = userWhoAsked.getNextTask();

		if (nextTask == null) {
			messageForDialogHandler.setText("Du hast nichts zu tun :)");
		} else {
			messageForDialogHandler.setText(nextTask);
		}
		return messageForDialogHandler;
	}

	private MiddlelayerHttpAnswerForTelegram doStartRoutine(DbUser userWhoAsked, DbChat chatWhereCommandWasGiven) {

		MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

		messageForDialogHandler.setChatId(chatWhereCommandWasGiven.getId());
		messageForDialogHandler.setText("Hallo! Welche Räume müsst ihr in eurer WG putzen?");

		return messageForDialogHandler;
	}

	private MiddlelayerHttpAnswerForTelegram answerWithUsersOwnMessage(TgmMessage message) {
		String name = message.getFrom().getFirst_name();
		String text = message.getText();
		int id = message.getChat().getId();

		MiddlelayerHttpAnswerForTelegram returnMessage = new MiddlelayerHttpAnswerForTelegram();

		returnMessage.setChatId(id);
		returnMessage.setText("Hi " + name + "! Du schriebst: " + text);

		return returnMessage;
	}
}
