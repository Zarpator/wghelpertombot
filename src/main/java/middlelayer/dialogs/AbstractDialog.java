package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public abstract class AbstractDialog {

	public abstract MiddlelayerHttpAnswerForTelegram doLogicDependentOnCurrentStateInChatAndGetAnswer(
			DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven, TgmMessage message);

}
