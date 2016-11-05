package shared.model.commandmanager.moves;

import org.json.JSONObject;
import shared.locations.HexLocation;
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
     * Tells server to move resource from victim to robbing players hand
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){

        return null;
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
