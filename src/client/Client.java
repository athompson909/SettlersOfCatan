package client;

import client.turntracker.IState;
import client.turntracker.WaitingState;
import shared.model.ClientModel;
import shared.model.commandmanager.CommandManager;

/**
 *
 *
 * Created by Alise on 9/20/2016.
 */
public class Client {

    private ServerPoller serverPoller;
    private CommandManager commandManager;
    private ClientModel clientModel;
    private IState state = new WaitingState();
    private IServerProxy serverProxy;

    private boolean updateOverride = false;

    private static Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

    private Client() {
        commandManager = new CommandManager();

        //todo this parameter is hardcoded
        clientModel = new ClientModel(0);
        serverProxy = new ServerProxy();

        ClientFacade.getInstance().setValues(serverProxy, clientModel);
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
        this.serverPoller = new ServerPoller(serverProxy);
    }

    public IState updateGameState(){
        state = state.update(clientModel.getTurnTracker());
        return state;
    }
}
