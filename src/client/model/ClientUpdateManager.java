package client.model;

import client.ClientFacade;
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
 * contained within to update the existing ClientModel objects.
 *
 * Created by Sierra on 9/18/16.
 */
public class ClientUpdateManager {

    //The ClientUpdateManager needs to be able to access the current ClientModel stored in the Client??

    ClientModel currentModel;

    //this is ok if we end up going with my new idea!
    //is there a way ClientUpdateManager can go through its superclass (ClientFacade) to talk to its data members?
    //like   ClientFacade clientFacade = super.?
    //it would be nice to not have to have a new instance of JSONTranslator here too...
    JSONTranslator jsonTranslator = new JSONTranslator();

    public ClientUpdateManager (ClientModel currModel) {
        currentModel = currModel;
    }


    /**
     * getNewModel() checks with jsonTranslator to see if the new model coming from the server
     * was parsed successfully. If it was, we save that new ClientModel here and give it to delegateUpdates()
     * to be distributed to the existing model.
     */
    public void getNewModel(){
        /*
        if (jsonTranslator.translateModel()) {

        }
        else {
            System.out.println(">ClientUpdateMgr: unable to get new model obj from JSONTranslator");
        }
        */
    }


    /**
     * DelegateUpdates() takes the newly updated ClientModel object coming from JSONTranslator,
     * who got it from translating the update the server sent,
     * and splits it into smaller objects to give to the subsequent individual update functions.
     */
    public void delegateUpdates(ClientModel newModel){
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
        updateModelVersion(newVersionNum);

        // Trade Offer???
        // REMINDER: Call whatever needs to check for and reassign longest road  and largest army HERE

    }


    /**
     * updateMap() takes the updated Map object from delegateUpdates(), extracts its smaller
     * objects, and sends them to the subsequent small update functions.
     *
     * @param newMap the updated Map object
     */
    private void updateMap(Map currMap, Map newMap) {
        currMap.updateMap(newMap);
    }

//-----------------
    /**
     * updatePlayer() takes the updated Player object from delegateUpdates(), extracts its smaller
     * objects, and sends them to the subsequent small update functions.
     *
     * @param newPlayer the updated Player object
     */
    private void updatePlayer(Player currPlayer, Player newPlayer){
        currPlayer.updatePlayer(newPlayer);
    }

//-----------------

    /**
     * updateMessageManager() takes the updated MessageManager object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newMessageMgr the updated MessageManager object
     */
    private void updateMessageManager(MessageManager currMessageMgr, MessageManager newMessageMgr){
        currMessageMgr.updateMessageManager(newMessageMgr);
    }

//-----------------

    /**
     * updateTurnTracker() takes the updated TurnTracker object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newTurnTracker the updated TurnTracker object
     */
    private void updateTurnTracker(TurnTracker currTurnTracker, TurnTracker newTurnTracker){
        currTurnTracker.updateTurnTracker(newTurnTracker);
    }
//-----------------

    /**
     * updateResourceBank() takes the updated ResourceBank object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newRBank the updated ResourceBank object
     */
    private void updateResourceBank(ResourceBank currResourceBank, ResourceBank newRBank){
        currResourceBank.updateResourceBank(newRBank);
    }

//-----------------

    private void updateModelVersion(int newModelVersion) {
        currentModel.setModelVersion(newModelVersion);
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
