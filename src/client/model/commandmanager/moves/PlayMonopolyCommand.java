package client.model.commandmanager.moves;

import client.model.commandmanager.BaseCommand;
import client.model.resourcebank.Resource;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayMonopolyCommand extends BaseCommand {

    /**
     * Index of player playing Monopoly
     */
    int playerIndex;
    /**
     * Resource player is calling monopoly on
     */
    Resource resource;

    /**
     * Creates PlayMonopolyCommand object to be sent to client.ClientFacade
     * for translating into JSON
     *
     * sets data members
     *
     * @param index
     * @param res
     */
    public PlayMonopolyCommand(int index, Resource res){
        playerIndex = index;
        resource = res;
    }

    /**
     * Calls all necessary model update methods
     *
     * Calls Player update methods to reflect changes in new and old devCard lists
     * Calls Player update methods to reflect changes in (potentially) each player's hand
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
