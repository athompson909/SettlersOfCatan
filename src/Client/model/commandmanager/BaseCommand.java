package Client.model.commandmanager;

/**
 * BaseCommand is the generic command object that all other commands inherit from.
 *
 * Created by Mitchell on 9/15/2016.
 */
public class BaseCommand {

    /**
     * the ID of the player who requested the command
     */
    public String playerID = null;

    /**
     *
     */
    public void clientExec(){

    }

    /**
     *
     */
    public void serverExec() {

    }
}
