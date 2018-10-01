package servicelayer.receiving.telegramobjects;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TgmUser {
	private int id;
	private boolean is_bot;
	private String first_name;
}
