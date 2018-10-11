package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class AnswerwithusersownmessageDialog extends AbstractFullDialog {

	public AnswerwithusersownmessageDialog() {
		this.mySpecificDialogStates = new DialogState[] {
				new FirstDialogState()
		};
	}
	
	private class FirstDialogState extends DialogState {

		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic(DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven,
				TgmMessage message) {
			String name = message.getFrom().getFirst_name();
			String text = message.getText();
			int id = message.getChat().getId();

			MiddlelayerHttpAnswerForTelegram returnMessage = new MiddlelayerHttpAnswerForTelegram();

			returnMessage.setChatId(id);
			returnMessage.setText("Hi " + name + "! Du schriebst: " + text);

			dialogFinished = true;
			
			return returnMessage;
		}	
	}
}
