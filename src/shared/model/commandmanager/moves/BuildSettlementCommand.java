package shared.model.commandmanager.moves;

import shared.shared_utils.Converter;
import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;
import shared.model.map.VertexObject;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildSettlementCommand extends BaseCommand {

    /**
     * ID of player who is building settlement
     */
    private int playerIndex;

    /**
     * Contains owner and roadLocation of vertex where player is upgrading to city
     */
    private transient VertexObject vertex;

     /*
     * Location where road is being placed
     */
     //  EdgeLocation location;   //**** I think this was a mistake, Settlements require VertexObjects

    /**
     * True if the road was placed in the first 2 rounds, otherwise false
     */
    private boolean free;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "buildSettlement";

    //For serialization purposes only
    private VertexLocation vertexLocation;

    /**
     * Creates a BuildSettlementCommand object to be sent to
     * client.ClientFacade for translation into JSON
     *
     * Sets data members
     * @param vertexObject  - this has the id of the player owner for this settlement inside it!
     */
    public BuildSettlementCommand(VertexObject vertexObject, boolean free){
        vertex = vertexObject;
        playerIndex = vertexObject.getOwner();
        vertexLocation = vertexObject.getVertexLocation();
        this.free = free;
        // location = edgeLocation;
    }

    private void setValues(VertexObject vertexObject, boolean free) {
        vertex = vertexObject;

        vertexObject.setOwner(playerIndex);
        vertexLocation = vertexObject.getVertexLocation();
        this.free = free;
    }

    /**
     * Creates empty BuildSettlementCommand
     */
    public BuildSettlementCommand() {
    }

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
     * Updates Map to reflect new settlement of specified color
     * Updates specified player to reflect decremented settlements
     * Updates TurnTracker to reflect victory points
     */
    @Override
    public String serverExec() {
        JSONObject buildSettlementJSON = new JSONObject(getRequest());
        playerIndex = buildSettlementJSON.getInt("playerIndex");

        JSONObject vertexLocJSON = buildSettlementJSON.getJSONObject("vertexLocation");
        int x = vertexLocJSON.getInt("x"), y = vertexLocJSON.getInt("y");
        VertexDirection dir = Converter.stringToVertexDirection(vertexLocJSON.getString("direction"));
        VertexLocation vertexLocation = new VertexLocation(new HexLocation(x, y), dir);

        VertexObject vertexObject = new VertexObject(vertexLocation);

        setValues(vertexObject, buildSettlementJSON.getBoolean("free"));

        BuildSettlementCommand command = new BuildSettlementCommand(vertexObject, free);
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().buildSettlement(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" built a settlement", getUserId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().buildSettlement(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            model.addLog(" built a settlement", userId);
            return true; //it worked
        }
        else{
            System.out.println(">BUILDSTLMTCMD: reExec(): couldn't re-execute!");
            return false;
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

    public boolean getFree() {return free;}

    public String getType() {
        return type;
    }

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
}
