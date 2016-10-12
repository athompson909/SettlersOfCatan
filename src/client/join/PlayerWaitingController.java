package client.join;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import client.misc.MessageView;
import shared.model.ClientUpdateManager;
import shared.model.commandmanager.game.AddAICommand;

import java.util.List;
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

		//TEST------
		//--------
		//Give the PlayerWaitingView the existing list of players from the game they want to join:
			GameInfo addedGame = ClientUser.getInstance().getCurrentAddedGame();
			List<PlayerInfo> newPlayerInfoList = addedGame.getPlayers();
			PlayerInfo[] setThesePlayerInfos = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);

			getView().setPlayers(setThesePlayerInfos);
		//------------

		getView().showModal();

	}

	@Override
	public void addAI() {
		// called when they push the "Add computer player" button

		System.out.println("PLAYERWAITINGCONT: addAI called");

		//actually talk to the server to add the player here

		//get the AI they picked from the spinner (only really one choice though)
		String selectedAI = getView().getSelectedAI();
		//build a AddAICmd object:
		AddAICommand addAICommand = new AddAICommand(selectedAI);

		System.out.println("PLAYERWAITINGCONT: adding AI type: " + selectedAI);

		//send cmd to ClientFacade:

		if (ClientFacade.getInstance().addAI(addAICommand)) {
			//it worked

			System.out.println("PLAYERWAITINGCONT: AI Add successful! : " + selectedAI);

			//PlayerWaitingView now needs to update its list of players added to be able to show their name/color correctly
			//setPlayers() wants a PlayerInfo[], which can come from GameInfo[] for this game.

			//To get the PlayerWAitingView to update its look, do I need to create a brand new PWV?
			//or is SetPlayers() supposed to update the look?
		}
		else {
			//it didn't work for some reason, show a message
			System.out.println("PLAYERWAITINGCONT: AI Add didn't work :(");
		}



		//update PlayerWaitingView

		//don't close until it's done - all four players are there
		if (ClientUser.getInstance().getCurrentAddedGame().getPlayers().size() < 4){
			// we still need more players - go again
			//make a new PlayerWaitingView?
			System.out.println(">PLAYERWAITINGCONT: addAI: currGame #players < 4");

		}
		else{
			System.out.println(">PLAYERWAITINGCONT: addAI: currGame has enough players! ");
			//ok to be done picking players
			getView().closeModal();
		}

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
