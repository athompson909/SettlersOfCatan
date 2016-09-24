package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.definitions.ResourceType;

/**
 * Created by Alise on 9/18/2016.
 */
public class MaritimeTradeCommand extends BaseCommand {

    /**
     * Index of player involved in maritime trade
     */
    int playerIndex;
    /**
     * ratio of trade associated with port (or 4:1) if no port
     */
    int ratio;
    /**
     * ResourceType being given to the bank
     */
    ResourceType toTrade;
    /**
     * ResourceType being received from bank
     */
    ResourceType toReceive;

    /**
     * Creates MaritimeTradeCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * sets data members
     *
     * @param index
     * @param rat
     * @param inputResource
     * @param outputResource
     */
    public MaritimeTradeCommand(int index, int rat, ResourceType inputResource, ResourceType outputResource){

    }

    /**
     * Calls all necessary update functions
     *
     * Calls Player update methods to reflect change in hand
     * Calls ResourceTypeBan update methods to reflect change in
     * recources in bank
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
