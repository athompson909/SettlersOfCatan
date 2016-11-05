package shared.model.commandmanager.game;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GetGameCommandsCommand extends BaseCommand {
    /**
     * Creates GetGameCommandsCommand to send to client.ClientFacade.
     */
    public GetGameCommandsCommand(){

    }

    /**
     * Tells the server to get all the game commands
     * @param command
     */
    @Override
    public JSONObject serverExec(BaseCommand command){

        return null;
    }
}
