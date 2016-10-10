package shared.model;

import client.ClientFacade;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.turntracker.TurnTracker;
import shared.model.resourcebank.ResourceBank;
import shared.model.messagemanager.MessageManager;


/**
 * ClientUpdateManager receives the new model piece objects created by the JSONTranslator
 * (after the server sends back the updated model), and uses the data
 * contained within to update the existing ClientModel objects.
 * <p>
 * Created by Sierra on 9/18/16.
 */
public class ClientUpdateManager {

    ClientModel currentModel;

    public ClientUpdateManager(ClientModel currModel) {
        currentModel = currModel;
    }

    /**
     * getNewModel() checks with jsonTranslator to see if the new model coming from the server
     * was parsed successfully. If it was, we save that new ClientModel here and give it to delegateUpdates()
     * to be distributed to the existing model.
     */
    public void getNewModel() {

    }

    /**
     * DelegateUpdates() takes the newly updated ClientModel object coming from JSONTranslator,
     * who got it from translating the update the server sent,
     * and splits it into smaller objects to give to the subsequent individual update functions.
     */
    public void delegateUpdates(ClientModel newModel) {
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

        int currVersionNum = currentModel.version;
        int newVersionNum = newModel.version;
        updateModelVersion(newVersionNum);

        // Trade Offer???
        // REMINDER: Call whatever needs to check for and reassign longest road  and largest army HERE

        currentModel.setChanged(true);
        currentModel.notifyObservers();
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
    private void updatePlayer(Player currPlayer, Player newPlayer) {
        currPlayer.updatePlayer(newPlayer);
    }

//-----------------

    /**
     * updateMessageManager() takes the updated MessageManager object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newMessageMgr the updated MessageManager object
     */
    private void updateMessageManager(MessageManager currMessageMgr, MessageManager newMessageMgr) {
        currMessageMgr.updateMessageManager(newMessageMgr);
    }

//-----------------

    /**
     * updateTurnTracker() takes the updated TurnTracker object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newTurnTracker the updated TurnTracker object
     */
    private void updateTurnTracker(TurnTracker currTurnTracker, TurnTracker newTurnTracker) {
        currTurnTracker.updateTurnTracker(newTurnTracker);
    }
//-----------------

    /**
     * updateResourceBank() takes the updated ResourceBank object from delegateUpdates(),
     * extracts its smaller objects, and sends them to the subsequent small update functions.
     *
     * @param newRBank the updated ResourceBank object
     */
    private void updateResourceBank(ResourceBank currResourceBank, ResourceBank newRBank) {
        currResourceBank.updateResourceBank(newRBank);
    }

//-----------------

    private void updateModelVersion(int newModelVersion) {
        currentModel.setVersion(newModelVersion);
    }

    /**
     * updateTradeOffer() gets the updated TradeOffer object from updateMap(),
     * and uses it to update the data in the existing ClientModel's TradeOffer.
     *
     * @param newTradeOffer the new TradeOffer object containing updated data
     */
    private void updateTradeOffer(TradeOffer newTradeOffer) {

    }

}
