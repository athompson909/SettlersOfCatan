package shared.model.commandmanager.game;

import server.IServerFacade;
import server.ServerTranslator;
import shared.model.commandmanager.BaseCommand;

/**
 * this may not be necessary, since the server only needs the URL to execute this command
 *
 * Created by Alise on 9/18/2016.
 */
public class ListAICommand extends BaseCommand {
    /**
     * Creates ListAICommand
     */
    public ListAICommand(){

    }

    /**
     * Asks the server to send a list of all AI types
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId) {

        String[] listAI = IServerFacade.getInstance().listAI();
        return ServerTranslator.getInstance().listAIToString(listAI);
    }
}
