package client.join;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.base.IAction;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import client.misc.MessageView;
import client.utils.MessageUtils;
import exceptions.ClientException;
import shared.definitions.CatanColor;
import shared.model.commandmanager.game.GameCreateCommand;
import shared.model.commandmanager.game.GameJoinCommand;

import java.util.*;
import java.util.regex.Pattern;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private GameInfo joinThisGameInfo;
	private GameInfo[] currGamesList; //used to compare to incoming update - check if we really need to refresh the view
	private Timer miniPollTimer; //made public so SelectColorView can stop the timer
	//number of seconds to wait between requesting updates from the server
	private int pollInterval = 2;
	private final String invalidGameTitle = "The game title is invalid. Please choose a name between 3-24 alphanumeric characters";


	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {
		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {	
		
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		
		return selectColorView;
	}
	public void setSelectColorView(ISelectColorView selectColorView) {
		
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	public void setMessageView(IMessageView messageView) {
		
		this.messageView = messageView;
	}

	/**
	 * starts the JGCminiPoller and shows the JoinGame modal
	 */
	@Override
	public void start() {
		fetchInitialGamesList();
        startTimer();
		getJoinGameView().showModal();
	}

    /**
     * starts a new miniPoller
     */
	private void startTimer(){
        //start JGC's personal poller
        miniPollTimer = new Timer(true);
        miniPollTimer.scheduleAtFixedRate(new JoinGameMiniPoller(), 1, pollInterval * 1000);
    }

    /**
     * stops the miniPoller - this cannot be undone!! But you can just start a new one after.
     */
    private void stopTimer(){
        miniPollTimer.cancel();
    }

	@Override
	public void startCreateNewGame() {

        //get the previous game title out of there
        ((NewGameView) getNewGameView()).clearTitleField();

        getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}


    /**
     * This is where we get the text fields/bools off the View
     * and send the new game info to the server to actually create the new game.
     */
    @Override
    public void createNewGame() {

        System.out.println(">>JOINGAMECONTROLLER: createNewGame() called");

		//get the new game title and attributes from the view
        String newGameTitle = getNewGameView().getTitle();
        boolean newGameRandHexes = getNewGameView().getRandomlyPlaceHexes();
        boolean newGameRandNums = getNewGameView().getRandomlyPlaceNumbers();
        boolean newGameRandPorts = getNewGameView().getUseRandomPorts();

        //check new game title against the other game titles
        if (isGameNameAvailable(newGameTitle)) {

            System.out.println(">JOINGAMECONTROLLER: gametitle " + newGameTitle + " was available");

            //make the timer wait here  - synchronous thread safety business
            stopTimer();
            //////
            GameCreateCommand newGameCreateCmd = new GameCreateCommand(newGameTitle, newGameRandHexes, newGameRandNums, newGameRandPorts);
            	System.out.println(">JOINGAMECONTROLLER: final gameCreateCmd:" + newGameCreateCmd);

			GameInfo newGameCreatedInfo = ClientFacade.getInstance().gameCreate(newGameCreateCmd);

            joinThisGameInfo = newGameCreatedInfo;
			//join them to the game they just created using WHITE as the temporary/default color
            joinGameWithDefaultColor();

            //restart timer after commands are done going through
            startTimer();
            /////

            if (getNewGameView().isModalShowing()){			//TESTING FOR CMDLINE
				getNewGameView().closeModal();
			}

            //Refresh the list of games in the JoinGameView to include this new game
            GameInfo[] newGameInfoArr = ClientFacade.getInstance().gamesList();
            PlayerInfo currPlayerInfo = ClientUser.getInstance().getLocalPlayerInfo();
            this.getJoinGameView().setGames(newGameInfoArr, currPlayerInfo);
        }
        else{
            System.out.println(">JOINGAMECONTROLLER: gametitle " + newGameTitle + " was taken already");
        }
    }

    /**
     * Sends the user's chosen game title through three checks:
	 * 1) it contains only valid characters
	 * 2) it is a good length (1 and 2 are checked by gameTitleDelimiter pattern
	 * 3) it hasn't been used already
	 *
	 * If the title passes these three checks, returns true. else, returns false.
	 *
     * @param newGameName the name to be checked against all the other ones
     * @return true if the game name hasn't been used yet, false if it has been taken already
     */
    private boolean isGameNameAvailable(String newGameName){

		//first check if it's got only valid characters and if it is a decent length
		Pattern delim = Client.getInstance().getGameTitleDelimiter();
		if(!delim.matcher(newGameName).matches()){
			//there were some weird characters in there or the title was too long
			MessageUtils.showRejectMessage((MessageView)messageView, "Error", invalidGameTitle);
			return false;
		}

        //use titles in currGameList to see if newGameName is taken or not
        for (int g = 0; g < currGamesList.length; g++){
            String currGameName = currGamesList[g].getTitle();
            if (currGameName.equals(newGameName)){
                //show error message
				String msgContent = "The name " + newGameName + "  is already used. Please choose another!";
				MessageUtils.showRejectMessage((MessageView)messageView, "Error", msgContent);
                return false;
            }
        }

        return true;
    }

    /**
	 * This function just shows the SelectColorView
	 * Sets all the color buttons to enabled/disabled depending on who is joined to the game right now
     */
	@Override
	public void startJoinGame(GameInfo game) {

		System.out.println("JOINGAMECONTROLLER: startJoinGame called, game= " + game);

		joinThisGameInfo = game;

		//first set them all enabled to account for previous uses of this window:
		for (CatanColor color : CatanColor.values()){
			getSelectColorView().setColorEnabled(color, true);
		}

		//check if WHITE was used as a default color or not. If so, we should enable it to be selectable
		// unless someone else in the game actually chose WHITE as their color.

		//Set available color buttons here using SelectColorView.setColorEnable
		List<PlayerInfo> playersInGame = game.getPlayers();
		setSelectColorViewBtnColors(playersInGame);

		getSelectColorView().showModal();
	}

	//called every time the miniPoller says to do the update
	private void setSelectColorViewBtnColors(List<PlayerInfo> players){
	//	System.out.println(">>>>> setting available color buttons <<<<<<");
	//	System.out.println(">>>>> playersList = " + players);

		for (int i = 0; i < players.size(); i++){
			if (players.get(i).getColor() != null){	//someone else has taken this color already
				if (players.get(i).getColor() == CatanColor.WHITE){
					// for WHITE: if the user joined with default color, this will be taken already.
					// Enable it again IF they joined with default color (they created their own game):
					//System.out.println(">JoinedWithDefaultColor = " + ClientUser.getInstance().joinedWithDefaultColor());
					//System.out.println(">comparing " + players.get(i).getId() + " and " + ClientUser.getInstance().getId());

					//only enable WHITE if you created the game AND if it's you picking your own color again
					if (ClientUser.getInstance().joinedWithDefaultColor() && players.get(i).getId() == ClientUser.getInstance().getId()) {
						getSelectColorView().setColorEnabled(CatanColor.WHITE, true); //enable WHITE
					}
					else { //they did not create their own game, so WHITE should be disabled since someone is actually using it.
						getSelectColorView().setColorEnabled(CatanColor.WHITE, false); //disable WHITE
					}
				}
				else{ //someone in this game already chose that color -
					// set it disabled UNLESS you're the one who chose that color last time

					if (players.get(i).getId() != ClientUser.getInstance().getId()) {
						getSelectColorView().setColorEnabled(players.get(i).getColor(), false);
					}
				}
			}
		}
//		System.out.println(">>>>> <<<<<<<<");
	}


	@Override
	public void cancelJoinGame() {

		getJoinGameView().closeModal();
	}

	/**
	 * This is called right after the user makes a new game and they need to be added to that game they just made.
	 * We can't use the regular joinGame because it wants to go straight into the map after executing,
	 * but we need to go back to the JoinGameView (gameList).
	 */
	private void joinGameWithDefaultColor(){
		System.out.println("JOINGAMECONTROLLER: joinGameWithDefaultColor called");

		int desiredGameID = joinThisGameInfo.getId();

		//create joinGameCommand
		GameJoinCommand gameJoinCommand = new GameJoinCommand(desiredGameID, CatanColor.WHITE);  //TODO: maybe we should use NULL instead

		//send it to ClientFacade's OTHER join function:
		if (ClientFacade.getInstance().gameJoinWithDefaultColor(gameJoinCommand)) {
			//save the id of the game they just joined to ClientUser singleton for later use
			ClientUser.getInstance().setCurrentGameID(desiredGameID);
			//MARK THAT THEY WENT THROUGH THIS METHOD
			ClientUser.getInstance().setJoinedWithDefaultColor(true);
		}
		else {
			//print - it didn't work
			System.out.println("\t>JOINGAMECONTROLLER: ClientFacade.gameJoin didn't work! :( ");
		}
	}

	@Override
	public void joinGame(CatanColor color) {

		System.out.println("JOINGAMECONTROLLER: joinGame called, selectedColor= " + color);

		//stop timer here, this view is done
		miniPollTimer.cancel();

		// If join succeeded, send the server a GameJoin Cmd object:

		//ask ClientFacade to do JoinGameCommand
		int desiredGameID = joinThisGameInfo.getId();

		//create joinGameCommand
		GameJoinCommand gameJoinCommand = new GameJoinCommand(desiredGameID, color);

		//send command to ClientFacade
		if (ClientFacade.getInstance().gameJoin(gameJoinCommand)) {
			System.out.println(">JOINGAMECONTROLLER: ClientFacade.gameJoin said TRUE");

			//ok to save current game info for the game they just joined to ClientUser singleton for later use
			ClientUser.getInstance().setCurrentGameID(desiredGameID);
			ClientUser.getInstance().setJoinedWithDefaultColor(false);

			//Saving the CurrAddedGame again to the CU so it reflects what color they just
			// picked instead of the default WHITE
			GameInfo[] currGamesArr = ClientFacade.getInstance().gamesList();
			GameInfo currAddedGame = currGamesArr[desiredGameID];
			ClientUser.getInstance().setCurrentAddedGameInfo(currAddedGame);

			// If join succeeded
			if (getSelectColorView().isModalShowing()){
				getSelectColorView().closeModal();
			}
			if (getJoinGameView().isModalShowing()) {
				getJoinGameView().closeModal();
			}
			joinAction.execute();

				//one more time just to make sure?t
				if (getSelectColorView().isModalShowing()){
					getSelectColorView().closeModal();
				}
				if (getJoinGameView().isModalShowing()) {
					getJoinGameView().closeModal();
				}

		}
		else{
			System.out.println(">JOINGAMECONTROLLER: ClientFacade.gameJoin didn't work! :( ");
		}
	}

	/**
	 * Used to populate the games list in JoinGameView right when the modal starts showing
	 * call this right when JoinGameController starts!
	 */
	private void fetchInitialGamesList(){

		currGamesList = ClientFacade.getInstance().gamesList();

		PlayerInfo localPlayerInfoSoFar = new PlayerInfo();
		localPlayerInfoSoFar.setName(ClientUser.getInstance().getName());
		localPlayerInfoSoFar.setId(ClientUser.getInstance().getId());
		getJoinGameView().setGames(currGamesList, localPlayerInfoSoFar);
	}

	//UNUSED
	@Override
	public void update(Observable o, Object arg) {
		// just make a miniPoller for JoinGameController!
		//don't bother with this update() crap
	}



	// GETTERS AND SETTERS

	public GameInfo[] getCurrGamesList() {
		return currGamesList;
	}

	private void setCurrGamesList(GameInfo[] currGamesList) {
		this.currGamesList = currGamesList;
	}


//////////////

	/**
	 * JoinGameMiniPoller is JoinGameController's personal poller that gets an updated list of games every 2 sec.
	 * The big/main poller for this program wasn't working too well for this part so Sierra made a new poller here.
	 * This TimerTask is started upon JoinGameController.start() and stopped right when the user finishes picking a color.
	 */
	private class JoinGameMiniPoller extends TimerTask {
		public void run() {
			try {
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^JGC miniPoller^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				System.out.println("JGminiPoller: fetching gamesList: " + new Date().toString());
				fetchGamesList();
			}
			catch (Exception e) {
				System.out.println("JGminiPoller Exception!");
				e.printStackTrace();
			}
		}

		/**
		 * FetchGamesList() sends an gamesList update request to the saved proxy (currentProxy) via HTTP request.
		 * This function is called every 2 seconds when pollTimer tells it to.
		 */
		public void fetchGamesList() throws ClientException {

			//don't get the model, just the gamesList!
			GameInfo[] newGameList = ClientFacade.getInstance().gamesList();
			//update the View with the new GameList info
			PlayerInfo currPlayerInfo = ClientUser.getInstance().getLocalPlayerInfo();

			System.out.println("\t\tJCGminiPoller: just got new gamesList, size= " + newGameList.length);

			//Check if this view needs to update and redraw
			if (isViewUpdateNeeded(newGameList)){
				//DO VIEW UPDATE

				System.out.println(">JCGminiPoller: DOING VIEW UPDATE");

					System.out.println("\t\tJCGminiPoller: currGamesList size= " + currGamesList.length);
					System.out.println("\t\tJCGminiPoller: new games found in gameList, size= " + newGameList);
				getJoinGameView().setGames(newGameList, currPlayerInfo);
				setCurrGamesList(newGameList);

				// closing/reopening the view to refresh it
				//but not if the other views are open, or else JoinGameView will awkwardly pop up again over them
				if (!getSelectColorView().isModalShowing() && !getNewGameView().isModalShowing()){

					if (getJoinGameView().isModalShowing()){
						getJoinGameView().closeModal();
					}
					getJoinGameView().showModal();
				}

				//update the available colors to choose
				else if (getSelectColorView().isModalShowing()){
					//they are trying to pick a color, so update which ones they can choose from
					setSelectColorViewBtnColors(newGameList[Client.getInstance().getCurrentJoinGameCandidate()].getPlayers());
				}

			}
			else{    //DON'T DO VIEW UPDATE
				System.out.println("\t\tJCGminiPoller: currGamesList size= " + currGamesList.length);
				System.out.println("\t\tJCGminiPoller: no change in gameList or playersLists");
				System.out.println(">JCGminiPoller: SKIPPING VIEW UPDATE");
			}

			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^");
		}

		/**
		 * Checks the new GameList against the existing one to see if the gameList or the PlayerLists have been updated.
		 *
		 * @param newGameList
		 * @return true if a list has been updated, false otherwise
		 */
		public boolean isViewUpdateNeeded(GameInfo[] newGameList){

			System.out.println(">isVUN: comparing newGL size " + newGameList.length + " \t to oldGL size " + currGamesList.length);

			//if an entire new game was added
			if (newGameList.length > currGamesList.length){
				return true;
			}

			//check each PlayerList in the gamesList
			for (int g = 0; g < newGameList.length; g++){
				List<PlayerInfo> newPlayersList= newGameList[g].getPlayers();
				List<PlayerInfo> existingPlayerList = currGamesList[g].getPlayers();

				System.out.println(">isVUN: comparing newPL size " + newPlayersList.size() + "\t to oldPL size " + existingPlayerList.size());


				if (newPlayersList.size() != existingPlayerList.size()){
					return true;
				}
			}

			return false;
		}
	}
}


