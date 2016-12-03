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
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONOnlyLogin();
    }


    /**
     * Tells server to reset the game
     */
    @Override
    public String serverExec(){

        return null;
    }

    @Override
    public void reExecute(int gameID){}
}
