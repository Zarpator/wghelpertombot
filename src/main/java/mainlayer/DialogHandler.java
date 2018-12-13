package mainlayer;

import datalayer.DataAccessObject;
import datalayer.DbChat;
import datalayer.DbUser;
import mainlayer.dialogs.AbstractFullDialog;
import mainlayer.dialogs.AnswerwithusersownmessageDialog;
import mainlayer.dialogs.MyTaskDialog;
import mainlayer.dialogs.StartDialog;
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
		messageToReturnToInspector = getDialogMessageByUsingSuitingDialog(message, dbUserWhoSentMessage,
				dbChatWhereCommandWasGiven);
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

		AbstractFullDialog dialogToDo;
		switch (currentDialogOfChat) {
		case "/start":
			dialogToDo = new StartDialog(message, myDAO, dbChatWhereCommandWasGiven, dbUserWhoSentMessage);
			dbChatWhereCommandWasGiven.setCurrentOngoingDialog("/start");
			break;
		case "/mytask":
			dialogToDo = new MyTaskDialog(message, myDAO, dbChatWhereCommandWasGiven, dbUserWhoSentMessage);
			dbChatWhereCommandWasGiven.setCurrentOngoingDialog("/mytask");
			break;
		default:
			System.out.println("no known command in currentOngoingDialog of the Chat or in Message of the User found");
			dialogToDo = new AnswerwithusersownmessageDialog(message, myDAO, dbChatWhereCommandWasGiven, dbUserWhoSentMessage);
			dbChatWhereCommandWasGiven.setCurrentOngoingDialog("/getanswerwithusersownmessage");
			break;
		}

		returnAnswer = getMessageByGettingSuitingMessageInDialogsMessageList(dialogToDo, dbUserWhoSentMessage,
				dbChatWhereCommandWasGiven, message);

		return returnAnswer;
	}

	private MiddlelayerHttpAnswerForTelegram getMessageByGettingSuitingMessageInDialogsMessageList(
			AbstractFullDialog chosenDialog, DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven,
			TgmMessage message) {

		MiddlelayerHttpAnswerForTelegram returnMessage;

		returnMessage = chosenDialog.doLogicDependentOnCurrentStateInChatAndGetAnswer();

		return returnMessage;
	}
}
