package Client.model;

import Client.model.messagemanager.MessageList;
import Client.model.map.Map;
import Client.model.player.Player;
import Client.model.resourcebank.DevCardList;
import Client.model.resourcebank.ResourceList;
import org.json.JSONObject;

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
     * The messageList object holding all the Chat messages
     */
    public MessageList chat;

    /**
     * The messageList object holding all the GameLog messages
     */
    public MessageList log;

    /**
     * the Map object holding all the aspects of the map for this game
     */
    public Map map;

    /**
     * An array of all the players included in this current game.
     */
    public Player[] players;

    /**
     * The current Trade offer, if there is one.
     */
    public TradeOffer tradeOffer;

    /**
     * The ClientModel's ClientUpdateManager, which will take the update objects coming from the JSON translator
     * after the server has sent a newly updated model, and appropriately distribute the
     * updated data throughout the ClientModel.
     */
    public ClientUpdateManager updateManager;






    /**
     * Takes the incoming JSONObject (newModel) coming from the server and forwards it on to
     * ClientUpdateManager so the data can be distributed properly to the objects in ClientModel.
     *
     * @param newModel - the JSON object representing the newly updated ClientModel being sent from the server
     */
    public void UpdateClientModel(JSONObject newModel){

    }


}



