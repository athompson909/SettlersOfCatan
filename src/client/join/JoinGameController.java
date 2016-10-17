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
	//lightweight personal poller for PlayerWaitingController
	private boolean savedInitialGamesList = false; //whether we got the initial gameslist to start with
	private Timer miniPollTimer; //made public so SelectColorView can stop the timer
	//number of seconds to wait between requesting updates from the server
	private int pollInterval = 2;

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
     *
     */
	private void startTimer(){
        //start JGC's personal poller
        miniPollTimer = new Timer(true);//true tells the program to end this thread if it is the only one left so we cand exit the program
        miniPollTimer.scheduleAtFixedRate(new JoinGameMiniPoller(), 1, pollInterval * 1000);
    }

    /**
     *
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

        //pull game title, bools off of the view
        //build GameCreateCommand object, send it to ClientFacade
        //server/clientFacade will return a GameInfo object
        // we need to give that GameInfo object to the JoinGameView's GameInfo[] field
        String newGameTitle = getNewGameView().getTitle();
        boolean newGameRandHexes = getNewGameView().getRandomlyPlaceHexes();
        boolean newGameRandNums = getNewGameView().getRandomlyPlaceNumbers();
        boolean newGameRandPorts = getNewGameView().getUseRandomPorts();

        //check new game title against the other game titles
        if (isGameNameAvailable(newGameTitle)) {

            System.out.println(">JOINGAMECONTROLLER: gametitle " + newGameTitle + " was available");

            //make the timer wait here
            stopTimer();
            //////
            GameCreateCommand newGameCreateCmd = new GameCreateCommand(newGameTitle, newGameRandHexes, newGameRandNums, newGameRandPorts);
            GameInfo newGameCreatedInfo = ClientFacade.getInstance().gameCreate(newGameCreateCmd);
            //restart timer after command is done going through
            startTimer();
            /////

            System.out.println(">JOINGAMECONTROLLER: just created game " + newGameCreatedInfo);

            //after you create a game, tell the server to add you to that same game, but don't go to SelectColorView yet.
            //Just go back to JoinGameView.

            //JOIN THE NEW GAME WITH DEFAULT COLOR WHITE
            joinThisGameInfo = newGameCreatedInfo;
            joinGameWithDefaultColor();

            getNewGameView().closeModal();

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
    public boolean isGameNameAvailable(String newGameName){

		//first check if it's got only valid characters and if it is a decent length
		Pattern delim = Client.getInstance().getGameTitleDelimiter();
		if(!delim.matcher(newGameName).matches()){
			System.out.println("\t>>>isGNA: name " + newGameName + " was found to be invalid!");
			//there were some weird characters in there or the title was too long
			showGameNameInvalidView();
			return false;
		}

        //use titles in currGameList to see if newGameName is taken or not
        for (int g = 0; g < currGamesList.length; g++){
            String currGameName = currGamesList[g].getTitle();
            if (currGameName.equals(newGameName)){
                //show error message
                showGameNameTakenView(newGameName);
                System.out.println("\t>>>isGNA: name " + newGameName + " found in currGamesList!");
                return false;
            }
        }

        System.out.println("\t>>>isGNA: name " + newGameName + " NOT found in currGamesList!");
        return true;
    }

    /**
     * displays a little error message to say that you lack originality
     * @param takenGameName
     */
    private void showGameNameTakenView(String takenGameName) {
        MessageView gameNameTakenView = (MessageView) messageView;
        String msgTitle = "Error";
        String msgContent = "The name " + takenGameName + "  is already used. Please choose another!";

        gameNameTakenView.setTitle(msgTitle, 220);
        gameNameTakenView.setMessage(msgContent, 220);
        gameNameTakenView.setCloseButton("Ok");
        gameNameTakenView.showModal();

        // clear the field in NewGameView
        ((NewGameView) getNewGameView()).clearTitleField();
    }

	/**
	 * displays a little error message to say that you suck at picking game titles
	 */
	private void showGameNameInvalidView() {
		MessageView gameNameTakenView = (MessageView) messageView;
		String msgTitle = "Error";
		String msgContent = "The game title is invalid. Please choose a name between 3-24 alphanumeric characters";

		gameNameTakenView.setTitle(msgTitle, 220);
		gameNameTakenView.setMessage(msgContent, 220);
		gameNameTakenView.setCloseButton("Ok");
		gameNameTakenView.showModal();

		// clear the field in NewGameView
		((NewGameView) getNewGameView()).clearTitleField();
	}

    /**
	 * This function just shows the SelectColorView
     */
	@Override
	public void startJoinGame(GameInfo game) {

		//WE CAN'T JOIN A GAME UNTIL WE'VE PICKED A COLOR

		System.out.println("JOINGAMECONTROLLER: startJoinGame called, game= " + game);

		joinThisGameInfo = game;  //TEST

		//first set them all enabled to account for previous uses of this window:
		for (CatanColor color : CatanColor.values()){
			getSelectColorView().setColorEnabled(color, true);
		}

		//check if WHITE was used as a default color or not. If so, we should enable it to be selectable
		// unless someone else in the game actually chose WHITE as their color.

		//Set available color buttons here using SelectColorView.setColorEnable
		List<PlayerInfo> playersInGame = game.getPlayers();
		for (int i = 0; i < playersInGame.size(); i++){
			if (playersInGame.get(i).getColor() != null){	//someone else has taken this color already
				if (playersInGame.get(i).getColor() == CatanColor.WHITE){
					// for WHITE: if the user joined with default color, this will be taken already.
					// Enable it again IF they joined with default color (they created their own game):
					System.out.println(">JoinedWithDefaultColor = " + ClientUser.getInstance().joinedWithDefaultColor());
					//System.out.println("comparing " + playersInGame.get(i).getId() + " and " + ClientUser.getInstance().getId());

					//only enable WHITE if you either created the game AND if it's you picking your own color again
					if (ClientUser.getInstance().joinedWithDefaultColor() && playersInGame.get(i).getId() == ClientUser.getInstance().getId()) {
						getSelectColorView().setColorEnabled(CatanColor.WHITE, true); //enable WHITE
					}
					else { //they did not create their own game, so WHITE should be disabled since someone is actually using it.
						getSelectColorView().setColorEnabled(CatanColor.WHITE, false); //disable WHITE
					}
				}
				else{ //someone in this game really chose that color
					//set this color button to be disabled
					//UNLESS ITS YOU PICKING A NEW COLOR FOR YOURSELF
					//don't disable the button if this color was previously chosen by you
				//	System.out.println("comparing " + playersInGame.get(i).getId() + " and " + ClientUser.getInstance().getId());

					if (playersInGame.get(i).getId() != ClientUser.getInstance().getId()) {
						getSelectColorView().setColorEnabled(playersInGame.get(i).getColor(), false);
					}
				}
			}
				//else, they're all enabled
		}

		getSelectColorView().showModal();

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
	public void joinGameWithDefaultColor(){
		System.out.println("JOINGAMECONTROLLER: joinGameWithDefaultColor called");

		int desiredGameID = joinThisGameInfo.getId();

		//create joinGameCommand
		GameJoinCommand gameJoinCommand = new GameJoinCommand(desiredGameID, CatanColor.WHITE);

		//send it to ClientFacade's OTHER join function:
		if (ClientFacade.getInstance().gameJoinWithDefaultColor(gameJoinCommand)) {
			//print - it worked
			System.out.println("\t>JOINGAMECONTROLLER: join ClientFacade.gameJoin said TRUE");

			//ok to save the id of the game they just joined to ClientUser singleton for later use
			ClientUser.getInstance().setCurrentGameID(desiredGameID);
			//MARK THAT THEY WENT THROUGH THIS METHOD
			ClientUser.getInstance().setJoinedWithDefaultColor(true);
		}
		else{
			//print - it didn't work
			System.out.println("\t>JOINGAMECONTROLLER: ClientFacade.gameJoin didn't work! :( ");
		}

		//user should now be added to the game they just made, with the temp/default color WHITE.
		//We should be going back to the GameList view right now.
	}

	@Override
	public void joinGame(CatanColor color) {

		System.out.println("JOINGAMECONTROLLER: joinGame called, selectedColor= " + color);

		//stop timer here, this view is done
		miniPollTimer.cancel();

		// If join succeeded, send the server a GameJoin Cmd object:

		//ask ClientFacade to do JoinGameCommand
		//CatanColor userColor = ClientUser.getInstance().getColor();  //TEST - use the color passed in here
		int desiredGameID = joinThisGameInfo.getId();

		//create joinGameCommand
		GameJoinCommand gameJoinCommand = new GameJoinCommand(desiredGameID, color);

		//send command to ClientFacade
		if (ClientFacade.getInstance().gameJoin(gameJoinCommand)) {
			System.out.println(">JOINGAMECONTROLLER: ClientFacade.gameJoin said TRUE");

			//ok to save current game info for the game they just joined to ClientUser singleton for later use
			ClientUser.getInstance().setCurrentGameID(desiredGameID);
			ClientUser.getInstance().setJoinedWithDefaultColor(false);

			//Saving the CurrAddedGame again to the CU so it reflects what color they just picked instead of the default WHITE
			//asking the server for the gameList again is easier than going way down into CU's currAddedGame and
			//changing the color value for one player in the PlayerInfo array.
			GameInfo[] currGamesArr = ClientFacade.getInstance().gamesList();
			GameInfo currAddedGame = currGamesArr[desiredGameID];
			ClientUser.getInstance().setCurrentAddedGameInfo(currAddedGame);
		}
		else{
			//print - it didn't work
			System.out.println(">JOINGAMECONTROLLER: ClientFacade.gameJoin didn't work! :( ");
		}

		// If join succeeded

		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		joinAction.execute();
	}

	/**
	 * Used to populate the games list in JoinGameView right when the modal starts showing
	 * call this right when JoinGameController starts!
	 */
	public void fetchInitialGamesList(){

		currGamesList = ClientFacade.getInstance().gamesList();
		System.out.println("\t\tJCG: fetchInitialGL: just got FIRST gamesList, size= " + currGamesList.length);

		PlayerInfo localPlayerInfoSoFar = new PlayerInfo();
		localPlayerInfoSoFar.setName(ClientUser.getInstance().getName());
		localPlayerInfoSoFar.setId(ClientUser.getInstance().getId());
		getJoinGameView().setGames(currGamesList, localPlayerInfoSoFar);

		//showing the modal for the first time right after this complete should show with the initial gamelist data
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

	public void setCurrGamesList(GameInfo[] currGamesList) {
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
//
//			//FOR TESTING ONLY------
//			for (int i = 0; i < newGameInfos.length; i++) {
//				if (newGameInfos[i] != null) {
//					System.out.println(newGameInfos[i]);
//				}
//			} System.out.println();
//			//----------------------

			//only setGames() and refresh the view if there was actually a change
			//compare currGamesList size to newGamesInfos size
			if (newGameList.length > currGamesList.length){  //DO VIEW UPDATE
				//ok to do update
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
			}
			else{    //DON'T DO VIEW UPDATE
				System.out.println("\t\tJCGminiPoller: currGamesList size= " + currGamesList.length);
				System.out.println("\t\tJCGminiPoller: no change in gameList");
			}


			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^");
		}
	}


}


