package shared.model.commandmanager.game;

import org.json.JSONObject;
import server.IServerFacade;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class AddAICommand extends BaseCommand {

    /**
     * Creates empty AddAICommand
     */
    public AddAICommand() {
    }

    /**
     * Creates AICommand object
     */
    public AddAICommand(String aiType) {
        setAIType(aiType);
    }

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final transient String type = "addAI";


    private String AIType;

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONOnlyLogin();
    }

    /**
     * Tells server to add AI to game
     */
    @Override
    public String serverExec() {

        JSONObject AICommandJSON = new JSONObject(getRequest());
        AIType = AICommandJSON.getString("AIType");

        boolean response = IServerFacade.getInstance().addAI(getUserId(), getGameId());
        return response ? "Success" : "Could not add AI type -- ["+AIType+"]";
    }

    @Override
    public boolean reExecute(int gameID){
        return true;
    }


    public String getAIType() {
        return AIType;
    }

    public void setAIType(String AIType) {
        this.AIType = AIType;
    }

    public String getType() {
        return type;
    }
}
