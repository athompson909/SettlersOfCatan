package shared.model.commandmanager.moves;

import shared.locations.VertexLocation;
import shared.model.commandmanager.BaseCommand;
import shared.model.map.VertexObject;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildCityCommand implements BaseCommand {

    /**
     * ID of player who is building city
     */
    private int playerIndex;

    /**
     * Contains owner and roadLocation of vertex where player is upgrading to city
     */
    private transient VertexObject vertex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "buildCity";

    //For serialization purposes only
    private VertexLocation vertexLocation;


    /**
     * Creates a buildCityCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members
     * @param vertexObject
     */
    public BuildCityCommand(VertexObject vertexObject){
        vertex = vertexObject;

        playerIndex = vertexObject.getOwner();
        vertexLocation = vertexObject.getVertexLocation();
    }


    /**
     * Calls all necessary model update functions
     *
     * Calls map update methods to reflect new City of specified color
     * Calls Player update methods to reflect decremented city count
     * and incremented settlement count
     * Calls TurnTracker update methods to reflect incremented victory points
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }

    //Getters and Setters
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public VertexObject getVertex() {
        return vertex;
    }

    public void setVertex(VertexObject vertex) {
        this.vertex = vertex;
    }

    public String getType() {
        return type;
    }

}

