package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class FetchNewModelCommand extends BaseCommand {
    /**
     * version of the tests.client model
     */
    private int version;

    /**
     * Creates FetchNewModelCommand to send to tests.client.ClientFacade
     * Sets data members
     * @param version = tests.client model version number
     */
    public FetchNewModelCommand(int version){

    }

    /**
     * Asks server if there is an updated model
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
