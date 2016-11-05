package shared.model.commandmanager.moves;

import org.json.JSONObject;
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
     * Calls all necessary model update methods
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId) {

        return null;
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
