package servicelayer.sending;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageForTelegramServers {
	public MessageForTelegramServers(PresetMessage presetMessage) {
		this.method = presetMessage.getMethod();
		this.command = presetMessage.getCommand();
		this.parameters = presetMessage.getParameters();
	}

	private HttpMethod method;
	private String schemeAndHost = "https://api.telegram.org/";
	private String token = "626144048:AAGHqF_B78gDR54v9qFWKEeaDY0eXbbHfjY";
	private String command;
	private String[] parameters;
	
	public enum HttpMethod {
		GET, SET;
	}
}
