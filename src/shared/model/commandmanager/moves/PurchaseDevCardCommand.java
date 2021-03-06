package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.PersistenceManager;
import server.ServerTranslator;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PurchaseDevCardCommand extends BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "buyDevCard";

    /**
     * Creates empty PurchaseDevCardCommand
     */
    public PurchaseDevCardCommand() {
    }

    /**
     * Creates PurchaseDevCardCommand to send to client.ClientFacade. sets player Index
     * @param playerIndex
     */
    public PurchaseDevCardCommand(int playerIndex){
        this.playerIndex = playerIndex;
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
     * Tells server to give player new DevCard
     */
    @Override
    public String serverExec(){
        JSONObject purchasDevCardJSON = new JSONObject(getRequest());
        playerIndex = purchasDevCardJSON.getInt("playerIndex");

        setUserIdFromCookie();
        PurchaseDevCardCommand command = new PurchaseDevCardCommand(playerIndex);
        command.setUserId(getUserId());
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().buyDevCard(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            //IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" bought a development card", getUserId());
            PersistenceManager.getInstance().writeGame(getGameId());
            PersistenceManager.getInstance().clearCommands(getGameId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserId();//getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().buyDevCard(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            model.addLog(" bought a development card", userId);
            return true; //it worked
        }
        else{
            System.out.println(">PURCHASEDEVCARDCMD: reExec(): couldn't re-execute!");
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
