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
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONBoth();
    }


    /**
     * Calls all necessary model updates methods
     *
     * Calls TurnTracker update methods to reflect new victory points
     * Calls Player update methods to reflect changes in old and new devCard lists
     *
     */
    @Override
    public String serverExec() {
        JSONObject monumentJSON = new JSONObject(getRequest());
        playerIndex = monumentJSON.getInt("playerIndex");

        setUserIdFromCookie();
        PlayMonumentCommand command = new PlayMonumentCommand(playerIndex);
        command.setUserId(getUserId());
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().playMonument(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" played a monument", getUserId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserId();//getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().playMonument(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            model.addLog(" played a monument", userId);
            return true; //it worked
        }
        else{
            System.out.println(">PLAYMONUMENTCMD: reExec(): couldn't re-execute!");
            return false;
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
