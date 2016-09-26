package shared.model.player;

/**
 * MaritimeTradeManager facilitates trade using ports that the player owns instead of other players.
 * It checks whether the player has the ports available to trade with, whether they have the resources
 * available to trade, and sends the trade request command to the server proxy via the CommandManager.
 *
 * Created by Sierra on 9/19/16.
 */
public class MaritimeTradeManager {

    /**
     * Whether the user owns an ore port available for trading
     */
    public boolean hasOrePort = false;

    /**
     * Whether the user owns a wheat port available for trading
     */
    public boolean hasWheatPort = false;

    /**
     * Whether the user owns a sheep port available for trading
     */
    public boolean hasSheepPort = false;

    /**
     *  Whether the user owns a wood port available for trading
     */
    public boolean hasWoodPort = false;

    /**
     * Whether the user owns a brick port available for trading
     */
    public boolean hasBrickPort = false;

    /**
     * Whether the user owns a generic (3:1) port available for trading
     */
    public boolean hasThreePort = false;

    public MaritimeTradeManager(){}



    /**
     * CanDoTrade() checks whether the user has resources available to complete the desired trade,
     * and that the port type matches the available resources (or that it's generic)
     *
     * @pre - Is is their turn, they own the correct port type, and they have the resources available
     * @return true if they can do the trade, false if they can't
     */
    public boolean canDoTrade() {
        return false;
    }


    /**
     * Tells CommandManager to create a MaritimeTradeRequest object using the
     * and send it to the ServerProxy/JSONTranslator.
     */
    public void sendTradeRequest(){

    }



}
