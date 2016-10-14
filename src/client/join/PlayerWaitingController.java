package client.join;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.ServerPoller;
import client.base.Controller;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import client.misc.MessageView;
import exceptions.ClientException;
import shared.definitions.CatanColor;
import shared.model.ClientModel;
import shared.model.commandmanager.game.AddAICommand;
import shared.model.player.Player;

import java.util.*;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	//TRY HAVING A PERSONAL MINI-POLLER HERE
	private Timer miniPollTimer;
	//number of seconds to wait between requesting updates from the server
	private int seconds = 2;



	//used to show messages
	private IMessageView messageView;

	private ClientModel clientModel;

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
		//TRY
		miniPollTimer = new Timer(true);//true tells the program to end this thread if it is the only one left so we cand exit the program
		miniPollTimer.scheduleAtFixedRate(new PlayerWaitingController.MiniPollerTask(), 1, seconds*1000);

		//--------
		//Give the PlayerWaitingView the existing list of players from the game they want to join:
		GameInfo addedGame = ClientUser.getInstance().getCurrentAddedGame();
		List<PlayerInfo> newPlayerInfoList = addedGame.getPlayers();
		PlayerInfo[] setThesePlayerInfos = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);

		getView().setPlayers(setThesePlayerInfos);
		//------------


		//check if there are enough players in the game to actually need to show this view
		int numPlayersInGame = setThesePlayerInfos.length;
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
			//just start the game
			//test:
			getView().closeModal();
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

		//get the AI they picked from the spinner
		String selectedAI = getView().getSelectedAI();
		//build a AddAICmd object:
		AddAICommand addAICommand = new AddAICommand(selectedAI);

		System.out.println("PLAYERWAITINGCONT: adding AI type: " + selectedAI);

		//send addAI cmd to ClientFacade:

		if (ClientFacade.getInstance().addAI(addAICommand)) {
			//it worked
			System.out.println("PLAYERWAITINGCONT: AI Add successful! : " + selectedAI);
		}
		else {
			//it didn't work for some reason, show a message
			System.out.println("PLAYERWAITINGCONT: AI Add didn't work :(");
		}

		//get the new list of players from the server and call updateView()
		GameInfo[] newGameInfos = ClientFacade.getInstance().gamesList();
		updateView(newGameInfos);

	}


	/**
	 * 	called after AddAI() or MiniPoller happens  - gets new list of players from server, refreshes PlayerWaitingView
	 */
	private void updateView(GameInfo[] updatedGameInfos){

		//pull out the game we need
		GameInfo currGameToDisplay = updatedGameInfos[ClientUser.getInstance().getCurrentGameID()];
		System.out.println("\t\tCurrGameToDisplay: " + currGameToDisplay);

		//pull out PlayerInfo[] and use that to do PWV.setPlayers and update the view
		//GameInfo stores PlayerInfos in an ArrayList
		List<PlayerInfo> tempPIArrList = currGameToDisplay.getPlayers();
		System.out.println("PlayerInfos to display:" + tempPIArrList);

		PlayerInfo[] newPlayerInfosToDisplay = tempPIArrList.toArray(new PlayerInfo[tempPIArrList.size()]);
		//use this array to update the view

		//FOR TESTING ONLY------
		System.out.print(">PWC: updateView(): setting players with content: ");
		for (int i = 0; i < newPlayerInfosToDisplay.length; i++) {
			if (newPlayerInfosToDisplay[i] != null) {
				System.out.print(newPlayerInfosToDisplay[i].getName() + ", ");
			}
		} System.out.println();
		//----------------------


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
			//is there any way to make it not blink like that? it's pretty tacky lol
			if (getView().isModalShowing()){
				getView().closeModal();
			}
			getView().showModal();

		}
		else {
			//ok to be done picking players
			System.out.println(">PWC: updateView: currGame has enough players! ");
			miniPollTimer.cancel();  //stop the polling loop
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

	/**
	 * pulls out the relevant info from each Player object (coming directly from the updated model)
	 * and packages it into a PlayerInfo object so the PWV can do setPlayers()
	 *
	 * @param playersArr
	 * @return
	 */
	public PlayerInfo[] playersToPlayerInfos(Player[] playersArr){
		//convert this arrList into an array afterwards, to force PlayerInfo[] to be the right size (< 4 if there are < 4 players)
		ArrayList<PlayerInfo> tempArrList = new ArrayList<>();

		//for each Player object:
		//pull out their Name, Color, Index, and ID
		//package those into a new PlayerInfo object and add it to playerInfosArr
		for (int p = 0; p < playersArr.length; p++){
			if (playersArr[p] != null) {
				PlayerInfo newPI = new PlayerInfo();
				String newPIName = playersArr[p].getName();
				CatanColor newPIColor = playersArr[p].getColor();
				int newPIIndex = playersArr[p].getPlayerIndex();
				int newPIID = playersArr[p].getPlayerID();

				newPI.setName(newPIName);
				newPI.setColor(newPIColor);
				newPI.setPlayerIndex(newPIIndex);
				newPI.setId(newPIID);

				tempArrList.add(newPI);
			}
		}

		PlayerInfo[] playerInfosArr = tempArrList.toArray(new PlayerInfo[tempArrList.size()]);
		//now playerInfosArr should be the same size as there are REAL players - shouldn't have spots for null players
		//so setPlayers() will only have real stuff to work with

		return playerInfosArr;
	}

	//this is blank right now
	@Override
	public void update(Observable o, Object arg) {

		//System.out.println("PLAYERWAITINGCONT: Update called! +++++++++++++++++++++++++");

		//the model comes back here, updated every 2 seconds, via the Observable object
		//cast Observable into ClientModel here
		//ClientModel model = (ClientModel) o;

		//PWC should have its own personal ClientModel that gets updated every time this function happens
		//clientModel = model;
//		Player[] players = model.getPlayers();
//		for(int i = 0; i < 4; i++){
//			if(players[i].getName().equals(ClientUser.getInstance().getName())){
//				ClientUser.getInstance().setIndex(i);
//				break;
//			}
//		}

		//and does setPlayers() or whatever it needs to do to show the new boxes on the view:
	//	updateView();

	}





//////////////

	//TRY ANOTHER POLLER HERE
	//this poller should only be happening for the duration of PlayerWaitingController being visible.
	//need a start/stop function
	private class MiniPollerTask extends TimerTask {
		public void run() {
			//System.out.println("fetching new model");
			try {
				System.out.println("miniPoller: fetching gamesList: " + new Date().toString());
				fetchGamesList();
			}
			catch (Exception e) {
				System.out.println("miniPoller Exception!");
				e.printStackTrace();
			}
		}

		/**
		 * fetchNewModel() sends an update request to the saved proxy (currentProxy) via HTTP request.
		 * This function is called every 2 seconds when pollTimer tells it to.
		 */
		public void fetchGamesList() throws ClientException {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~PWC miniPoller~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			//don't get the model, just the gamesList!
			GameInfo[] newGameInfos = ClientFacade.getInstance().gamesList();

			updateView(newGameInfos);

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
		}
	}


}
