package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayMonumentCommand extends BaseCommand {

    /**
     * Index of player playing monument card
     */
    private int playerIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "Monument";

    /**
     * Creates empty PlayMonumentCommand
     */
    public PlayMonumentCommand() {
    }

    /**
     * Creates PlayMonumentCard object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * sets data member
     *
     * @param index
     */
    public PlayMonumentCommand(int index){
        playerIndex = index;
    }

    /**
     * Calls all necessary model updates methods
     *
     * Calls TurnTracker update methods to reflect new victory points
     * Calls Player update methods to reflect changes in old and new devCard lists
     *
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId) {
        ClientModel model = IServerFacade.getInstance().playMonument(userId, gameId, this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }

//Getters
    public String getType() {
        return type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
