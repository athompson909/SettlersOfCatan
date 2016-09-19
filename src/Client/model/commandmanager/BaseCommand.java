package Client.model.commandmanager;

/**
 * BaseCommand is the generic command object that all other commands inherit from.
 * It contains the ID of the player who is executing the command,
 * and functions to execute the command on the client side and the server side.
 *
 * Created by Mitchell on 9/15/2016.
 */
public class BaseCommand {

    /**
     * the ID of the player who requested the command
     */
    public String playerID = null;

    /**
     * ClientExec() facilitates the process of passing the command object to the JSONTranslator.
     */
    public void clientExec(){

    }

    /**
     * ServerExec() executes all the smaller commands required to complete the main command on the server side.
     */
    public void serverExec() {

    }
}
