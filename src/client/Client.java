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

    //TODO: make this a minimum of 3 chars eventually
    private Pattern delimiter = Pattern.compile("([A-z]|[0-9]){1,24}");

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

    public void setUpdateOverride(boolean bool) {
        updateOverride = bool;
    }

    public boolean isUpdateOverride() {
        return updateOverride;
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

    public Pattern getDelimiter() {
        return delimiter;
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
