package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.locations.EdgeLocation;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildRoadCommand implements BaseCommand {

    /**
     * ID of player who is building settlement
     */
    int playerID;
    /**
     * Location where road is being placed
     */
    EdgeLocation location;

    /**
     * Creates a BuildRoadCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members
     *
     * @param edgeLocation
     * @param ID
     */
    public BuildRoadCommand(EdgeLocation edgeLocation, int ID){
        playerID = ID;
        location = edgeLocation;
    }

    /**
     * Calls all necessary model update functions
     *
     * Updates Map to reflect new road of specified color
     * Updates Player to reflect decremented road count
     * Updates TurnTracker to reflect updated victory points and to recount longestRoad
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
