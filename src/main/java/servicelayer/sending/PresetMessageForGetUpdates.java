package servicelayer.sending;


import servicelayer.sending.HttpMessageForTelegramServers.HttpMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PresetMessageForGetUpdates extends PresetMessage {
	private int timeout = 100;
	
	//spÃ¤ter beim Neustart aus dem festen Speicher holen --> bei Shutdown dort abspeichern
	@Setter
	private static int offset = 916991347;
	
	private HttpMethod method;
	private String command;
	private String[] parameters;
	
	public PresetMessageForGetUpdates() {
		this.method = HttpMethod.GET;
		this.command = "getUpdates";
		this.parameters = new String[] {"timeout=" + timeout, "offset=" + offset};
	}
}
