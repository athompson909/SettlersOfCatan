package Client.model.commandmanager.game;

import Client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class ExecuteGameCommandsCommand extends BaseCommand {
    /**
     * Creates ExecuteGameCommandsCommand to send to Client.ClientFacade
     */
    public ExecuteGameCommandsCommand(){

    }

    /**
     * Tells server to execute the list of game commands
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
