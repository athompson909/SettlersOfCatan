package shared.model.commandmanager.game;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 *
 * This isn't actually necessary - the server only needs the URL header anyways!
 *
 * Created by Alise on 9/18/2016.
 */
public class ExecuteGameCommandsCommand extends BaseCommand {
    /**
     * Creates ExecuteGameCommandsCommand to send to client.ClientFacade
     */
    public ExecuteGameCommandsCommand(){

    }

    //list of all the commands to be executed - not sure about this
    // JUST GET THIS FROM THE COMMANDMANAGER
    //public List<String> gameCommands = new ArrayList<String>();

    /**
     * Tells server to execute the list of game commands
     */
    @Override
    public JSONObject serverExec(BaseCommand command){

        return null;
    }
}
