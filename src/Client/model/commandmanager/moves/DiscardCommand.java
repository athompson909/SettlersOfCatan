package Client.model.commandmanager.moves;

import Client.model.commandmanager.BaseCommand;
import Client.model.resourcebank.ResourceList;

/**
 * Created by Alise on 9/18/2016.
 */
public class DiscardCommand extends BaseCommand {

    /**
     * Index of player who is discarding
     */
    int playerIndex;
    /**
     * list of cards player is discarding
     */
    ResourceList discardedCards;

    /**
     * Creates DiscardCommand object to be sent to Client.ClientFacade
     * for translation into JSON
     *
     * Sets data members
     * @param index
     * @param discarded
     */
    public DiscardCommand(int index, ResourceList discarded){
        playerIndex = index;
        discardedCards = discarded;
    }

    /**
     * Calls all neccessary model update methods
     *
     * Calls player updates methods to reflect new hand
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
