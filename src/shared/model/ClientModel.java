package shared.model;

import shared.model.messagemanager.MessageList;
import shared.model.map.Map;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceBank;
import shared.model.turntracker.TurnTracker;

/**
 * ClientModel holds all the data about the client that can be updated and changed by the gameplay
 * and that will be updated every time the ServerPoller requests a copy of the model.
 *
 * Created by Mitchell on 9/15/2016.
 */
public class ClientModel {

    /**
     * The current version of the ClientModel
     * .
     * Updated every time the Poller or user requests a new copy of the ClientModel from the server.
     */
    public int modelVersion = 0;

    /**
     * The index of the player who won the game. -1 if no one has won yet.
     */
    public int gameWinner = -1;

    public ResourceBank resourceBank;
    public MessageManager messageManager;
    public TurnTracker turnTracker;
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
    public void UpdateClientModel(ClientModel newModel){
        updateManager.delegateUpdates(newModel);
    }

    //GETTERS
    public int getModelVersion() {return modelVersion;}
    public int getGameWinner() {return gameWinner;}
    public ResourceBank getResourceBank() {return resourceBank;}
    public MessageManager getMessageManager() {return messageManager;}
    public TurnTracker getTurnTracker() {return turnTracker;}
    public MessageList getChat() {return chat;}
    public MessageList getLog() {return log;}
    public Map getMap() {return map;}
    public Player[] getPlayer() {return players;}
    public TradeOffer getTradeOffer() {return tradeOffer;}
    public ClientUpdateManager getUpdateManager() {return updateManager;}

    //SETTERS
    public void setModelVersion(int newModVer) {modelVersion = newModVer;}
    public void setGameWinner(int newGameWinner) {gameWinner = newGameWinner;}
    public void setResourceBank(ResourceBank newResBank) {resourceBank = newResBank;}
    public void setTurnTracker(TurnTracker newTurnTracker) {turnTracker = newTurnTracker;}
    /*Not sure if I can just set the TurnTracker so easily or if I need to break it down into smaller parts*/
    public void setChat(MessageList newChat) {chat = newChat;}
    public void setLog(MessageList newLog) {log = newLog;}
    // ....???? Come back to this

}



