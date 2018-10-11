package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public class AnswerwithusersownmessageDialog extends AbstractDialog {

	@Override
	public MiddlelayerHttpAnswerForTelegram doLogicDependentOnCurrentStateInChatAndGetAnswer(
			DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven, TgmMessage message) {
		String name = message.getFrom().getFirst_name();
		String text = message.getText();
		int id = message.getChat().getId();

		MiddlelayerHttpAnswerForTelegram returnMessage = new MiddlelayerHttpAnswerForTelegram();

		returnMessage.setChatId(id);
		returnMessage.setText("Hi " + name + "! Du schriebst: " + text);

		return returnMessage;
	}
	
}
