package client;

import client.model.ClientModel;
import client.model.ClientUpdateManager;
import client.model.commandmanager.CommandManager;

/**
 * Created by Alise on 9/20/2016.
 */
public class Client {
    private ClientFacade clientFacade;
    private ServerPoller serverPoller;
    private CommandManager commandManager;
    private ClientUpdateManager clientUpdateManager;
    private ClientModel clientModel;
    // private GameManager gameManager;
    // private View view;
    // private Controller controller;
    private ServerProxy serverProxy;



}
