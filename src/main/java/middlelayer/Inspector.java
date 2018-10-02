package middlelayer;

import java.util.ArrayList;

import servicelayer.receiving.TgmAnswerWithUpdateArray;
import servicelayer.receiving.telegramobjects.TgmMessage;
import servicelayer.receiving.telegramobjects.TgmUpdate;
import servicelayer.sending.MessageForTelegramServers;
import servicelayer.sending.PresetMessageForSendMessage;

public class Inspector {

	public ArrayList<MessageForTelegramServers> analyzeAnswerWithUpdatesAndGiveAppropriateMessageArrayList(
			TgmAnswerWithUpdateArray answerWithUpdates) {

		TgmUpdate[] updatesArray = answerWithUpdates.getResult();

		ArrayList<MessageForTelegramServers> answers = new ArrayList<MessageForTelegramServers>();

		for (TgmUpdate update : updatesArray) {

			MessageForTelegramServers messageToServer;

			messageToServer = analyzeAndAnswer(update);

			answers.add(messageToServer);
		}

		return answers;
	}

	private MessageForTelegramServers analyzeAndAnswer(TgmUpdate update) {

		TgmMessage message = update.getMessage();
		MessageForTelegramServers messageToReturnToTelegramAPI;

		// Can later also be something else than a message to the bot!! --> Errors
		// possible
		if (message == null) {
			System.out.println("There is no Message in the received Update!");
			return null;
		}

		switch (message.getText()) {
		case "/start":
			messageToReturnToTelegramAPI = doStartRoutine(message);
			break;
		default:
			messageToReturnToTelegramAPI = answerWithUsersOwnMessage(message);
			break;
		}

		return messageToReturnToTelegramAPI;
	}
	
	private MessageForTelegramServers doStartRoutine(TgmMessage message) {
		return null;
	}

	private MessageForTelegramServers answerWithUsersOwnMessage(TgmMessage message) {
		String name = message.getFrom().getFirst_name();
		String text = message.getText();
		int id = message.getChat().getId();
		
		return new MessageForTelegramServers(
				new PresetMessageForSendMessage("Hi " + name + "! Du schriebst: " + text, id));
	}
}
