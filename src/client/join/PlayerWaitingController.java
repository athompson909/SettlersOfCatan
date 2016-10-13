package client.join;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import client.misc.MessageView;
import shared.definitions.CatanColor;
import shared.model.ClientModel;
import shared.model.commandmanager.game.AddAICommand;
import shared.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	//used to show messages
	private IMessageView messageView;

	//used to pull out the list of players in each game
	private ClientModel clientModel;

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
		// we should probably be getting it from the ClientModel>Players. VIA NOTIFYOBSERVERS!
		// yes we need to do that because if we're not updating the PlayerWaitingView based on the Players in the model
		// the view will not be updated with the players that joined your game from different computers.

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
			//there are still more players needed for this game, so DO show the PlayerWaitingView
			System.out.println("PLAYERWAITINGCONTROLLER: start(): Showing the PlayerWaitingView!");
			getView().showModal();
		}
		else if (numPlayersInGame == 4){
			//ok to start the game, we have all the players
			//so DON'T show the PlayerWaitingView
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

		//maybe special request the model from the server right here?
		//ClientFacade.getInstance().gameModelVersion(); //try forcing model update
		//updateView();

	}

	//called after AddAI() or Update() happens  - gets new list of players from server, refreshes view
	private void updateView(){

		//THIS NEEDS TO ASK MODEL FOR PLAYERS INSTEAD ****
//		//ask server for gamelist:
//		GameInfo[] newGameInfoArr = ClientFacade.getInstance().gamesList();
//		//get the list of players from the server response using the current gameID
//		GameInfo currGameInfo = newGameInfoArr[ClientUser.getInstance().getCurrentGameID()];
//		//use that list of players to do setPlayers() here
//		List<PlayerInfo> newPlayerInfoList = currGameInfo.getPlayers();
//		//turn it into an array
//		PlayerInfo[] setThesePlayerInfos = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);
//		//give that array of PlayerInfos to setPlayers()

		//this is where PWC pulls out the updated list of Players for ClientUser's currAddedGameID
		Player[] updatedPlayersArr = clientModel.getPlayers(); //I'm pretty sure that this will be unique for each game. Check!
		//convert the Player[] to PlayerInfo[] by pulling out the relevant info from each Player and
		//packaging it into a PlayerInfo object instead.
		PlayerInfo[] updatePlayerInfosArr = playersToPlayerInfos(updatedPlayersArr);



		getView().setPlayers(updatePlayerInfosArr);
		int numPlayersInGame = updatePlayerInfosArr.length;
		//-----------------

		//update PlayerWaitingView with newly added players IF THERE ARE LESS THAN 4 PLAYERS:
		if (numPlayersInGame < 4){
			// we still need more players - go again
			System.out.println(">PLAYERWAITINGCONT: addAI: currGame #players < 4");

			//try just closing the modal/reopening it:   - TA
			if (getView().isModalShowing()){
				getView().closeModal();
			}
			getView().showModal();

		}
		else{
			//ok to be done picking players
			System.out.println(">PLAYERWAITINGCONT: addAI: currGame has enough players! ");
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

				//playerInfosArr[p] = newPI;
				tempArrList.add(newPI);
			}
		}

		PlayerInfo[] playerInfosArr = tempArrList.toArray(new PlayerInfo[tempArrList.size()]);
		//now playerInfosArr should be the same size as there are REAL players - shouldn't have spots for null players
		//so setPlayers() will only have real stuff to work with

		return playerInfosArr;
	}

	@Override
	public void update(Observable o, Object arg) {

		System.out.println("PLAYERWAITINGCONT: Update called! +++++++++++++++++++++++++");

		//the model comes back here, updated every 2 seconds, via the Observable object
		//cast Observable into ClientModel here
		ClientModel model = (ClientModel) o;

		//PWC should have its own personal ClientModel that gets updated every time this function happens
		clientModel = model;
		int newLocalPlayerIndex = model.getCurrentPlayer().getPlayerIndex();// try this
		ClientUser.getInstance().setIndex(newLocalPlayerIndex);


//		//this is where PWC pulls out the updated list of Players for ClientUser's currAddedGameID
//		Player[] updatedPlayersArr = clientModel.getPlayers(); //I'm pretty sure that this will be unique for each game. Check!
//		//convert the Player[] to PlayerInfo[] by pulling out the relevant info from each Player and
//		//packaging it into a PlayerInfo object instead.
//		PlayerInfo[] updatePlayerInfosArr = playersToPlayerInfos(updatedPlayersArr);

		//and does setPlayers() or whatever it needs to do to show the new boxes on the view:
		updateView();



		/*
			My issue right now is that when you add an AI player, and presumably a real player too,
			the server doesn't count that as a model update, and so it doesn't increment the model version number.
			This means that ClientUpdateManager doesn't notifyObservers since there was "no change".
			So PlayerWaitingView doesn't get the new list of players added to the game.
			How can I give PlayerWaitingView an updated list of Players from the server every 2 sec?
			//I tried forcing an observer network update so the updated Playerslist would be sent through to PWV,
			but for some reason it just closes the PWV modal after update() is called.
		 */
		/*
			We used to have PWV update just whenever you click the AddAI button, at which point it would go
			ask the server for the GamesList, but then we remembered that we need to be asking the constantly updating
			ClientModel for the list of players, to be sure that we update PWV every 2 sec to account for real players
			joining from around the server at any time.
		 */
		/*
			I may want to ask the TAs how they got the PlayerList to update every 2 sec even when the server
			doesn't count that as a model "update"... maybe they just don't even check the version numbers with the
			new model coming back, and they just send the update around the observer network every time anyways?
		 */

		/*
			**UPDATE: when looking at the server output while running the demo client, it shows games/list
			* being called every 2 sec. That means the Poller just calls /games/List UNTIL you
			* finish picking a color and join the game for real. So I still need to ask the TAs about the
			* player list updating from the model during PlayerWaitingView, but for JoinGameView we need to
			* add a state or something to the Poller to tell it to do /games/list until the end of SelectColorView.
		 */
	}



}
