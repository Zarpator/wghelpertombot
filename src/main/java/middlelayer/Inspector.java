package middlelayer;

import java.util.ArrayList;

import servicelayer.receiving.TgmAnswerWithUpdateArray;
import servicelayer.receiving.telegramobjects.TgmMessage;
import servicelayer.receiving.telegramobjects.TgmUpdate;
import servicelayer.sending.MessageForTelegramServers;
import servicelayer.sending.PresetMessageForSendMessage;

public class Inspector {

	public ArrayList<MessageForTelegramServers> analyzeAndGiveAppropriateMessages(
			TgmAnswerWithUpdateArray answerWithUpdates) {

		TgmUpdate[] updatesArray = answerWithUpdates.getResult();
		ArrayList<MessageForTelegramServers> answers = new ArrayList<MessageForTelegramServers>();

		if (updatesArray.length != 0) {
			for (TgmUpdate update : updatesArray) {
				TgmMessage message = update.getMessage();

				String name = message.getFrom().getFirst_name();
				String text = message.getText();
				int id = message.getChat().getId();

				answers.add(new MessageForTelegramServers(
						new PresetMessageForSendMessage("Hi " + name + "! Du schriebst: " + text, id)));
			}
		}

		return answers;

	}
}
