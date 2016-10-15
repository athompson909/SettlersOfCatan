package client;

import client.turntracker.IState;
import client.turntracker.WaitingState;
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

    private IState state = new WaitingState();

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

    public void updateGameState(){
        state = state.update(clientModel.getTurnTracker());
    }

    public IState getGameState(){
        return state;
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
