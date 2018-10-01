package servicelayer;

import org.springframework.web.client.RestTemplate;

import servicelayer.receiving.TgmAnswerSuperClass;
import servicelayer.receiving.TgmAnswerWithUpdateArray;
import servicelayer.receiving.telegramobjects.TgmUpdate;
import servicelayer.sending.MessageForTelegramServers;
import servicelayer.sending.PresetMessageForGetUpdates;

public class MessageSenderAndReceiver {
	public <T extends TgmAnswerSuperClass> T analyzeAndGiveAppropriateAnswer(MessageForTelegramServers message,
			Class<T> typeOfTheTgmAnswer) {

		String url = buildURL(message);

		RestTemplate restTemplate = new RestTemplate();
		T answer = restTemplate.getForObject(url, typeOfTheTgmAnswer);

		incrementOffsetIfItsUpdateRequest(message, answer);

		return answer;
	}

	private <T extends TgmAnswerSuperClass> void incrementOffsetIfItsUpdateRequest(MessageForTelegramServers message,
			T answer) {
		if (message.getCommand().equals("getUpdates")) {

			TgmUpdate[] updates = ((TgmAnswerWithUpdateArray) answer).getResult();

			int lastIndexOfTheReceivedUpdates = updates.length - 1;
			
			if(lastIndexOfTheReceivedUpdates != -1) {
				int newOffset = updates[lastIndexOfTheReceivedUpdates].getUpdate_id() + 1;
				PresetMessageForGetUpdates.setOffset(newOffset);
			}
		}
	}

	private String getFullParameterStringOutOf(String[] parametersArray) {
		String outputParameterString = "";
		if (parametersArray != null) {
			outputParameterString += "?" + parametersArray[0];
			for (int posInParametersArray = 1; posInParametersArray < parametersArray.length; posInParametersArray++) {
				outputParameterString += "&" + parametersArray[posInParametersArray];
			}
		}
		return outputParameterString;
	}

	private String buildURL(MessageForTelegramServers message) {
		String url = "";
		url += message.getSchemeAndHost();
		url += "/bot" + message.getToken();
		url += "/" + message.getCommand();
		url += getFullParameterStringOutOf(message.getParameters());
		return url;
	}
}
