package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class MytaskDialog extends AbstractFullDialog {
	public MytaskDialog() {
		this.mySpecificDialogStates = new DialogState[] {
				new FirstDialogState()
		};
	}
	
	private class FirstDialogState extends DialogState{

		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic(DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven,
				TgmMessage message) {
			
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

			messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());

			String nextTask = dbUserWhoSentMessage.getNextTask();

			if (nextTask == null) {
				messageForDialogHandler.setText("Du hast nichts zu tun :)");
			} else {
				messageForDialogHandler.setText(nextTask);
			}
			
			dialogFinished = true;
			
			return messageForDialogHandler;
		}
		
	}
}
