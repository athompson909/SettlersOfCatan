package client;

import client.catan.CatanPanel;
import shared.model.ClientModel;
import shared.model.commandmanager.CommandManager;
import client.base.*;

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
    private Controller controller;
    private IServerProxy serverProxy;

    public Client() {
        commandManager = new CommandManager();

        //todo this parameter is hardcoded
        clientModel = new ClientModel(0);
        serverProxy = new ServerProxy();

        ClientFacade.getInstance().setValues(serverProxy, clientModel);
        serverPoller = new ServerPoller();
        serverPoller.setProxy(serverProxy);
    }

    public void setController(CatanPanel catanPanel) {
        //Get the panels from the catanPanel and set the Controllers...
        //Add the controllers as observers of the model
    }

}
