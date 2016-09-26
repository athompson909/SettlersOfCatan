package shared.model.commandmanager.moves;

import com.google.gson.annotations.SerializedName;
import shared.model.commandmanager.BaseCommand;
import shared.model.map.VertexObject;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildCityCommand implements BaseCommand {

    /**
     * Contains owner and roadLocation of vertex where player is upgrading to city
     */
    @SerializedName("vertexLocation")
    VertexObject vert;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    String type;

    //TEST for serialization only
    int playerIndex;

    /**
     * Creates a buildCityCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members
     * @param vertexObject
     */
    public BuildCityCommand(VertexObject vertexObject){
        vert = vertexObject;
        type = "buildCity";
        playerIndex = vertexObject.getOwner();
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
}
