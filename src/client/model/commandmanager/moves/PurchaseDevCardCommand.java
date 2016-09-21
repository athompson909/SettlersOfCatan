package client.model.commandmanager.moves;

import client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PurchaseDevCardCommand extends BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * Creates PurchaseDevCardCommand to send to client.ClientFacade. sets player Index
     * @param playerIndex
     */
    public PurchaseDevCardCommand(int playerIndex){

    }

    /**
     * Tells server to give player new DevCard
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
