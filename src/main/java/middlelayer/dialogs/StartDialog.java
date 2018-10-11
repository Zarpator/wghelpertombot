package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class StartDialog extends AbstractDialog {
	@Override
	public MiddlelayerHttpAnswerForTelegram doLogicDependentOnCurrentStateInChatAndGetAnswer(
			DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven, TgmMessage message) {
		
		MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

		messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());
		messageForDialogHandler.setText("Hallo! Welche Räume müsst ihr in eurer WG putzen?");

		return messageForDialogHandler;	}
	
}
