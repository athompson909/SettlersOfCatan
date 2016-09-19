package Client.model;

import Client.model.ClientModel;
import Client.model.TradeOffer;
import Client.model.map.Map;
import Client.model.map.Robber;
import Client.model.messagemanager.MessageList;
import Client.model.player.Player;
import Client.model.resourcebank.ResourceList;
import Client.model.turntracker.TurnTracker;
import Client.model.resourcebank.ResourceBank;
import Client.model.resourcebank.DevCardList;
import Client.model.messagemanager.MessageManager;


/**
 * Client.model.UpdateManager receives the new model piece objects created by the JSONTranslator
 * (after the server sends back the updated model), and uses the data
 * contained within
 *
 * Created by Sierra on 9/18/16.
 */
public class UpdateManager {


    /**
     * DelegateUpdate() takes the newly updated ClientModel object coming from JSONTranslator,
     * who got it from translating the update the server sent,
     * and splits it into smaller objects to give to the subsequent individual update functions.
     */
    private void delegateUpdates(ClientModel newClientModel){
    }


    /**
     * updateMap() takes the updated Map object from delegateUpdates(), extracts its smaller
     * objects, and sends them to the subsequent small update functions.
     *
     * @param newMap the updated Map object
     */
    private void updateMap(Map newMap){

    }

    /**
     * updateRobber() gets the updated Robber object from updateMap(),
     * and uses it to update the data in the existing ClientModel's Robber.
     *
     * @param newRobber the new Robber object containing updated data
     */
    private void updateRobber(Robber newRobber){

    }

    /**
     * updateRoads() gets the updated array of Roads data from updateMap(),
     * and uses it to update the data in the existing ClientModel.
     *
     * @param newRoads the array of new Roads data
     */
    private void updateRoads(int[] newRoads){

    }

    /**
     * updateSettlements() gets the updated array of Settlements data from updateMap(),
     * and uses it to update the data in the existing ClientModel.
     *
     * @param newStlmts the array of new Settlements data
     */
    private void updateSettlements(int[] newStlmts){

    }

    /**
     * updateCities() gets the updated array of Cities data from updateMap(),
     * and uses it to update the data in the existing ClientModel.
     *
     * @param newCities the array of new Cities data
     */
    private void updateCities(int[] newCities){

    }

//-----------------
    /**
     * updatePlayer() takes the updated Player object from delegateUpdates(), extracts its smaller
     * objects, and sends them to the subsequent small update functions.
     *
     * @param newPlayer the updated Player object
     */
    private void updatePlayer(Player newPlayer){

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
    private void updateMessageManager(MessageManager newMMgr){

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
    private void updateTurnTracker(TurnTracker newTurnTracker){

    }
//-----------------

    /**
     * updateResourceBank() takes the updated ResourceBank object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newRBank the updated ResourceBank object
     */
    private void updateResourceBank(ResourceBank newRBank){

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

    /**
     * updateTradeOffer() gets the updated Client.model.TradeOffer object from updateMap(),
     * and uses it to update the data in the existing ClientModel's Client.model.TradeOffer.
     *
     * @param newTradeOffer the new Client.model.TradeOffer object containing updated data
     */
    private void updateTradeOffer(TradeOffer newTradeOffer){

    }

}
