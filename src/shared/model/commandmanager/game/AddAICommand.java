package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class AddAICommand implements BaseCommand {
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
