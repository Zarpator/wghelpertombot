package servicelayer.sending;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpMessageForTelegramServers {
	public HttpMessageForTelegramServers(PresetMessage presetMessage) {
		this.method = presetMessage.getMethod();
		this.command = presetMessage.getCommand();
		this.parameters = presetMessage.getParameters();
	}

	private HttpMethod method;
	private String schemeAndHost = "https://api.telegram.org/";
	private String token = getTokenFromLocalInfo();
	private String command;
	private String[] parameters;

	public enum HttpMethod {
		GET, SET;
	}

	private String getTokenFromLocalInfo() {
		LocalInfo info;

		try {
			
			File jsonWithToken = new File("src/main/java/servicelayer/sending/localInfo.json");
			info = new ObjectMapper().readValue(jsonWithToken, LocalInfo.class);
			return info.getToken();
			
		} catch (JsonMappingException e) {
			System.out.println(e.getMessage());
		} catch (JsonParseException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}
}
