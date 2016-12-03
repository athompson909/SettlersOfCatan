package shared.model.commandmanager.game;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import server.IServerFacade;
import server.PersistenceManager;
import shared.definitions.CatanColor;
import shared.model.commandmanager.BaseCommand;
import shared.shared_utils.Converter;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONOnlyLogin();
    }

    /**
     * Tells server to add user as new player of game with given gameID
     */
    @Override
    public String serverExec(){

        JSONObject requestJSON = new JSONObject(getRequest());
        gameID = requestJSON.getInt("id");
        color = Converter.stringToCatanColor(requestJSON.getString("color"));

        GameJoinCommand command = new GameJoinCommand(gameID, color);
        command.setGameId(gameID);
        boolean success = IServerFacade.getInstance().join(getUserId(), command);
        if(success) {
            String fullResponseLoginCookieStr = "catan.game="+gameID+";Path=/;";
            List<String> cookieList = new ArrayList<>(1);
            cookieList.add(fullResponseLoginCookieStr);
            getHttpExchange().getResponseHeaders().put("Set-cookie", cookieList);
            //IServerFacade.getInstance().logCommand(command.getGameId(), command);
            PersistenceManager.getInstance().writeGame(gameID);
        }

        return (success ? "Success" : null);
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
