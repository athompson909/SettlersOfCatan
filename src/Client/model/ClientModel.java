package Client.model;

import Client.model.map.Map;
import Client.model.messagemanager.MessageList;
import Client.model.player.Player;
import Client.model.resourcebank.DevCardList;
import Client.model.resourcebank.ResourceList;

/**
 * ClientModel holds all the data about the Client that can be updated and changed by the gameplay
 * and that will be updated every time the ServerPoller requests a copy of the model.
 *
 * Created by Mitchell on 9/15/2016.
 */
public class ClientModel {

    /**
     * The current version of the ClientModel.
     * Updated every time the Poller or user requests a new copy of the ClientModel from the server.
     */
    public int modelVersion = 0;

    /**
     * The index of the player who won the game. -1 if no one has won yet.
     */
    public int gameWinner = -1;

    /**
     * The ResourceList holding all the resources that this player currently has
     */
    public ResourceList bank;

    /**
     * The DevCardList holding all the Dev Cards that this player has that are at lease one turn old
     */
    public DevCardList oldDevCardList;

    /**
     * The DevCardList holding all the Dev Cards that this player has that are brand new,
     * i.e. purchased on the current turn
     */
    public DevCardList newDevCardList;

    /**
     *
     */
    public MessageList chat;

    /**
     *
     */
    public MessageList log;

    /**
     *
     */
    public Map map;

    /**
     *
     */
    public Player[] players;

    /**
     *
     */
    public TradeOffer tradeOffer;

    /**
     *
     */
    public UpdateManager updateManager;

}
