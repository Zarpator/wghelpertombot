package servicelayer.sending;

import lombok.Getter;
import lombok.Setter;
import servicelayer.sending.MessageForTelegramServers.HttpMethod;

@Getter @Setter
public class PresetMessageForSendMessage extends PresetMessage{
	
	private HttpMethod method = HttpMethod.GET;
	private String command = "sendMessage";
	private String[] parameters;
	
	private int chat_id;
	private String text;
	
	public PresetMessageForSendMessage(String text, int chat_id) {
		this.chat_id = chat_id;
		this.text = text;
		
		this.parameters = new String[] {"chat_id=" + chat_id, "text=" + text};
	}
	
}
