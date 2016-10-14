package client.join;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.base.IAction;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.IMessageView;
import shared.definitions.CatanColor;
import shared.model.commandmanager.game.GameCreateCommand;
import shared.model.commandmanager.game.GameJoinCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private GameInfo joinThisGameInfo;  //TEST

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

	@Override
	public void start() {
		
		getJoinGameView().showModal();
	}

	@Override
	public void startCreateNewGame() {

		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}


    @Override
    public void createNewGame() {

        System.out.println(">>JOINGAMECONTROLLER: createNewGame() called");

        //This is where we need to put the functionality for getting the text fields/bools off the View
        // and sending the info to the server to actually create the new game

		//TODO: check if the game they're trying to make has the same title as a previous game?

        //pull game title, bools off of the view
        //build GameCreateCommand object, send it to ClientFacade
        //server/clientFacade will return a GameInfo object
        // we need to give that GameInfo object to the JoinGameView's GameInfo[] field
        //either that or just reinitialize the JoinGameView? try both
        String newGameTitle = getNewGameView().getTitle();
        boolean newGameRandHexes = getNewGameView().getRandomlyPlaceHexes();
        boolean newGameRandNums = getNewGameView().getRandomlyPlaceNumbers();
        boolean newGameRandPorts = getNewGameView().getUseRandomPorts();

        GameCreateCommand newGameCreateCmd = new GameCreateCommand(newGameTitle, newGameRandHexes, newGameRandNums, newGameRandPorts);
        GameInfo newGameCreatedInfo = ClientFacade.getInstance().gameCreate(newGameCreateCmd);

		System.out.println(">JOINGAMECONTROLLER: just created game " + newGameCreatedInfo);

		//TODO: add user to the game they just created!!
		//after you create a game, tell the server to add you to that same game, but don't go to SelectColorView yet.
		//Just go back to JoinGameView.
		//How can we tell the server to add you to the game you just created if you don't have a color yet?
		//maybe just have a default/temp color? like white or something?
		//that could work because on the demo it adds you to the game you just created and then you can ReJoin it.
		//when you ReJoin it, you pick a color that could overwrite the temp/default color we added you with.
		//I just checked, you can join a game twice with different colors and it will overwrite the previous color!
		//so I'm going to default their join color to WHITE, and when they go back and choose ReJoin,
		//it'll let them pick another color and overwrite the default one.

		//TRY: JOINING THE NEW GAME WITH DEFAULT COLOR WHITE
		joinThisGameInfo = newGameCreatedInfo;
		joinGameWithDefaultColor();
		//joinGame(CatanColor.WHITE);  //I think we need to make a different JoinGame function for when you're joining
		// a game you just created, because JoinGame wants to go right in to color select/some later step
		//and skip going back to the GameList view.


        getNewGameView().closeModal();

        //Refresh the list of games in the JoinGameView to include this new game
		//		I THINK THIS NEEDS TO HAPPEN VIA POLLER - update gamelist every 2 sec - ask TAs
        GameInfo[] newGameInfoArr = ClientFacade.getInstance().gamesList();
        PlayerInfo currPlayerInfo = ClientUser.getInstance().getLocalPlayerInfo();
        this.getJoinGameView().setGames(newGameInfoArr, currPlayerInfo);
    }

    /**
	 * This function just shows the SelectColorView
     */
	@Override
	public void startJoinGame(GameInfo game) {

		//WE CAN'T JOIN A GAME UNTIL WE'VE PICKED A COLOR
		//fyi you select a color if you're "re-joining" too

		System.out.println("JOINGAMECONTROLLER: startJoinGame called, game= " + game);

		joinThisGameInfo = game;  //TEST

		//first set them all enabled to account for previous uses of this window:
		for (CatanColor color : CatanColor.values()){
			getSelectColorView().setColorEnabled(color, true);
		}


		//the default color is white, and we use that to "join" a game before picking a color,
		// so selectcolorview is showing that white is taken.
		//maybe set a bool inside ClientUser, joinedWithDefaultColor, that we check here to see if we should enable white or not.
		//but still check that no one has already picked white who's actually in the game.

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
					System.out.println("comparing " + playersInGame.get(i).getId() + " and " + ClientUser.getInstance().getId());

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
					System.out.println("comparing " + playersInGame.get(i).getId() + " and " + ClientUser.getInstance().getId());

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

			//don't think we need these yet
				//GameInfo[] currGamesArr = ClientFacade.getInstance().gamesList();
				//GameInfo currAddedGame = currGamesArr[desiredGameID];
				//ClientUser.getInstance().setCurrentAddedGame(currAddedGame);

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

		// If join succeeded, send the server a GameJoin Cmd object:

		//add the user to the game they just picked using the GameInfo object here

		//ask ClientFacade to do JoinGameCommand
		//CatanColor userColor = ClientUser.getInstance().getColor();  //TEST - use the color passed in here
		int desiredGameID = joinThisGameInfo.getId();

		//create joinGameCommand
		GameJoinCommand gameJoinCommand = new GameJoinCommand(desiredGameID, color);

		//send it to ClientFacade
		if (ClientFacade.getInstance().gameJoin(gameJoinCommand)) {
			//print - it worked
			System.out.println(">JOINGAMECONTROLLER: ClientFacade.gameJoin said TRUE");

			//ok to save the id of the game they just joined to ClientUser singleton for later use
			ClientUser.getInstance().setCurrentGameID(desiredGameID);
			//TEST
			ClientUser.getInstance().setJoinedWithDefaultColor(false);


			//TESTING  - trying to pass the currAddedGameInfo item into PlayerWaitingController by saving it in CU
			GameInfo[] currGamesArr = ClientFacade.getInstance().gamesList();
			GameInfo currAddedGame = currGamesArr[desiredGameID];
			ClientUser.getInstance().setCurrentAddedGame(currAddedGame);

		}
		else{
			//print - it didn't work
			System.out.println(">JOINGAMECONTROLLER: ClientFacade.gameJoin didn't work! :( ");
		}
		//user should now be added to the game they clicked on.

		// If join succeeded
		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		joinAction.execute();
	}

	@Override
	public void update(Observable o, Object arg) {
		//TODO: update gamelist from model
		//actually we don't have the model until PlayerWaitingView is happening.
		//I think JoinGamecontroller is going to be on its own personal observer pattern because it only wants
		//the gamesList from the server.

		// just make a miniPoller for JoinGameController!

	}

}

