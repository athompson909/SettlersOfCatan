package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.model.map.EdgeLocation;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildSettlementCommand extends BaseCommand {

    /**
     * ID of player who is building settlement
     */
    int playerID;
    /**
     * Location where road is being placed
     */
    EdgeLocation location;

    /**
     * Creates a BuildSettlementCommand object to be sent to
     * tests.client.ClientFacade for translation into JSON
     *
     * Sets data members
     * @param edgeLocation
     * @param ID
     */
    public BuildSettlementCommand(EdgeLocation edgeLocation, int ID){
        playerID = ID;
        location = edgeLocation;
    }

    /**
     * Calls all necessary model update functions
     *
     * Updates Map to reflect new settlement of specified color
     * Updates specified player to reflect decremented settlements
     * Updates TurnTracker to reflect victory points
     *
     * @param BC BaseCommand object
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }

}
