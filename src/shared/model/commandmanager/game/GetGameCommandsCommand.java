package shared.model.commandmanager.game;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GetGameCommandsCommand extends BaseCommand {
    /**
     * Creates GetGameCommandsCommand.
     */
    public GetGameCommandsCommand(){

    }

    /**
     * Tells the server to get all the game commands
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){

        return null;
    }
}
