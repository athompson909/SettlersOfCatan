package client.model.commandmanager.game;

import client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class FetchNewModelCommand extends BaseCommand {
    /**
     * version of the client model
     */
    private int version;

    /**
     * Creates FetchNewModelCommand to send to client.ClientFacade
     * Sets data members
     * @param version = client model version number
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