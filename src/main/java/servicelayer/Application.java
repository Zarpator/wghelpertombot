package servicelayer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

//import middlelayer.Inspector;
//import servicelayer.receiving.TgmAnswerWithMessage;
//import servicelayer.receiving.TgmAnswerWithUpdateArray;
import servicelayer.receiving.getMeAnswer;
//import servicelayer.sending.MessageForTelegramServers;
//import servicelayer.sending.PresetMessageForGetUpdates;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		
		RestTemplate restTemplate = new RestTemplate();
        getMeAnswer answer = restTemplate.getForObject("https://api.telegram.org/bot626144048:AAGHqF_B78gDR54v9qFWKEeaDY0eXbbHfjY/getMe", getMeAnswer.class);
        System.out.println(answer.toString());
		
		while (true) {

			MessageSenderAndReceiver myMessageSender = new MessageSenderAndReceiver();
			
			Inspector myInspector = new Inspector();
			
			
			TgmAnswerWithUpdateArray answer = myMessageSender.sendAndReceive(new MessageForTelegramServers(new PresetMessageForGetUpdates()), TgmAnswerWithUpdateArray.class);
			
			MessageForTelegramServers[] messagesToReturn = myInspector.analyzeAndGiveAppropriateMessages(answer);
			
			for (MessageForTelegramServers message : messagesToReturn) {
				TgmAnswerWithMessage returnedResponseFromTgmServer = myMessageSender.sendAndReceive(message, TgmAnswerWithMessage.class);
				System.out.println("Telegramserver ist ok: " + returnedResponseFromTgmServer.getOk());
				System.out.println("Nachricht: " + returnedResponseFromTgmServer.getResult().getText());
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