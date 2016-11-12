package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.locations.HexLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class RobPlayerCommand extends BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * roadLocation to move Robber to
     */
    private HexLocation location;

    /**
     * int 0-3 of player to rob; -1 if nobody
     */
    private int victimIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "robPlayer";

    /**
     * Creates empty RobPlayerCommand
     */
    public RobPlayerCommand() {
    }

    /**
     * Creates RobPlayerCommand to send to client.ClientFacade. Sets data members
     * @param playerIndex
     * @param location
     * @param victimIndex
     */
    public RobPlayerCommand(int playerIndex, HexLocation location, int victimIndex){
        this.playerIndex = playerIndex;
        this.location = location;
        this.victimIndex = victimIndex;
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
     * Tells server to move resource from victim to robbing players hand
     */
    @Override
    public String serverExec(){
        JSONObject robPlayerJSON = new JSONObject(getRequest());
        playerIndex = robPlayerJSON.getInt("playerIndex");
        victimIndex = robPlayerJSON.getInt("victimIndex");
        JSONObject locationJSON = robPlayerJSON.getJSONObject("location");
        location = new HexLocation(locationJSON.getInt("x"), locationJSON.getInt("y"));

        ClientModel model = IServerFacade.getInstance().robPlayer(getUserId(), getGameId(), this);
        if(model != null) {model.incrementVersion();}
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    //Getters

    public int getPlayerIndex() {
        return playerIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    public int getVictimIndex() {
        return victimIndex;
    }

    public String getType() {
        return type;
    }
}
