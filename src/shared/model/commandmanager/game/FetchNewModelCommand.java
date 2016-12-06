package shared.model.commandmanager.game;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class FetchNewModelCommand extends BaseCommand {
    /**
     * version of the client model
     */
    private int version;

    /**
     * Creates empty FetchNewModelCommand
     */
    public FetchNewModelCommand() {
    }

    /**
     * Creates FetchNewModelCommand to send to client.ClientFacade
     * Sets data members
     * @param version = client model version number
     */
    public FetchNewModelCommand(int version){
        this.version = version;
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
     * Asks server if there is an updated model
     */
    @Override
    public String serverExec() {
        ClientModel model = IServerFacade.getInstance().model(getUserId(), getGameId(), this);
        return ServerTranslator.getInstance().modelToJSON(model);
    }

    @Override
    public boolean reExecute(int gameID){
        return true;
    }

    //Getters
    public int getVersion() {
        return version;
    }
}
