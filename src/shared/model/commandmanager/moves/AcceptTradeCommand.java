package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import server.UserManager;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class AcceptTradeCommand extends BaseCommand {
//Data Members
    /**
     * Index of player who is being offered a trade
     */
    int playerIndex;

    /**
     * true if player wants to willAccept trade
     */
    boolean willAccept;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "acceptTrade";

    /**
     * Creates empty AcceptTradeCommand
     */
    public AcceptTradeCommand(){

    }

    /**
     * Creates AcceptTradeCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members of object
     *
     * @param index
     * @param willAcc
     */
    public AcceptTradeCommand(int index, boolean willAcc){
        playerIndex = index;
        willAccept = willAcc;
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
     * Calls all necessary model update functions
     *
     * Calls Player update methods to reflect a change of cards in hand
     *
     */
    @Override
    public String serverExec() {
        JSONObject offerTradeJSON = new JSONObject(getRequest());
        playerIndex = offerTradeJSON.getInt("playerIndex");
        willAccept = offerTradeJSON.getBoolean("willAccept");

        setUserIdFromCookie();
        AcceptTradeCommand command = new AcceptTradeCommand(playerIndex, willAccept);
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().acceptTrade(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            if (!willAccept) {
                model.addLog(" rejected trade offer", getUserId());
            }
            else {
                //TESTING
                model.addLog(" accepted trade offer", getUserId());
            }
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().acceptTrade(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            if (!willAccept) {
                model.addLog(" rejected trade offer", userId);
            }
            else {
                model.addLog(" accepted trade offer", userId);
            }
            return true; //it worked
        }
        else{
            System.out.println(">ACCEPTTRADECMD: reExec(): couldn't re-execute!");
            return false;
        }
    }

    //Getters
    public int getPlayerIndex() {
        return playerIndex;
    }

    public boolean isWillAccept() {
        return willAccept;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public void setWillAccept(boolean willAccept) {
        this.willAccept = willAccept;
    }

    public String getType() {
        return type;
    }

    public boolean getWillAccept() {return willAccept;}

}
