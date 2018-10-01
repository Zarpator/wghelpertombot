package servicelayer.receiving.telegramobjects;

import lombok.Getter;
import lombok.Setter;
import servicelayer.receiving.TgmPossibleResult;

@Setter @Getter
public class TgmMessage extends TgmPossibleResult {
	private int message_id;
	private TgmUser from;
	private int date;
	private TgmChat chat;
	private String text;
}
