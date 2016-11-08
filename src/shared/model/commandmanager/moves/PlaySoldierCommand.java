package shared.model.commandmanager.moves;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.locations.HexLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlaySoldierCommand extends BaseCommand {

    /**
     * Index of player placing the soldier/moving the robber
     */
    private int playerIndex;
    /**
     * Location of hex where robber is being placed
     */
    @SerializedName("location")
    private HexLocation robberLoc;
    /**
     * PlayerIndex of player being robbed as a result of the soldier moving
     */
    private int victimIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "Soldier";

    /**
     * Creates empty PlaySoldierCommand
     */
    public PlaySoldierCommand() {
    }

    /**
     * Creates a PlaySoldierCommand object to be sent to client.ClientFacade
     * for translating into JSON
     *
     * sets data members
     *
     * @param index
     * @param robberLocation
     * @param victimIndex
     */
    public PlaySoldierCommand(int index, HexLocation robberLocation, int victimIndex){
        playerIndex = index;
        robberLoc = robberLocation;
        this.victimIndex = victimIndex;
    }

    /**
     * Calls all necessary model update functions
     *
     * Calls Map robber update functions to reset roadLocation of the robber
     * Calls Player update functions to reflect changes in hands
     *
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId) {
        ClientModel model = IServerFacade.getInstance().playSoldier(userId, gameId, this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }

//Getters and Setters
    public HexLocation getRobberLoc() {
        return robberLoc;
    }

    public void setRobberLoc(HexLocation robberLoc) {
        this.robberLoc = robberLoc;
    }

    public int getVictimIndex() {
        return victimIndex;
    }

    public void setVictimIndex(int victimIndex) {
        this.victimIndex = victimIndex;
    }

    public String getType() {
        return type;
    }
}

