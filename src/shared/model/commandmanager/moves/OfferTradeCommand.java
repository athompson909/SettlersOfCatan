package shared.model.commandmanager.moves;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;
import shared.model.resourcebank.ResourceList;

/**
 * Created by Alise on 9/18/2016.
 */
public class OfferTradeCommand extends BaseCommand {

    /**
     * playerIndex of player offering trade
     */
    private int playerIndex;
    /**
     * list of resources being offered
     */
    private ResourceList offer;
    /**
     * playerIndex of player being offered trade
     */
    private int receiver;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "offerTrade";


    /**
     * Creates OfferTradeCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data memebrs
     *
     * @param index
     * @param off
     * @param rec
     */
    public OfferTradeCommand(int index, ResourceList off, int rec){
        playerIndex = index;
        offer = off;
        receiver = rec;
    }

    /**
     * Creates an willAccept trade command object to invite other user to
     * willAccept or reject offer
     *
     * @param BC
     * @return true if other user accepted offer
     */
    public boolean buildAcceptTradeCommand(BaseCommand BC) {
        return false;
    }

    /**
     * Creates empty OfferTradeCommand
     */
    public OfferTradeCommand() {
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
     * Calls all necessary model update methods
     */
    @Override
    public String serverExec() {
        JSONObject offerTradeJSON = new JSONObject(getRequest());
        playerIndex = offerTradeJSON.getInt("playerIndex");
        receiver = offerTradeJSON.getInt("receiver");

        // creating the vertex object:
        JSONObject offerJSON = offerTradeJSON.getJSONObject("offer");
        ResourceList resourceList = new ResourceList();
        resourceList.setBrickCardCount(offerJSON.getInt("brick"));
        resourceList.setOreCardCount(offerJSON.getInt("ore"));
        resourceList.setSheepCardCount(offerJSON.getInt("sheep"));
        resourceList.setWheatCardCount(offerJSON.getInt("wheat"));
        resourceList.setWoodCardCount(offerJSON.getInt("wood"));
        offer = resourceList;

        OfferTradeCommand command = new OfferTradeCommand(playerIndex, offer, receiver);
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().offerTrade(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().offerTrade(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();
            return true; //it worked
        }
        else{
            System.out.println(">OFFERTRADECMD: reExec(): couldn't re-execute!");
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

    public ResourceList getOffer() {
        return offer;
    }

    public int getReceiver() {
        return receiver;
    }
}
