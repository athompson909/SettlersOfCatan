package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.model.resourcebank.ResourceList;

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
     * Creates DiscardCommand object to be sent to client.ClientFacade
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
