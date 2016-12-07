package client.join;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import client.misc.MessageView;
import exceptions.ClientException;
import shared.definitions.CatanColor;
import shared.model.commandmanager.game.AddAICommand;
import shared.model.commandmanager.game.GameJoinCommand;

import java.util.*;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {
	/**
	 * Lightweight personal poller for PlayerWaitingController
	 */
	private Timer miniPollTimer;
	//number of seconds to wait between requesting updates from the server
	private int pollInterval = 1;
	/**
	 * The list of playerInfos that gets updated every time a new player is added (either AI or real)
	 */
	private PlayerInfo[] currPlayerInfosList;
	private IMessageView messageView = new MessageView();

	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	/**
	 * Initializes the MiniPoller so it starts the poll loop, gives the PlayerWaitingView an initial list of players
	 * in the chosen game, and checks if we actually need to show this view at all (if there are less than 4 players)
	 */
	@Override
	public void start() {

		System.out.println("PLAYERWAITINGCONTROLLER: start called");

		//start PWC's personal poller
		miniPollTimer = new Timer(true);//true tells the program to end this thread if it is the only one left so we cand exit the program
		miniPollTimer.scheduleAtFixedRate(new PlayerWaitingMiniPoller(), 1, pollInterval *1000);

		//--------
		//Give the PlayerWaitingView the existing list of players from the game they want to join:
		GameInfo addedGame = ClientUser.getInstance().getCurrentAddedGameInfo();
		List<PlayerInfo> newPlayerInfoList = addedGame.getPlayers();
		currPlayerInfosList = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);

		getView().setPlayers(currPlayerInfosList);
		//------------


		//check if there are enough players in the game to actually need to show this view
		int numPlayersInGame = currPlayerInfosList.length;
		System.out.println("PLAYERWAITINGCONTROLLER: start(): there are " + numPlayersInGame +
				" players in game " + addedGame.getId());

		if (numPlayersInGame < 4) {
			//there are still more players needed for this game, so DO show the PlayerWaitingView
			System.out.println("PLAYERWAITINGCONTROLLER: start(): Showing the PlayerWaitingView!");
			getView().showModal();
		}
		else if (numPlayersInGame == 4){
			//ok to start the game, we have all the players - so DON'T show the PlayerWaitingView
			System.out.println("PLAYERWAITINGCONTROLLER: start(): SKIPPING the PlayerWaitingView");

			startGamePlay();
		}
		else {
			//something weird is happening
			System.out.println("PLAYERWAITINGCONTROLLER: start(): wat?");
		}

	}

	/**
	 * Stops the PWVminiPoller, starts the main Poller, and closes the PWC modal.
	 * Starts the main gameplay phase.
	 */
	public void startGamePlay(){
		System.out.println(">PWC: STARTING GAME PLAY **************");
		miniPollTimer.cancel();
		System.out.println("~~~~~~~~~~~~ ENDING PWC MINIPOLLER ~~~~~~~~~~~~~");
		Client.getInstance().startServerPoller();

		// do player color null checking here:
		//make each client/player check their own color only
		//send a new joinGameCommand for localPlayer if they still have a NULL color at this point
			//this will eliminate the need to check for NULL colors every 2 sec in ServerFacade

			if (ClientUser.getInstance().getColor() == CatanColor.NULL || ClientUser.getInstance().getColor() == null){

				System.out.println("\t\t startGamePlay() ClientUser " + ClientUser.getInstance().getName() + " still has color NULL");
				//pick a default color that hasn't been used yet
				CatanColor defaultedColor = pickDefaultColor();

				//send a JoinGame command with that default color to override the NULL
				int gameIDtoJoin = ClientUser.getInstance().getCurrentAddedGameInfo().getId();
				GameJoinCommand gameJoinCmdWithDefaultedColor = new GameJoinCommand(gameIDtoJoin, defaultedColor);
				//this should override the user's default color (NULL probably) with the new defaulted color.
				if (ClientFacade.getInstance().gameJoin(gameJoinCmdWithDefaultedColor)){
					System.out.println(">PWC: STARTGAMEPLAY: user " + ClientUser.getInstance().getName() + " defaulted to color " + defaultedColor);
						//should be ok to start the game  now
				}
				else {
					System.out.println(">PWC: STARTGAMEPLAY: unable to reset user " + ClientUser.getInstance().getName() + "'s NULL color!");
				}

			}

		//all colors need to be set before this point. no nulls
		ClientUser.getInstance().setPlayerColors();

		//Save the final GameInfo item to the ClientUser singleton
		GameInfo[] updatedGameInfosList = ClientFacade.getInstance().gamesList();
		GameInfo finalGameInfo = updatedGameInfosList[ClientUser.getInstance().getCurrentGameID()];
		ClientUser.getInstance().setCurrentAddedGameInfo(finalGameInfo);

		//repopulates the port vertex locations to the correct values each time the player rejoins
		Client.getInstance().getClientModel().getMap().populatePortVertexLocations();

		if (getView().isModalShowing()){  //close PlayerWaitingView
			getView().closeModal();
		}

		Client.getInstance().setStartGame(true);
	}

	/**
	 * Helper function for StartGamePlay() - called if the localPlayer hasn't selected a color yet (it's still NULL)
	 * but they need to be assigned a default color that hasn't been used yet so the game can start
	 */
	private CatanColor pickDefaultColor(){

		GameInfo currGameInfo = ClientUser.getInstance().getCurrentAddedGameInfo();

		//check each color in CatanColor
		for (CatanColor currColor : CatanColor.values()) {

			//check if any of the players in this game are already claimed currColor
			for (int p = 0; p < currGameInfo.getPlayers().size(); p++){
				PlayerInfo currPI = currGameInfo.getPlayers().get(p);

				if (currPI.getColor() == currColor){
					System.out.println(">pickDefaultColor: player " + currPI.getName() + " already has color " + currColor);
					break; //no other player will have this color, so go to next color (hopefully)
				}
			}

			//no player had this color yet, so it's ok to use it as a default
			return currColor;
		}

		return null;  //should never get here
	}


	/**
	 * Called when they push the "Add a computer player" button
	 *
	 * Sends an addAI command to the ClientFacade. If the server returns true (it worked),
	 * gets the new list of players from the server and updates the PWV.
	 */
	@Override
	public void addAI() {
		//get the AI they picked from the spinner
		String selectedAI = getView().getSelectedAI();
		//build a AddAICmd object:
		AddAICommand addAICommand = new AddAICommand(selectedAI);

		//send addAI cmd to ClientFacade:
		if (ClientFacade.getInstance().addAI(addAICommand)) {
			//it worked
			updateView(updatePlayerInfosList());
		}
		else {
			//it didn't work for some reason, show a message
			System.out.println("PLAYERWAITINGCONT: AI Add didn't work :(");
			showAddAIFailedMessage();
		}
	}

	/**
	 * Holds functionality for asking server for gamesList, pulling out Players from the current game,
	 * and returning those as an array that setPlayers() can use.
	 *
	 * Both the PWCminiPoller and addAI() use this!
	 * @return
	 */
	public PlayerInfo[] updatePlayerInfosList(){
		GameInfo[] newGameInfos = ClientFacade.getInstance().gamesList();
		GameInfo currGameToDisplay = newGameInfos[ClientUser.getInstance().getCurrentGameID()];
		List<PlayerInfo> tempPIArrList = currGameToDisplay.getPlayers();
		PlayerInfo[] newPlayerInfosList = tempPIArrList.toArray(new PlayerInfo[tempPIArrList.size()]);

		return newPlayerInfosList;
	}

	/**
	 * 	called after AddAI() or MiniPoller happens  - gets new list of players from server, refreshes PlayerWaitingView
	 */
	private void updateView(PlayerInfo[] newPlayerInfosToDisplay){
		System.out.println(">PWC: updateView: called, newPlayerInfosList= " + newPlayerInfosToDisplay.length);

		//use this array to update the view
		setCurrPlayerInfosList(newPlayerInfosToDisplay);

		//update the View with the new players:  ***************************
		getView().setPlayers(newPlayerInfosToDisplay);

		int numPlayersInGame = newPlayerInfosToDisplay.length;
		//-----------------

		//Decide whether or not to close the modal and start the game
		//update PlayerWaitingView with newly added players IF THERE ARE LESS THAN 4 PLAYERS:
		if (numPlayersInGame < 4){
			// we still need more players - go again
			System.out.println(">PWC: updateView: currGame #players < 4");

			//Close/reopen the modal to refresh it
			if (getView().isModalShowing()){
				getView().closeModal();
			}
			getView().showModal();

		}
		else {
			//ok to be done picking players
			System.out.println(">PWC: updateView: currGame has enough players! ");
			startGamePlay();
		}
	}

	/**
	 * after bad input is rejected a customizable message is displayed
	 */
	private void showAddAIFailedMessage() {
		MessageView addAIFailedView = (MessageView) messageView;
		String title = "Add AI Error";
		String message = "Adding computer player failed!";

		addAIFailedView.setTitle(title, 220);
		addAIFailedView.setMessage(message, 220);
		addAIFailedView.setCloseButton("Retry");
		addAIFailedView.showModal();
	}

	//UNUSED
	@Override
	public void update(Observable o, Object arg) {

	}

	public PlayerInfo[] getCurrPlayerInfosList() {
		return currPlayerInfosList;
	}

	public void setCurrPlayerInfosList(PlayerInfo[] currPlayerInfosList) {
		this.currPlayerInfosList = currPlayerInfosList;
	}



	//////////////

	/**
	 * MiniPoller is PlayerWaitingController's personal poller that gets an updated list of games every 2 sec.
	 * The big/main poller for this program wasn't working too well for this part so Sierra made a new poller here.
	 * This TimerTask is started upon PlayerWaitingController.start() and stopped just before PlayerWaitingview closes.
	 *
	 * NOTE: this poller runs 2x as fast as the JoinGameMiniPoller!
	 */
	private class PlayerWaitingMiniPoller extends TimerTask {
		public void run() {
			try {
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~PWC miniPoller~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("PWVminiPoller: fetching gamesList: " + new Date().toString());
				fetchGamesList();
			} catch (Exception e) {
				System.out.println("\t ~~ PWVminiPoller Exception! ~~");
				System.out.println(e);
				//e.printStackTrace();
			}
		}

		/**
		 * fetchNewModel() sends an gamesList update request to the saved proxy (currentProxy) via HTTP request.
		 * This function is called every 2 pollInterval when pollTimer tells it to.
		 */
		public void fetchGamesList() throws ClientException {

			//grab new list from server
			PlayerInfo[] newPlayerInfos = updatePlayerInfosList();

			System.out.println("\t\tPWVminiPoller: just got new playerInfosList, size= " + newPlayerInfos.length);
			System.out.println("\t\tPWVminiPoller: currPlayerInfosList size= " + currPlayerInfosList.length);

			//check if we actually need to update the view
			//if (currPlayerInfosList.length < newPlayerInfos.length) {
			if (isViewUpdateNeeded(newPlayerInfos)){
				updateView(newPlayerInfos);
			} else {  //don't update the view
				System.out.println("\t\tPWVminiPoller: no change in PlayerInfoList");
			}

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
		}

		/**
		 * Checks each of the PlayerInfos in the array coming from the server,
		 * and returns True if the list size is different from before (more players were added),
		 * or if any of them are different from the existing list of PlayerInfos, including color/name changes.
		 *
		 * @return true if the PlayerInfo list itself or any item in that list has been updated
		 */
		public boolean isViewUpdateNeeded(PlayerInfo[] newPlayerInfosArr) {
			if (currPlayerInfosList.length != newPlayerInfosArr.length) {
				System.out.println("\tPWVminiPoller: iVUN: PlayerInfo arrsize was different");
				return true;
			}

			//check if any of them have been updated
			for (int p = 0; p < currPlayerInfosList.length; p++) {
				PlayerInfo existingPI = currPlayerInfosList[p];
				PlayerInfo newPI = newPlayerInfosArr[p];

				if (!existingPI.equals(newPI)) {
					System.out.println("\tPWVminiPoller: iVUN: PlayerInfo " + newPI.getName() + " was different");
					return true;
				}
			}

			//no differences found
			return false;
		}

	}//end inner class

}
