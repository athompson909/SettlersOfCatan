package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alise on 9/18/2016.
 */
public class ExecuteGameCommandsCommand implements BaseCommand {
    /**
     * Creates ExecuteGameCommandsCommand to send to client.ClientFacade
     */
    public ExecuteGameCommandsCommand(){

    }

    //list of all the commands to be executed - not sure about this
    public List<String> gameCommands = new ArrayList<String>();

    /**
     * Tells server to execute the list of game commands
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
