import Client.model.ClientModel;
import Client.model.ClientUpdateManager;
import Client.model.commandmanager.CommandManager;
import server.ServerPoller;
import server.ServerProxy;

/**
 * Created by Alise on 9/20/2016.
 */
public class Client {
    private ClientFacade clientFacade;
    private ServerPoller serverPoller;
    private CommandManager commandManager;
    private ClientUpdateManager clientUpdateManager;
    private ClientModel clientModel;
  //  private GameManager gameManager;
   // private View view;
   // private Controller controller;
    private ServerProxy serverProxy;

}
