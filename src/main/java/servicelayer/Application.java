package servicelayer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import middlelayer.Inspector;
import servicelayer.receiving.TgmAnswerWithMessage;
import servicelayer.receiving.TgmAnswerWithUpdateArray;
import servicelayer.sending.MessageForTelegramServers;
import servicelayer.sending.PresetMessageForGetUpdates;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		while (true) {

			MessageSenderAndReceiver myMessageSender = new MessageSenderAndReceiver();

			Inspector myInspector = new Inspector();

			TgmAnswerWithUpdateArray answer = myMessageSender.analyzeAndGiveAppropriateAnswer(
					new MessageForTelegramServers(new PresetMessageForGetUpdates()), TgmAnswerWithUpdateArray.class);

			ArrayList<MessageForTelegramServers> messagesToReturn = myInspector
					.analyzeAnswerWithUpdatesAndGiveAppropriateMessageArrayList(answer);

			for (MessageForTelegramServers message : messagesToReturn) {
				if (message != null) {
					TgmAnswerWithMessage returnedResponseFromTgmServer = myMessageSender
							.analyzeAndGiveAppropriateAnswer(message, TgmAnswerWithMessage.class);
					System.out.println("Telegramserver ist ok: " + returnedResponseFromTgmServer.getOk());
					System.out.println("Nachricht: " + returnedResponseFromTgmServer.getResult().getText());
				} else {
					System.out.println("Received empty Message from Inspector");
				}
				
				
			}

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("servicelayer.Application: " + e.getMessage());
			}
		}
	}
}

//ICE 201 --> Basel SBB
//18:40 Gleis 4

//TGV
//18:30 Gleis 5 