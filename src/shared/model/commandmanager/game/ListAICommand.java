package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class ListAICommand extends BaseCommand {
    /**
     * Creates ListAICommand to send to the client.ClientFacade
     */
    public ListAICommand(){

    }

    /**
     * Asks the server to send a list of all AI types
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
