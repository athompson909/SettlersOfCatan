package shared.model.commandmanager.moves;

import server.IServerFacade;
import server.ServerTranslator;
import shared.locations.EdgeLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildRoadCommand extends BaseCommand {

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
    private boolean free;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "buildRoad";


    /**
     * Creates a BuildRoadCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members
     *
     * @param edgeLocation
     * @param ID
     */
    public BuildRoadCommand(EdgeLocation edgeLocation, int ID, boolean free){
        playerIndex = ID;
        roadLocation = edgeLocation;
        this.free = free;
    }

    /**
     * Creates empty BuildRoadCommand
     */
    public BuildRoadCommand() {}

    /**
     * Calls all necessary model update functions
     *
     * Updates Map to reflect new road of specified color
     * Updates Player to reflect decremented road count
     * Updates TurnTracker to reflect updated victory points and to recount longestRoad
     *
     */
    @Override
    public String serverExec() {
        ClientModel model = IServerFacade.getInstance().buildRoad(getUserId(), getGameId(), this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }

//Getters and Setters
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

    public String getType() {
        return type;
    }
}
