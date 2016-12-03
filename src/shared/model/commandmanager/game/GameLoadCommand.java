package shared.model.commandmanager.game;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameLoadCommand extends BaseCommand {
    /**
     * name of file to Load
     */
    @SerializedName("name")
    private String filename;

    /**
     * Creates empty Command
     */
    public GameLoadCommand() {
    }

    /**
     * Creates GameLoadCommand to send to the client.ClientFacade. Sets fileName.
     * @param fileName
     */
    public GameLoadCommand(String fileName){

        this.filename = fileName;
    }

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONOnlyLogin();
    }


    /**
     * Tells server to load given file
     */
    @Override
    public String serverExec(){

        return null;
    }

    @Override
    public void reExecute(){}
}
