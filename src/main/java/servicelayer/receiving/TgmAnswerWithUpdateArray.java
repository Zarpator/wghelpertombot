package servicelayer.receiving;

import lombok.Getter;
import servicelayer.receiving.telegramobjects.TgmUpdate;

@Getter
public class TgmAnswerWithUpdateArray extends TgmAnswerArray{
	private TgmUpdate[] result;
}
