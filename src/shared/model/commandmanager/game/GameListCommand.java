package shared.model.commandmanager.game;

import client.data.GameInfo;
import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameListCommand extends BaseCommand {
    /**
     * Creates GameListCommand to send to client.ClientFacade
     */
    public GameListCommand() {

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
     * Tells the server to send a list of all games
     */
    @Override
    public String serverExec() {
        int userId = getUserId();

        GameInfo[] response = IServerFacade.getInstance().list(userId); // todo: implement (not implemented in mock server facade)
        return ServerTranslator.getInstance().gamesListToJSON(response); // this return statement may need to be different
    }
}
