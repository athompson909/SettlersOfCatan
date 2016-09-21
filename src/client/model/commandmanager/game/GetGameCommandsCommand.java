package client.model.commandmanager.game;

import client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GetGameCommandsCommand extends BaseCommand {
    /**
     * Creates GetGameCommandsCommand to send to client.ClientFacade.
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