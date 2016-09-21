package Client.model.commandmanager.moves;

import Client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PurchaseDevCardCommand extends BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * Creates PurchaseDevCardCommand to send to Client.ClientFacade. sets player Index
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
