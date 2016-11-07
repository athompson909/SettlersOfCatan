package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class RollDiceCommand extends BaseCommand {
    /**
     * number rolled
     */
    private int number;

    /**
     * the index of the player who rolled the dice
     */
    private int playerIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "rollNumber";

    /**
     * Creates empty RollDiceCommand
     */
    public RollDiceCommand() {
    }

    /**
     * Creates RollDiceCommand to send to client.ClientFacade. Sets data members
     * @param number
     */
    public RollDiceCommand(int number){
        this.number = number;
    }

    /**
     * Tells server the dice were rolled and to distribute/require discarding of resources
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){
        ClientModel model = IServerFacade.getInstance().rollNumber(userId, gameId, this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getType() {
        return type;
    }
}
