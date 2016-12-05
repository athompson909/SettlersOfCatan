package shared.model.commandmanager.moves;

import shared.locations.HexLocation;
import shared.shared_utils.Converter;
import org.json.JSONObject;
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
     * @param index
     */
    public BuildRoadCommand(EdgeLocation edgeLocation, int index, boolean free){
        playerIndex = index;
        roadLocation = edgeLocation;
        this.free = free;
    }

    /**
     * Creates empty BuildRoadCommand
     */
    public BuildRoadCommand() {}

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONBoth();
    }


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
        JSONObject buildRoadJSON = new JSONObject(getRequest());
        playerIndex = buildRoadJSON.getInt("playerIndex");
        JSONObject roadLocJSON = buildRoadJSON.getJSONObject("roadLocation");
        int x = roadLocJSON.getInt("x");
        int y = roadLocJSON.getInt("y");
        //roadLocation.setX(roadLocJSON.getInt("x"));
        //roadLocation.setY(roadLocJSON.getInt("y"));
        roadLocation = new EdgeLocation(new HexLocation(x,y), Converter.stringToEdgeDirection(roadLocJSON.getString("direction")));
        free = buildRoadJSON.getBoolean("free");

        BuildRoadCommand command = new BuildRoadCommand(roadLocation, playerIndex, free);
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().buildRoad(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" built a road", getUserId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    /**
     * Called to bring the ClientModel back up to speed upon reload from file/sql.
     *
     * Sierra: we need to pass in the userID here, when this function is called the GamesMgr doesn't have all the games added to it yet
     *
     * @param gameID
     */
    @Override
    public void reExecute(int gameID){
        int userId = getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().buildRoad(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            model.addLog(" built a road", userId);
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

    public EdgeLocation getRoadLocation() {return roadLocation;}

    public int getPlayerIndex() {return playerIndex;}

    public boolean getFree() {return free;}
}
