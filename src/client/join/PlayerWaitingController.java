package client.join;

import client.ClientFacade;
import client.base.Controller;
import client.misc.IMessageView;
import client.misc.MessageView;
import shared.model.commandmanager.game.AddAICommand;

import java.util.Observable;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	//used to show messages
	private IMessageView messageView;

	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {

		getView().showModal();
	}

	@Override
	public void addAI() {
		// called when they push the "Add computer player" button

		//actually talk to the server to add the player here

		//get the AI they picked from the spinner (only really one choice though)
		String selectedAI = getView().getSelectedAI();
		//build a AddAICmd object:
		AddAICommand addAICommand = new AddAICommand(selectedAI);
		//send cmd to ClientFacade:
		if (ClientFacade.getInstance().addAI(addAICommand) == true){
			//it worked
			//PlayerWaitingView now needs to update its list of players added to be able to show their name/color correctly
			//setPlayers() wants a PlayerInfo[], which can come from GameInfo[] for this game.
		}
		else {
			//it didn't work for some reason, show a message
			String msgTitle = "AddAI Failed";
			String message = "Adding computer player " + selectedAI + " failed!";
			showRejectMessage(msgTitle, message);
		}

		getView().closeModal();
	}

	/**
	 * after bad input is rejected a customizable message is displayed
	 * @param title message title
	 * @param message message content
	 */
	private void showRejectMessage(String title, String message) {
		MessageView addAIFailedView = (MessageView) messageView;

		addAIFailedView.setTitle(title, 220);
		addAIFailedView.setMessage(message, 220);
		addAIFailedView.setCloseButton("Retry");
		addAIFailedView.showModal();
	}



	@Override
	public void update(Observable o, Object arg) {

	}

}
