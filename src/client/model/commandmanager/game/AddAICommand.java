package client.model.commandmanager.game;

import client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class AddAICommand extends BaseCommand {
    /**
     * Creates AICommand object to send to client.ClientFacade
     */
    public AddAICommand() {

    }

    /**
     * Tells server to add AI to game
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
