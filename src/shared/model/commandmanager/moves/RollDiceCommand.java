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
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONBoth();
    }


    /**
     * Tells server the dice were rolled and to distribute/require discarding of resources
     */
    @Override
    public String serverExec(){
        JSONObject sendChatJSON = new JSONObject(getRequest());
        playerIndex = sendChatJSON.getInt("playerIndex");
        number = sendChatJSON.getInt("number");

        RollDiceCommand command = new RollDiceCommand(number);
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().rollNumber(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" rolled a "+number, getUserId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public void reExecute(int gameID){
        int userId = getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().rollNumber(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            model.addLog(" rolled a "+number, userId);
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
