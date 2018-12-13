package mainlayer.dialogs;

import datalayer.DataAccessObject;
import datalayer.DbChat;
import datalayer.DbUser;
import mainlayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class AnswerwithusersownmessageDialog extends AbstractFullDialog {

	public AnswerwithusersownmessageDialog(TgmMessage message, DataAccessObject dao, DbChat chat, DbUser user) {
		super(message, dao, chat, user);
		this.mySpecificDialogStates = new DialogState[] {
				new FirstDialogState()
		};
	}
	
	private class FirstDialogState extends DialogState {

		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {
			String name = messageToProcess.getFrom().getFirst_name();
			String text = messageToProcess.getText();
			int id = messageToProcess.getChat().getId();

			MiddlelayerHttpAnswerForTelegram returnMessage = new MiddlelayerHttpAnswerForTelegram();

			returnMessage.setChatId(id);
			returnMessage.setText("Hi " + name + "! Du schriebst: " + text);

			dialogFinished = true;
			
			return returnMessage;
		}	
	}
}
