package shared.model.commandmanager.game;

import org.json.JSONObject;
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
     * Tells server to add AI to game
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){

        return null;
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
