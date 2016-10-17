package client;

import client.map.mapStates.MapState;

import client.turntracker.IState;
import client.turntracker.ITurnTrackerState;
import client.turntracker.WaitingState;
import shared.definitions.State;
import shared.model.ClientModel;
import shared.model.commandmanager.CommandManager;

import java.util.regex.Pattern;

/**
 * Contains the command manager, client model, state, and starts the server poller
 *
 * Created by Alise on 9/20/2016.
 */
public class Client {

    private boolean startGame;
    private CommandManager commandManager;

    private ClientModel clientModel;
    private State gameState = State.WAITING;
    private ITurnTrackerState state = new WaitingState();
    private IServerProxy serverProxy;
    private ServerPoller serverPoller;


    //TODO: change unDelim to be min of 3, and pwDelim to be min of 5
    //According to the TAs tooltips: username must be 3-7 chars long, and can include letters, numbers, underscore, or dash
    private Pattern usernameDelimiter = Pattern.compile("([A-z]|[0-9]|_|-){1,7}");
    //According to the TAs tooltips: username must be 5-16 chars long, and can include letters, numbers, underscore, or dash
    private Pattern passwordDelimiter = Pattern.compile("([A-z]|[0-9]|_|-){1,16}");
    //I just picked this length
    private Pattern gameTitleDelimiter = Pattern.compile("([A-z]|[0-9]|_|-){1,24}");



    private static Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

    private Client() {
        commandManager = new CommandManager();
        clientModel = new ClientModel(0);
        ClientFacade.getInstance().setValues(new ServerProxy(), clientModel);
        serverPoller = null;
        startGame = false;
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public void setServerPoller() {
        this.serverPoller = new ServerPoller();
    }
    //The state will get updated via the updateTurnTracker method
    /*  public void updateGameState(){
        state = state.update(clientModel.getTurnTracker());
    } */
    public void setGameState(State newState) {gameState = newState;}
    public State getGameState(){
        return gameState;
    }
    public ITurnTrackerState getState() {return state;}
    public void updateState() {
        state = state.update(clientModel.getTurnTracker());
        gameState = state.toEnum();
    }

    public Pattern getUsernameDelimiter() {
        return usernameDelimiter;
    }

    public Pattern getPasswordDelimiter() {
        return passwordDelimiter;
    }

    public Pattern getGameTitleDelimiter() {
        return gameTitleDelimiter;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * creates a new instance of server poller which then starts the server poller task
     */
    public void startServerPoller() {
        serverPoller = new ServerPoller();
    }
    public boolean getStartGame() {return startGame;}
    public void setStartGame(boolean set) {startGame = set;}
}
