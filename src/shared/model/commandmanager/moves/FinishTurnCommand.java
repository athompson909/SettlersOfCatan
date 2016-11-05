package shared.model.commandmanager.moves;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * EndTurn doesn't have any API documentation or an entry on the swagger page, so I don't think it's a real command
 *  - Sierra
 *
 * Created by Alise on 9/18/2016.
 */
public class FinishTurnCommand extends BaseCommand {

    /**
     * index of player finishing turn
     */
    int playerIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "finishTurn";

    /**
     * Creates empty FinishTurnCommand
     */
    public FinishTurnCommand() {}

    /**
     * Creates FinishTurnCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * sets data member
     *
     * @param index
     */
    public FinishTurnCommand(int index){
        this.playerIndex = index;
    }

    /**
     * Calls necessary model update methods to reflect changes
     *
     * Calls TurnTracker update methods to reflect end of turn
     * and cjange currentPlayer
     * Calls MessageManager update methods to reflect new Log
     *
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId) {

        return null;
    }

//Getters

    public int getPlayerIndex() {
        return playerIndex;
    }

    public String getType() {
        return type;
    }
}
