package shared.model.commandmanager.game;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameResetCommand extends BaseCommand {
    /**
     * Creates GameResetCommand to send to the client.ClientFacade
     */
    public GameResetCommand(){

    }

    /**
     * Tells server to reset the game
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){

        return null;
    }
}
