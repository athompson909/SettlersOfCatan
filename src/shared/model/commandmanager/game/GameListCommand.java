package shared.model.commandmanager.game;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameListCommand extends BaseCommand {
    /**
     * Creates GameListCommand to send to client.ClientFacade
     */
    public GameListCommand(){

    }

    /**
     * Tells the server to send a list of all games
     * @param command
     */
    @Override
    public JSONObject serverExec(BaseCommand command){

        return null;
    }
}
