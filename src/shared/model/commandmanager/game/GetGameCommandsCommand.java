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
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONBoth();
    }


    /**
     * Tells the server to get all the game commands
     */
    @Override
    public String serverExec() {

        return null;
    }
}
