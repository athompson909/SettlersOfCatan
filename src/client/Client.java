package client;

import shared.model.ClientModel;
import shared.model.ClientUpdateManager;
import shared.model.commandmanager.CommandManager;
import server.ServerPoller;
import server.ServerProxy;

/**
 *
 *
 * Created by Alise on 9/20/2016.
 */
public class Client {
    private ClientFacade clientFacade;
    private ServerPoller serverPoller;
    private CommandManager commandManager;
    private ClientModel clientModel;
    private ClientUpdateManager clientUpdateManager;
    // private GameManager gameManager;
    // private View view;
    // private Controller controller;
    private ServerProxy serverProxy;

    public Client() {
        serverPoller = new ServerPoller();
        commandManager = new CommandManager();
        clientModel = new ClientModel();
        clientUpdateManager = new ClientUpdateManager(clientModel);
        serverProxy = new ServerProxy();

        clientFacade = new ClientFacade(serverProxy, clientModel);
    }


}
