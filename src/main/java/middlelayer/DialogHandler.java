package middlelayer;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.dialogs.AbstractDialog;
import middlelayer.dialogs.AnswerwithusersownmessageDialog;
import middlelayer.dialogs.MytaskDialog;
import middlelayer.dialogs.StartDialog;
import servicelayer.receiving.telegramobjects.TgmMessage;
import servicelayer.receiving.telegramobjects.TgmUpdate;

public class DialogHandler {

	DataAccessObject myDAO = new DataAccessObject();

	public MiddlelayerHttpAnswerForTelegram processUpdateByGettingDbEntitiesAndDelegating(TgmUpdate update) {

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

		// neu (dialog unabhängig von Chatstatus durchführen, User hat ja Eigenschaft
		// dialogType und dialogNum)

		messageToReturnToInspector = getDialogMessageByUsingSuitingDialog(message, dbUserWhoSentMessage,
				dbChatWhereCommandWasGiven);

		// alt
		/*
		 * if (chatIsAlreadyInDialog(message.getChat().getId())) {
		 * messageToReturnToInspector = getNextMessageInPresentDialog(message,
		 * dbUserWhoSentMessage, dbChatWhereCommandWasGiven); } else {
		 * messageToReturnToInspector = getFirstMessageInNewDialog(message,
		 * dbUserWhoSentMessage, dbChatWhereCommandWasGiven); }
		 */

		return messageToReturnToInspector;
	}

	private MiddlelayerHttpAnswerForTelegram getDialogMessageByUsingSuitingDialog(TgmMessage message,
			DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven) {

		MiddlelayerHttpAnswerForTelegram returnAnswer;

		String currentDialogOfChat = dbChatWhereCommandWasGiven.getCurrentOngoingDialog();

		if (dbChatWhereCommandWasGiven.isInDialog()) {
			currentDialogOfChat = dbChatWhereCommandWasGiven.getCurrentOngoingDialog();
		} else {
			currentDialogOfChat = message.getText();
		}

		AbstractDialog dialogToDo;
		switch (currentDialogOfChat) {
		case "/start":
			dialogToDo = new StartDialog();
			break;
		case "/mytask":
			dialogToDo = new MytaskDialog();
			break;
		default:
			System.out.println("no known command in currentOngoingDialog of the Chat or in Message of the User found");
			dialogToDo = new AnswerwithusersownmessageDialog();
			break;
		}

		returnAnswer = getMessageByGettingSuitingMessageInDialogsMessageList(dialogToDo, dbUserWhoSentMessage,
				dbChatWhereCommandWasGiven, message);

		return returnAnswer;
	}

	private MiddlelayerHttpAnswerForTelegram getMessageByGettingSuitingMessageInDialogsMessageList(
			AbstractDialog chosenDialog, DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven,
			TgmMessage message) {

		MiddlelayerHttpAnswerForTelegram returnMessage;

		returnMessage = chosenDialog.doLogicDependentOnCurrentStateInChatAndGetAnswer(dbUserWhoSentMessage,
				dbChatWhereCommandWasGiven, message);

		return returnMessage;
	}
}
