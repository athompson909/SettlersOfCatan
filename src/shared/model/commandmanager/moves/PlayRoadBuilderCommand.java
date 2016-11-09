package shared.model.commandmanager.moves;

import com.google.gson.annotations.SerializedName;
import server.IServerFacade;
import server.ServerTranslator;
import shared.locations.EdgeLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayRoadBuilderCommand extends BaseCommand {

    /**
     * Index of player playing roadBuilding card
     */
    private int playerIndex;

    /**
     * First roadLocation where player is building road
     */
    @SerializedName("spot1")
    private EdgeLocation locationONE;

    /**
     * Second roadLocation where player is building road
     */
    @SerializedName("spot2")
    private EdgeLocation locationTWO;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "Road_Building";

    /**
     * Creates empty PlayRoadBuilderCommand
     */
    public PlayRoadBuilderCommand() {
    }

    /**
     * Creates PlayRoadBuilderCommand object to be sent to client.ClientFacade
     * to be translated into JSON
     *
     * sets data members
     *
     * @param playerIndex
     * @param edgeLocation1
     * @param edgeLocation2
     */
    public PlayRoadBuilderCommand(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2){
        this.playerIndex = playerIndex;
        locationONE = edgeLocation1;
        locationTWO = edgeLocation2;
    }

    /**
     * Calls all necessary model update methods
     *
     * Calls Map update methods to reflect to new roads of the
     * specified colr
     * Calls Player update methods to reflect decremented road count
     * Calls TurnTracker to recount and reassign longestRoad
     *
     */
    @Override
    public String serverExec() {
        ClientModel model = IServerFacade.getInstance().playRoadBuilding(getUserId(), getGameId(), this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }


//Getters and Setters
    public EdgeLocation getLocationONE() {
        return locationONE;
    }

    public void setLocationONE(EdgeLocation locationONE) {
        this.locationONE = locationONE;
    }

    public EdgeLocation getLocationTWO() {
        return locationTWO;
    }

    public void setLocationTWO(EdgeLocation locationTWO) {
        this.locationTWO = locationTWO;
    }

    public String getType() {
        return type;
    }

    public int getPlayerIndex() {return playerIndex;}
}
