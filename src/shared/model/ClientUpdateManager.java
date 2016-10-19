package shared.model;

import client.Client;
import shared.model.map.Map;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceBank;
import shared.model.turntracker.TurnTracker;


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

    public void setCurrentModel(ClientModel currentModel) {
        this.currentModel = currentModel;
    }

    /**
     * DelegateUpdates() takes the newly updated ClientModel object coming from JSONTranslator,
     * who got it from translating the update the server sent,
     * and splits it into smaller objects to give to the subsequent individual update functions.
     */
    public void delegateUpdates(ClientModel newModel) {
        System.out.println(">delegateUpdates called!");

        ResourceBank currResourceBank = currentModel.resourceBank;
        ResourceBank newResourceBank = newModel.resourceBank;
        updateResourceBank(currResourceBank, newResourceBank);

        MessageManager currMessageMgr = currentModel.messageManager;
        MessageManager newMessageMgr = newModel.messageManager;
        updateMessageManager(currMessageMgr, newMessageMgr);

        Map currMap = currentModel.map;
        Map newMap = newModel.map;
        updateMap(currMap, newMap);

        /*TODO: This is where have a problem. Sierra/Adam, talk to me (Mitch) about this.
        Stephanie and I made the clientmodel initialize with 4 empty players so the players array in the clientmodel
        wasn't null, that way it doesn't crash in these following functions. However, not that the client model has 4
        players, when you click join game, it doesn't let you add players or computers because there's already 4 players.
        We need to talk about how to approach this.
         */
        Player[] currPlayers = currentModel.getPlayers();
        Player[] newPlayers = newModel.getPlayers();
        updatePlayers(currPlayers, newPlayers);

        //why are we not updating chat?  -Adam... would this update function work?
        currentModel.setChat(newModel.getChat());
        currentModel.setLog(newModel.getLog());


        TradeOffer currTradeOffer = currentModel.tradeOffer;
        TradeOffer newTradeOffer = newModel.tradeOffer;
        updateTradeOffer(currTradeOffer, newTradeOffer);//shows as (null, null)

        TurnTracker currTurnTracker = currentModel.turnTracker;
        TurnTracker newTurnTracker = newModel.turnTracker;
        updateTurnTracker(currTurnTracker, newTurnTracker);

        int currVersionNum = currentModel.version;
        int newVersionNum = newModel.version;
        updateModelVersion(newVersionNum);

        currentModel.setWinner(newModel.getWinner());

        currentModel.setChanged();
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
     * @param newPlayers the updated Players object
     */
    private void updatePlayers(Player currPlayers[], Player newPlayers[]) {
        for(int i = 0; i < newPlayers.length; i++) {
            if(newPlayers[i] != null) {
                /*The problem with this statement below is if, for example, currPlayers[3] is null and we're trying to update
                him to be newPlayer[3], its going to crash because currPlayer[3] is null, and you can't call functions
                on null objects.*/

                //currPlayers[i].updatePlayer(newPlayers[i]);  //Read explanation above

                //So can we just do this instead?
                currPlayers[i] = newPlayers[i];
            }
        }
    }
//-----------------

    //TESTING
    public void testForceUpdatePlayersList(Player[] newPlayersArr){
        if (currentModel.getPlayers() != newPlayersArr) {

            //put the new list of players in for the old one
            currentModel.setPlayers(newPlayersArr);

            System.out.print(">CUM: testForceUpdatePL: newPlayersArr= ");

            for (int i = 0; i < currentModel.getPlayers().length; i++) {
                if (currentModel.getPlayers()[i] != null) {
                    System.out.print(currentModel.getPlayers()[i].getName() + ", ");
                }
            }
            System.out.println();

            //this?
            currentModel.setChanged();
            currentModel.setChanged(true);
            currentModel.notifyObservers();
            //ClientModel is now part of Client Singleton. so PWC can ask the singleton for the new list of players.
        }
        else {
            System.out.println(">CUM: testForceUpdatePL: arrs were the same");
        }
    }


    private void updateTradeOffer(TradeOffer currTradeOffer, TradeOffer newTradeOffer) {
        currTradeOffer.updateTradeOffer(newTradeOffer);
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
        Client.getInstance().updateState();
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

//-----------------


}
