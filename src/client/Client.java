package client;

import shared.model.ClientModel;
import shared.model.commandmanager.CommandManager;

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
    // private GameManager gameManager;
    // private View view;
    // private Controller controller;
    private ServerProxy serverProxy;

    public Client() {
        serverPoller = new ServerPoller();
        commandManager = new CommandManager();

        //todo this parameter is hardcoded
        clientModel = new ClientModel(0);
        serverProxy = new ServerProxy();

        clientFacade = new ClientFacade(serverProxy, clientModel);
    }


}
