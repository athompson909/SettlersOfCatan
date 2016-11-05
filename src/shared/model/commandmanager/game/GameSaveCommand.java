package shared.model.commandmanager.game;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameSaveCommand extends BaseCommand {
    /**
     * id of game to save
     */
    @SerializedName("id")
    private int gameID;

    /**
     * name of file to create
     */
    @SerializedName("name")
    private String fileName;

    /**
     * Creates empty Command
     */
    public GameSaveCommand() {
    }

    /**
     * CreatesGameSaveCommand to send to the client.ClientFacade. Sets data members.
     * @param gameID
     * @param fileName
     */
    public GameSaveCommand(int gameID, String fileName){
        this.gameID = gameID;
        this.fileName = fileName;
    }

    /**
     * Tells server to save given game with to a file with the given name
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

    public String getFileName() {
        return fileName;
    }
}
