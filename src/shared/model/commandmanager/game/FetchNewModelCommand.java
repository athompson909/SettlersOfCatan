package shared.model.commandmanager.game;

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
     * Asks server if there is an updated model
     */
    @Override
    public String serverExec(){
       ClientModel model = IServerFacade.getInstance().model(getUserId(), getGameId(), this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }

    //Getters
    public int getVersion() {
        return version;
    }
}
