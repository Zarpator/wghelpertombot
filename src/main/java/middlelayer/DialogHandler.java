package middlelayer;

import datalayer.DbChat;
import servicelayer.receiving.telegramobjects.TgmMessage;
import servicelayer.receiving.telegramobjects.TgmUpdate;

public class DialogHandler {

	DataAccessObject myDAO = new DataAccessObject();

	public MiddlelayerHttpAnswerForTelegram processUpdateAndReturnAppropriateAnswer(TgmUpdate update) {

		TgmMessage message = update.getMessage();

		MiddlelayerHttpAnswerForTelegram messageToReturnToInspector;

		if (chatIsAlreadyInDialog(message.getChat().getId())) {
			messageToReturnToInspector = getNextMessageInPresentDialog(message);
		} else {
			messageToReturnToInspector = getFirstMessageInNewDialog(message);
		}

		return messageToReturnToInspector;
	}

	private MiddlelayerHttpAnswerForTelegram getFirstMessageInNewDialog(TgmMessage message) {

		switch (message.getText()) {
		case "/start":
			return doStartRoutine(message);
		case "/mytask":
			return getNextTask(message);
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

	private MiddlelayerHttpAnswerForTelegram getNextTask(TgmMessage incommingMessage) {
		MiddlelayerHttpAnswerForTelegram returnMessage = new MiddlelayerHttpAnswerForTelegram();

		int userId = incommingMessage.getFrom().getId();

		DbUser userRepresentationInDb;
		try {
			userRepresentationInDb = myDAO.getDbUserById(userId);
		} catch (EntityNotFoundException e) {
			returnMessage.setChatId(userId);
			returnMessage.setText(
					"Ich konnte dich nicht im System finden, da ist wohl etwas schief gelaufen. Hast du schon mit /start alles eingerichtet? Sonst frag mal meinen Entwickler :)");
			return returnMessage;
		}

		int chatIdFromSender = incommingMessage.getChat().getId();
		returnMessage.setChatId(chatIdFromSender);

		String nextTask = userRepresentationInDb.getNextTask();
		returnMessage.setText(nextTask);

		return returnMessage;
	}

	private MiddlelayerHttpAnswerForTelegram doStartRoutine(TgmMessage message) {

		MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

		messageForDialogHandler.setChatId(message.getChat().getId());
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
