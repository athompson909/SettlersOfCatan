package Client.model.commandmanager.moves;

import Client.model.commandmanager.BaseCommand;
import Client.model.resourcebank.ResourceList;

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
     * Creates OfferTradeCommand object to be sent to ClientFacade
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

    }

    /**
     * Calls all necessary model update methods
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
