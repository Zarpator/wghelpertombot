package servicelayer.receiving;

import lombok.Getter;
import lombok.Setter;
import servicelayer.receiving.telegramobjects.TgmMessage;

@Getter @Setter
public class TgmAnswerWithMessage extends TgmAnswerNormal {
	private TgmMessage result;
}
