package mainlayer;

import java.util.ArrayList;

import datalayer.DataAccessObject;
import datalayer.DbChat;
import datalayer.DbUser;
import servicelayer.receiving.TgmAnswerWithUpdateArray;
import servicelayer.receiving.telegramobjects.TgmChat;
import servicelayer.receiving.telegramobjects.TgmUpdate;
import servicelayer.receiving.telegramobjects.TgmUser;
import servicelayer.sending.HttpMessageForTelegramServers;
import servicelayer.sending.PresetMessageForSendMessage;

public class Inspector {

	DialogHandler myDH = new DialogHandler();
	DataAccessObject myDAO = new DataAccessObject();
	
	private TgmUpdate[] updatesArray;
	

	public ArrayList<HttpMessageForTelegramServers> analyzeAnswerWithUpdatesAndGiveAppropriateMessageArrayList(
			TgmAnswerWithUpdateArray answerWithUpdates) {

		updatesArray = answerWithUpdates.getResult();

		ArrayList<HttpMessageForTelegramServers> answers = new ArrayList<HttpMessageForTelegramServers>();

		for (TgmUpdate update : updatesArray) {

			HttpMessageForTelegramServers messageToServer;

			messageToServer = analyzeAndAnswerASingleUpdate(update);

			answers.add(messageToServer);
		}

		return answers;
	}

	private HttpMessageForTelegramServers analyzeAndAnswerASingleUpdate(TgmUpdate update) {
		TgmUser messageSender = update.getMessage().getFrom();
		if (messageSenderIsMissingInDatabase(messageSender)) {
			myDAO.addNewUser(messageSender);
		}
		
		TgmChat messageChat = update.getMessage().getChat();
		if(sendingChatIsMissingInDatabase(messageChat)) {
			myDAO.addNewChat(messageChat);
		}
		
		MiddlelayerHttpAnswerForTelegram answerFromDialogHandler = myDH.processUpdateByGettingDbEntitiesAndDelegating(update);
		
		if (answerFromDialogHandler == null) {
			return null;
		}
		
		PresetMessageForSendMessage presetMessage = new PresetMessageForSendMessage(answerFromDialogHandler.getText(), answerFromDialogHandler.getChatId());
		
		HttpMessageForTelegramServers messageToReturnToTelegramAPI = new HttpMessageForTelegramServers(presetMessage);

		return messageToReturnToTelegramAPI;
	}
	
	public void printAllDbChatsToConsole() {
		System.out.println("Alle Chats in der Datenbank:");
		for (DbChat chat : myDAO.getAllChatsAsArrayList()) {
			System.out.println(chat.getId());
		}
	}

	public void printAllDbUsersToConsole() {
		System.out.println("Alle User in der Datenbank:");
		for (DbUser user : myDAO.getAllUsersAsArrayList()) {
			System.out.println(user.getFirstname());
		}
	}

	private boolean sendingChatIsMissingInDatabase(TgmChat messageChat) {
		try {
			myDAO.getChatById(messageChat.getId());
		} catch (EntityNotFoundException e) {
			return true;
		}
		return false;
	}

	private boolean messageSenderIsMissingInDatabase(TgmUser messageSender) {
		try {
			myDAO.getDbUserById(messageSender.getId());
		} catch (EntityNotFoundException e) {
			return true;
		}
		return false;
	}
}
