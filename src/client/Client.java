package client;

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
    // private GameManager gameManager;
    // private View view;
    // private Controller controller;
    private IServerProxy serverProxy;

    public Client() {
        commandManager = new CommandManager();

        //todo this parameter is hardcoded
        clientModel = new ClientModel(0);
        serverProxy = new ServerProxy();

        ClientFacade.getInstance().setValues(serverProxy, clientModel);
        serverPoller = new ServerPoller();
    }


}
