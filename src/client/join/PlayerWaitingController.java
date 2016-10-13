package client.join;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import client.misc.MessageView;
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

		System.out.println("PLAYERWAITINGCONTROLLER: start called");

		//currently getting list of players from the server via ListGames.
		// we should probably be getting it from the ClientModel>Players. VIA NOTIFYOBSERVERS!!
		// yes we need to do that because if we're not updating the PlayerWaitingView based on the Players in the model
		// the view will not be updated with the players that joined your game from different computers.

		//maybe I could make a function that converts the Player object from ClientModel into PlayerInfo objects here in this class

		//--------
		//Give the PlayerWaitingView the existing list of players from the game they want to join:
		GameInfo addedGame = ClientUser.getInstance().getCurrentAddedGame();
		List<PlayerInfo> newPlayerInfoList = addedGame.getPlayers();
		PlayerInfo[] setThesePlayerInfos = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);

		getView().setPlayers(setThesePlayerInfos);
		//------------

		//check HERE if there are enough player in the game
		int numPlayersInGame = setThesePlayerInfos.length;
		System.out.println("PLAYERWAITINGCONTROLLER: start(): there are " + numPlayersInGame +
				" players in game " + addedGame.getId());


		if (numPlayersInGame < 4) {
			//there are still more players needed for this game, so show the PlayerWaitingView
			System.out.println("PLAYERWAITINGCONTROLLER: start(): Showing the PlayerWaitingView!");
			getView().showModal();
		}
		else if (numPlayersInGame == 4){
			//ok to start the game, we have all the players
			//so DON'T show the PlayerWaitingView
			System.out.println("PLAYERWAITINGCONTROLLER: start(): SKIPPING the PlayerWaitingView");
			//just start the game
			//test:
			getView().closeModal(); //might not be necessary. but where does the pgm execution go from here?
		}
		else {
			//something weird is happening
			System.out.println("PLAYERWAITINGCONTROLLER: start(): wat?");
		}

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

		//send addAI cmd to ClientFacade:

		if (ClientFacade.getInstance().addAI(addAICommand)) {
			//it worked

			System.out.println("PLAYERWAITINGCONT: AI Add successful! : " + selectedAI);

			//PlayerWaitingView now needs to update its list of players added to be able to show their name/color correctly
			//setPlayers() wants a PlayerInfo[], which can come from GameInfo[] for this game.

		}
		else {
			//it didn't work for some reason, show a message
			System.out.println("PLAYERWAITINGCONT: AI Add didn't work :(");
		}


		//this can all be in another function, like setUpView() or something
		//-----------------
		//now call setPlayers here - TALKING TO SERVER
		//ask server for gamelist:
		GameInfo[] newGameInfoArr = ClientFacade.getInstance().gamesList();
		//get the list of players from the server response using the current gameID
		GameInfo currGameInfo = newGameInfoArr[ClientUser.getInstance().getCurrentGameID()];
		//use that list of players to do setPlayers() here
		List<PlayerInfo> newPlayerInfoList = currGameInfo.getPlayers();
		//turn it into an array
		PlayerInfo[] setThesePlayerInfos = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);
		//give that array of PlayerInfos to setPlayers()
		getView().setPlayers(setThesePlayerInfos);
		int numPlayersInGame = setThesePlayerInfos.length;
		//-----------------

		//update PlayerWaitingView with newly added players IF THERE ARE LESS THAN 4 PLAYERS:

		//I don't think this is the right place to check how many players are in the game. You need to ask the server
		// how many players are in the game now that you've told it to add an aI.
		if (numPlayersInGame < 4){
			// we still need more players - go again
			System.out.println(">PLAYERWAITINGCONT: addAI: currGame #players < 4");
				//To get the PlayerWaitingView to update its look, do I need to create a brand new PWV?
				//or is SetPlayers() supposed to update the look?

			//try just closing the modal/reopening it:   - TA
			if (getView().isModalShowing()){
				getView().closeModal();
			}
			getView().showModal();

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

		//cast Observable into ClientModel here

		//the model comes back here, updated every 2 seconds.
		//this is where PWV pulls out the updated list of Players for ClientUser's currAddedGameID
		//and does setPlayers() or whatever it needs to do to show the new boxes on the view.
	}

}
