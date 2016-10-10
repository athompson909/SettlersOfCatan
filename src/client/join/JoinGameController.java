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

import java.util.Observable;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;

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

        //I think this is where we need to put the functionality for getting the text fields/bools off the View
        // and sending the info to the server to actually create the new game


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


        getNewGameView().closeModal();

        //Refresh the list of games in the JoinGameView to include this new game
        //I would just tell JoinGameView to do fetchListOfGamesFromServer() and then setGames(), but
        //JoinGameController only has access to the functions that are in JoinGameView's interface class,
        // and fetchList...() is not in that interface. :(
        GameInfo[] newGameInfoArr = ClientFacade.getInstance().gamesList();
        PlayerInfo currPlayerInfo = ClientUser.getInstance().getLocalPlayerInfo();
        this.getJoinGameView().setGames(newGameInfoArr, currPlayerInfo);
    }
	@Override
	public void startJoinGame(GameInfo game) {

		//add the user to the game they just created using the GameInfo object here

		System.out.println(">JOINGAMECONTROLLER: startJoinGame called, gameInfo= " + game);

		getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
	
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		
		// If join succeeded
		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		joinAction.execute();
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}

