package middlelayer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MiddlelayerHttpAnswerForTelegram {
	public static final MiddlelayerHttpAnswerForTelegram noMessage = null;
	private String text;
	private int chatId;
	
}
