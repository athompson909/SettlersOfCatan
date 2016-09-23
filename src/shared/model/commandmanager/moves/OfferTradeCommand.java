package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.model.resourcebank.ResourceList;

/**
 * Created by Alise on 9/18/2016.
 */
public class OfferTradeCommand extends BaseCommand {

    /**
     * playerIndex of player offering trade
     */
    int playerIndex;
    /**
     * list of resources being offered
     */
    ResourceList offer;
    /**
     * playerIndex of player being offered trade
     */
    int receiver;

    /**
     * Creates OfferTradeCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data memebrs
     *
     * @param index
     * @param off
     * @param rec
     */
    public OfferTradeCommand(int index, ResourceList off, int rec){
        playerIndex = index;
        offer = off;
        receiver = rec;
    }

    /**
     * Creates an accept trade command object to invite other user to
     * accept or reject offer
     *
     * @param BC
     * @return true if other user accepted offer
     */
    public boolean buildAcceptTradeCommand(BaseCommand BC) {
        return false;
    }

    /**
     * Calls all necessary model update methods
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
