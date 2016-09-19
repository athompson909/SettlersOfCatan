package Client.model.commandmanager.moves;

import Client.model.commandmanager.BaseCommand;
import Client.model.resourcebank.Resource;

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
     * Resource being given to the bank
     */
    Resource toTrade;
    /**
     * Resource being received from bank
     */
    Resource toReceive;

    /**
     * Creates MaritimeTradeCommand object to be sent to ClientFacade
     * for translation into JSON
     *
     * sets data members
     *
     * @param index
     * @param rat
     * @param inputResource
     * @param outputResource
     */
    public MaritimeTradeCommand(int index, int rat, Resource inputResource, Resource outputResource){

    }

    /**
     * Calls all necessary update functions
     *
     * Calls Player update methods to reflect change in hand
     * Calls ResourceBan update methods to reflect change in
     * recources in bank
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
