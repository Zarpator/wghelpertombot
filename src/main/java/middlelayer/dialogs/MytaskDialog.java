package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class MytaskDialog extends AbstractDialog {

	@Override
	public MiddlelayerHttpAnswerForTelegram doLogicDependentOnCurrentStateInChatAndGetAnswer(
			DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven, TgmMessage message) {
		MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

		
		messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());

		String nextTask = dbUserWhoSentMessage.getNextTask();

		if (nextTask == null) {
			messageForDialogHandler.setText("Du hast nichts zu tun :)");
		} else {
			messageForDialogHandler.setText(nextTask);
		}
		return messageForDialogHandler;
	}

}
