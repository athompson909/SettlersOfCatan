package shared.model.commandmanager.game;

import client.data.GameInfo;
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
     * Tells the server to send a list of all games
     */
    @Override
    public String serverExec() {
        int userId = getUserId();

        GameInfo[] response = IServerFacade.getInstance().list(userId);
        return ServerTranslator.getInstance().gameListToString(response);
    }
}
