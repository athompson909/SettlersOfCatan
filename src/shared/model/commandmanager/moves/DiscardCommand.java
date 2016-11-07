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
public class DiscardCommand extends BaseCommand {

    /**
     * Index of player who is discarding
     */
    private int playerIndex;
    /**
     * list of cards player is discarding
     */
    private ResourceList discardedCards;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private String type;

    /**
     * Creates empty DiscardCommand
     */
    public DiscardCommand() {}

    /**
     * Creates DiscardCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members
     * @param index
     * @param discarded
     */
    public DiscardCommand(int index, ResourceList discarded){
        playerIndex = index;
        discardedCards = discarded;
        type = "discardCards";
    }

    /**
     * Calls all neccessary model update methods
     *
     * Calls player updates methods to reflect new hand
     *
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId) {
        ClientModel model = IServerFacade.getInstance().discardCards(userId, gameId, this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }


    //Getters and Setters
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public ResourceList getDiscardedCards() {
        return discardedCards;
    }

    public void setDiscardedCards(ResourceList discardedCards) {
        this.discardedCards = discardedCards;
    }
}
