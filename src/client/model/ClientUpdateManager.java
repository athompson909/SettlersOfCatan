package client.model;

import client.model.map.Robber;
import client.model.messagemanager.MessageList;
import client.model.map.Map;
import client.model.player.Player;
import client.model.resourcebank.ResourceList;
import client.model.turntracker.TurnTracker;
import client.model.resourcebank.ResourceBank;
import client.model.resourcebank.DevCardList;
import client.model.messagemanager.MessageManager;


/**
 * ClientUpdateManager receives the new model piece objects created by the JSONTranslator
 * (after the server sends back the updated model), and uses the data
 * contained within
 *
 * Created by Sierra on 9/18/16.
 */
public class ClientUpdateManager {

    //The ClientUpdateManager needs to be able to access the current ClientModel stored in the Client??

    ClientModel currentModel;

    public ClientUpdateManager (ClientModel currModel) {
        currentModel = currModel;
    }

    /**
     * DelegateUpdate() takes the newly updated ClientModel object coming from JSONTranslator,
     * who got it from translating the update the server sent,
     * and splits it into smaller objects to give to the subsequent individual update functions.
     */
    public void delegateUpdates(ClientModel currentModel, ClientModel newModel){
        Map currMap = currentModel.map;
        Map newMap = newModel.map;
        updateMap(currMap, newMap);

        Player currPlayer = currentModel.players[0]; /*Index of player] *///////////
        Player newPlayer = newModel.players[0]; /*Index of player */////////
        updatePlayer(currPlayer, newPlayer);

        MessageManager currMessageMgr = currentModel.messageManager;
        MessageManager newMessageMgr = newModel.messageManager;
        updateMessageManager(currMessageMgr, newMessageMgr);

        TurnTracker currTurnTracker = currentModel.turnTracker;
        TurnTracker newTurnTracker = newModel.turnTracker;
        updateTurnTracker(currTurnTracker, newTurnTracker);

        ResourceBank currResourceBank = currentModel.resourceBank;
        ResourceBank newResourceBank = newModel.resourceBank;
        updateResourceBank(currResourceBank, newResourceBank);

        int currVersionNum = currentModel.modelVersion;
        int newVersionNum = newModel.modelVersion;
        updateModelVersion(currVersionNum, newVersionNum);

        // Trade Offer???
    }


    /**
     * updateMap() takes the updated Map object from delegateUpdates(), extracts its smaller
     * objects, and sends them to the subsequent small update functions.
     *
     * @param newMap the updated Map object
     */
    private void updateMap(Map currMap, Map newMap) {
        currMap.setRobber(newMap.getRobber());
        currMap.setRoads(newMap.getRoads());
        currMap.setSettlements(newMap.getSettlements());
        currMap.setCities(newMap.getCities());
    }

//-----------------
    /**
     * updatePlayer() takes the updated Player object from delegateUpdates(), extracts its smaller
     * objects, and sends them to the subsequent small update functions.
     *
     * @param newPlayer the updated Player object
     */
    private void updatePlayer(Player currPlayer, Player newPlayer){

    }

    /**
     * updateResourceList() gets the updated ResourceList object from updateMap(),
     * and uses it to update the data in the existing ClientModel's ResourceList.
     *
     * @param newRList the new ResourceList object containing updated data
     */
    private void updateResourceList(ResourceList newRList){

    }

    /**
     * updateDevCardList() gets the updated DevCardList object from updateMap(),
     * and uses it to update the data in the existing ClientModel's DevCardList.
     *
     * @param newDCList the new DevCardList object containing updated data
     */
    private void updateDevCardList(DevCardList newDCList){

    }
//-----------------

    /**
     * updateMessageManager() takes the updated MessageManager object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newMMgr the updated MessageManager object
     */
    private void updateMessageManager(MessageManager currMessageMgr, MessageManager newMMgr){

    }

    /**
     * updateChat() gets the updated Chat MessageList object from updateMap(),
     * and uses it to update the data in the existing ClientModel's Chat MessageList.
     *
     * @param newChatList the new Chat MessageList object containing updated data
     */
    private void updateChat(MessageList newChatList) {

    }

    /**
     * updateGameLog() gets the updated GameLog MessageList object from updateMap(),
     * and uses it to update the data in the existing ClientModel's GameLog MessageList.
     *
     * @param newGameLog the new GameLog MessageList object containing updated data
     */
    private void updateGameLog(MessageList newGameLog) {

    }
//-----------------

    /**
     * updateTurnTracker() takes the updated TurnTracker object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newTurnTracker the updated TurnTracker object
     */
    private void updateTurnTracker(TurnTracker currTurnTracker, TurnTracker newTurnTracker){

    }
//-----------------

    /**
     * updateResourceBank() takes the updated ResourceBank object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newRBank the updated ResourceBank object
     */
    private void updateResourceBank(ResourceBank currResourceBank, ResourceBank newRBank){

    }

    /**
     * updateBankResourceList() gets the updated ResourceList object from updateMap(),
     * and uses it to update the data in the existing ResourceBank's ResourceList.
     *
     * @param newRList the new ResourceList object containing updated data
     */
    private void updateBankResourceList(ResourceList newRList){

    }

    /**
     * updateBankDevCardList() gets the updated DevCardList object from updateMap(),
     * and uses it to update the data in the existing ResourceBank's DevCardList.
     *
     * @param newDCList the new DevCardList object containing updated data
     */
    private void updateBankDevCardList(DevCardList newDCList){

    }
//-----------------

    private void updateModelVersion(int currVersionNum, int newVersionNum) {

    }
    /**
     * updateTradeOffer() gets the updated TradeOffer object from updateMap(),
     * and uses it to update the data in the existing ClientModel's TradeOffer.
     *
     * @param newTradeOffer the new TradeOffer object containing updated data
     */
    private void updateTradeOffer(TradeOffer newTradeOffer){

    }

}
