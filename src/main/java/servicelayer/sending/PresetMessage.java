package servicelayer.sending;
import servicelayer.sending.MessageForTelegramServers.HttpMethod;

public abstract class PresetMessage {
	public abstract HttpMethod getMethod();

	public abstract String getCommand();

	public abstract String[] getParameters();

	protected String getParameterString(String[] parametersAndValuesList) {
		String outputParamaterString = "";

		if (parametersAndValuesList.length > 1) {
			outputParamaterString += "?" + parametersAndValuesList[0] + "=" + parametersAndValuesList[1];

			for (int paramOfPair = 2; paramOfPair < parametersAndValuesList.length; paramOfPair += 2) {
				outputParamaterString += "&" + parametersAndValuesList[paramOfPair] + "="
						+ parametersAndValuesList[paramOfPair + 1];
			}
		}

		return outputParamaterString;
	}
}