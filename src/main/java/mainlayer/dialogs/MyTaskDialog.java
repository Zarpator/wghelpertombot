package mainlayer.dialogs;

import java.util.List;

import datalayer.DataAccessObject;
import datalayer.DbChat;
import datalayer.DbUser;
import mainlayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class MyTaskDialog extends AbstractFullDialog {
	public MyTaskDialog(TgmMessage message, DataAccessObject dao, DbChat chat, DbUser user) {
		super(message, dao,chat, user);
		this.mySpecificDialogStates = new DialogState[] {
				new FirstDialogState()
		};
	}
	
	private class FirstDialogState extends DialogState{

		@Override
		public MiddlelayerHttpAnswerForTelegram doLogic() {
			
			MiddlelayerHttpAnswerForTelegram messageForDialogHandler = new MiddlelayerHttpAnswerForTelegram();

			messageForDialogHandler.setChatId(dbChatWhereCommandWasGiven.getId());

			List<String> allTasks = dbUserWhoSentMessage.getRoomsToDo();

			if (allTasks == null || allTasks.isEmpty()) {
				messageForDialogHandler.setText("Du hast gerade nichts zu tun :)");
			} else {
				String text = "Diese Räume musst du momentan putzen:\n\n";
				for(String nextTask : allTasks)
					text += nextTask + ", ";	
				String finalText = text.substring(0, text.length() - 3);
				messageForDialogHandler.setText(finalText);
			}
			
			dialogFinished = true;
			
			return messageForDialogHandler;
		}
		
	}
}
