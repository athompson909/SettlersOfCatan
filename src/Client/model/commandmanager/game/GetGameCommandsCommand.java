package Client.model.commandmanager.game;

import Client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GetGameCommandsCommand extends BaseCommand {
    /**
     * Creates GetGameCommandsCommand to send to ClientFacade.
     */
    public GetGameCommandsCommand(){

    }

    /**
     * Tells the server to get all the game commands
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
