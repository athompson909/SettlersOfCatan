package client;

import client.turntracker.TurnTrackerState;
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
    //private String hostNumber = "";
    //private String portNumber = "";

    private ClientModel clientModel;
    private State gameState = State.WAITING;
    private TurnTrackerState state = new WaitingState();
    private IServerProxy serverProxy;
    private ServerPoller serverPoller;
    private int currentJoinGameCandidate = -1;


    //According to the TAs tooltips: username must be 3-7 chars long, and can include letters, numbers, underscore, or dash
    private Pattern usernameDelimiter = Pattern.compile("[a-zA-Z0-9-_\\s]{3,7}");
    //According to the TAs tooltips: username must be 5-16 chars long, and can include letters, numbers, underscore, or dash
    private Pattern passwordDelimiter = Pattern.compile("[a-zA-Z0-9-_\\s]{5,16}");
    //I just picked this length
    private Pattern gameTitleDelimiter = Pattern.compile("[a-zA-Z0-9-_\\s]{3,24}");



    private static Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

    private Client() {
        commandManager = new CommandManager();
        clientModel = new ClientModel(0);
        serverProxy = new ServerProxy();
        ClientFacade.getInstance().setValues(serverProxy, clientModel);
        serverPoller = null;
        startGame = false;
    }

    public void setServerHostPort(String host, String port) {
        System.out.println(">>CLIENT: setServerHostPost: host#= " + host + ", port#= " + port);

        serverProxy.setHost(host);
        serverProxy.setPort(port);
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public void setNewClientModel(){
        this.clientModel = new ClientModel(0);
    }

    public void setServerPoller() {
        this.serverPoller = new ServerPoller();
    }
    //The state will get updated via the updateTurnTracker method
    /*  public void updateGameState(){
        state = state.update(clientModel.getTurnTracker());
    } */
    public void setGameState(State newState) {gameState = newState;}

    public State getGameState() {return gameState;}

    public TurnTrackerState getState() {return state;}

    public void updateState() {
        state = state.update(clientModel.getTurnTracker());
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
     */;
    public void startServerPoller() {
        serverPoller = new ServerPoller();
    }

    public void stopServerPoller(){
        serverPoller.getPollTimer().cancel();
    }

    public int getCurrentJoinGameCandidate() {
        return currentJoinGameCandidate;
    }

    public void setCurrentJoinGameCandidate(int currentJoinGameCandidate) {
        this.currentJoinGameCandidate = currentJoinGameCandidate;
    }

    public boolean getStartGame() {return startGame;}
    public void setStartGame(boolean set) {startGame = set;}

//    public String getHostNumber() {
//        return hostNumber;
//    }
//
//    public void setHostNumber(String hostNumber) {
//        this.hostNumber = hostNumber;
//    }
//
//    public String getPortNumber() {
//        return portNumber;
//    }
//
//    public void setPortNumber(String portNumber) {
//        this.portNumber = portNumber;
//    }

    public void endGame(){
        stopServerPoller();

        //just quit the program here
        System.exit(0);
    }

}
