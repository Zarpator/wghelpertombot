package middlelayer.dialogs;

import datalayer.DbChat;
import datalayer.DbUser;
import middlelayer.MiddlelayerHttpAnswerForTelegram;
import servicelayer.receiving.telegramobjects.TgmMessage;

public abstract class AbstractFullDialog {
	protected DialogState[] mySpecificDialogStates;
	private final int stateIncrementStandard = 1;
	protected int stateIncrement = stateIncrementStandard;
	protected boolean dialogFinished = false;

	public MiddlelayerHttpAnswerForTelegram doLogicDependentOnCurrentStateInChatAndGetAnswer(
			DbUser dbUserWhoSentMessage, DbChat dbChatWhereCommandWasGiven, TgmMessage message) {
		int currentStateInThisDialog = dbChatWhereCommandWasGiven.getCurrentStateInOngoingDialog();

		DialogState state = mySpecificDialogStates[currentStateInThisDialog];
		MiddlelayerHttpAnswerForTelegram answer = state.doLogic(dbUserWhoSentMessage, dbChatWhereCommandWasGiven,
				message);

		if (dialogFinished) {
			dbChatWhereCommandWasGiven.resetOngoingDialog();
		} else {
			dbChatWhereCommandWasGiven.incrementCurrentState(stateIncrement);
		}
		
		stateIncrement = stateIncrementStandard;

		return answer;
	}

	protected abstract class DialogState {
		public abstract MiddlelayerHttpAnswerForTelegram doLogic(DbUser dbUserWhoSentMessage,
				DbChat dbChatWhereCommandWasGiven, TgmMessage message);
	}
}