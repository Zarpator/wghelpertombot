package servicelayer.receiving.telegramobjects;

import lombok.Getter;
import lombok.Setter;
import servicelayer.receiving.TgmPossibleResult;

@Getter @Setter
public class TgmUpdate extends TgmPossibleResult{
	private int update_id;
	private TgmMessage message;
	private TgmMessage edited_message;
	private TgmMessage channel_post;
	private TgmMessage edited_channel_post;
}
