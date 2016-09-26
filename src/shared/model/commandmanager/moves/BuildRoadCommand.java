package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.locations.EdgeLocation;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildRoadCommand implements BaseCommand {

    /**
     * Index of player who is building settlement
     */
    int playerIndex;
    /**
     * Location where road is being placed
     */
    EdgeLocation roadLocation;

    /**
     * True if the road was placed in the first 2 rounds, false otherwise
     */
    boolean free;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    String type;


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
        type = "buildRoad";
        playerIndex = ID;
        roadLocation = edgeLocation;
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

    /**
     * Getter for boolean free
     * @return
     */
    public boolean isFree() {
        return free;
    }

    /**
     * Setter for boolean free
     * @param free
     */
    public void setFree(boolean free) {
        this.free = free;
    }
}
