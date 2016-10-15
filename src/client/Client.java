package client;

import client.map.mapStates.MapState;

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

    private CommandManager commandManager;

    private ClientModel clientModel;

    private State gameState = State.WAITING;
    private IServerProxy serverProxy;
    private ServerPoller serverPoller;

    //According to the TAs tooltips: username must be 3-7 chars long, and can include letters, numbers, underscore, or dash
    private Pattern usernameDelimiter = Pattern.compile("([A-z]|[0-9]|_|-){3,7}");
    //According to the TAs tooltips: username must be 5+ chars long (stop at 16?),
    // and can include letters, numbers, underscore, or dash
    private Pattern passwordDelimiter = Pattern.compile("([A-z]|[0-9]|_|-){5,16}");


    private static Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

    private Client() {
        commandManager = new CommandManager();
        clientModel = new ClientModel(0);
        ClientFacade.getInstance().setValues(new ServerProxy(), clientModel);
        serverPoller = null;
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

    public Pattern getUsernameDelimiter() {
        return usernameDelimiter;
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
}
