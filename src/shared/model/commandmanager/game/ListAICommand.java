package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * this may not be necessary, since the server only needs the URL to execute this command
 *
 * Created by Alise on 9/18/2016.
 */
public class ListAICommand implements BaseCommand {
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
