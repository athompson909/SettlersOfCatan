package shared.model.commandmanager.game;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;
import shared.definitions.CatanColor;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameJoinCommand extends BaseCommand {

    /**
     * gameID of game the player wants to join
     */
    @SerializedName("id")
    private int gameID;

    /**
     * color of player
     */
    private CatanColor color;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final transient String type = "joinGame";

    /**
     * Creates empty GameJoindCommand
     */
    public GameJoinCommand() {
    }

    /**
     * Creates GameJoinCommand to send to the client.ClientFacade
     * @param gameID
     * @param color
     */
    public GameJoinCommand(int gameID, CatanColor color){
        this.gameID = gameID;
        this.color = color;
    }

    /**
     * Tells server to add user as new player of game with given gameID
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){

        return null;
    }

    //Getters

    public int getGameID() {
        return gameID;
    }

    public CatanColor getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
