package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;
import shared.model.map.VertexObject;
import shared.shared_utils.Converter;

/**
 * Created by Alise on 9/18/2016.
 */
public class BuildCityCommand extends BaseCommand {

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

    private void setValues(VertexObject vertexObject) {
        vertex = vertexObject;

        vertexObject.setOwner(playerIndex);
        vertexLocation = vertexObject.getVertexLocation();
    }

    /**
     * Creates empty BuildCityCommand
     */
    public BuildCityCommand(){}

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
     * Calls map update methods to reflect new City of specified color
     * Calls Player update methods to reflect decremented city count
     * and incremented settlement count
     * Calls TurnTracker update methods to reflect incremented victory points
     */
    @Override
    public String serverExec() {
        JSONObject buildCityJSON = new JSONObject(getRequest());
        playerIndex = buildCityJSON.getInt("playerIndex");
        // creating the vertex object:
        JSONObject vertexLocJSON = buildCityJSON.getJSONObject("vertexLocation");
        int x = vertexLocJSON.getInt("x"), y = vertexLocJSON.getInt("y");
        VertexDirection dir = Converter.stringToVertexDirection(vertexLocJSON.getString("direction"));
        VertexLocation vertexLocation = new VertexLocation(new HexLocation(x, y), dir);

        VertexObject vertexObject = new VertexObject(vertexLocation);
        setValues(vertexObject);

        setUserIdFromCookie();
        BuildCityCommand command = new BuildCityCommand(vertexObject);
        command.setUserId(getUserId());
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().buildCity(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" built a city", getUserId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserId();//getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().buildCity(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            model.addLog(" built a city", userId);
            return true; //it worked
        }
        else{
            System.out.println(">BUILDCITYCMD: reExec(): couldn't re-execute!");
            return false;
        }

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

    public VertexLocation getVertexLocation() {
        return vertexLocation;
    }
}

