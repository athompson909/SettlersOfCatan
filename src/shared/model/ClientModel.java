package shared.model;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
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
    public int version = 0;

    public int gameNumber;

    /**
     * The index of the player who won the game. -1 if no one has won yet.
     */
    public int winner = -1;

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


    public ClientModel (int gameNumber){
        this.gameNumber = gameNumber;
    }

    /**
     * Takes the incoming JSONObject (newModel) coming from the server and forwards it on to
     * ClientUpdateManager so the data can be distributed properly to the objects in ClientModel.
     *
     * @param newModel - the JSON object representing the newly updated ClientModel being sent from the server
     */
    public void UpdateClientModel(ClientModel newModel){
        updateManager.delegateUpdates(newModel);
    }







    //CAN METHODS
    public boolean canPurchaseRoad(int playerIndex){
        return players[playerIndex].canPurchaseRoad();
    }

    public boolean canPurchaseSettlement(int playerIndex){
        return players[playerIndex].canPurchaseSettlement();
    }

    public boolean canPurchaseCity(int playerIndex){
        return players[playerIndex].canPurchaseCity();
    }

    public boolean canPurchaseDevCard(int playerIndex){
        return (players[playerIndex].canPurchaseDevelopmentCard() && resourceBank.hasDevCards());
    }

    public boolean canPlaySolider(int playerIndex){
        return (players[playerIndex].canPlaySoldierCard());
    }

    public boolean canPlayMonument(int playerIndex){
        return (players[playerIndex].canPlayMonumentCard());
    }

    public boolean canPlayRoadBuilding(int playerIndex){
        return (players[playerIndex].canPlayRoadBuildingCard());
    }

    public boolean canPlayMonopoly(int playerIndex){
        return (players[playerIndex].canPlayMonopolyCard());
    }

    public boolean canPlayYearOfPlenty(int playerIndex){
        return (players[playerIndex].canPlayYearOfPlentyCard());
    }

    //DO METHODS
    public void purchaseRoad(int playerIndex){
        //TODO: Access map and place road
        players[playerIndex].purchaseRoad();
    }

    public void purcahseSettlement(int playerIndex){
        //TODO: Access map and place settlement
        players[playerIndex].purchaseSettlement();
    }

    public void purchaseCity(int playerIndex){
        //TODO: Access map and place city
        players[playerIndex].purchaseCity();
    }

    public void purchaseDevCard(int playerIndex){
        DevCardType purcahsedDevCard = resourceBank.removeRandomDevCard(); //Remove from bank
        players[playerIndex].purchaseDevelopmentCard(purcahsedDevCard); //Send to player
    }

    public void playSoldierCard(int playerIndex){
        //TODO: Access map and move soldier
        players[playerIndex].playSoldierCard();
    }

    public void playMonumentCard(int playerIndex){
        players[playerIndex].playMonumentCard();
    }

    public void playRoadBuildingCard(int playerIndex){
        int roadsUsed = 0;
        if(players[playerIndex].getRoadCount() > 0){
            //TODO: Call maps road building function
            roadsUsed++;
        }
        if(players[playerIndex].getRoadCount() > 0){
            //TODO: Call maps road building function
            roadsUsed++;
        }
        players[playerIndex].playRoadBuildingCard(roadsUsed);
    }

    public void playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2){
        //TODO: Need a method or way that can make sure the bank has those resource cards. Need a case if bank only has 0-1 cards.
        resourceBank.playYearOfPlenty(resource1, resource2);
        players[playerIndex].playYearOfPlentyCard(resource1, resource2);
    }

    public void playMonopolyCard(int recieverPlayerIndex, ResourceType monopolizedResource){
        int totalCardsGained = 0;
        //Take all cards of specified resource from each opposing player
        for(int index = 0; index < players.length; index++){
            if(index != recieverPlayerIndex){
                totalCardsGained += players[index].loseAllCardsOfType(monopolizedResource);
            }
        }
        //Give those cards to the player who used the monopoly card.
        players[recieverPlayerIndex].playMonopolyCard(monopolizedResource, totalCardsGained);
    }


    //ADDITIONAL DO METHODS (With no accompanying can methods)

    /*
    Rulebook: If there are not enough of a resource type, then no one recieve any of that resource
    (unless it only affects one player, then that player gets the remaining resources from the bank)
     */
    public void recieveResourcesFromDiceRoll(){
        //TODO: Go to map and calculate how many cards each player gets

        //TODO: Calculate resource production, and consider special rulebook exception.
        //int total1;
        //int total2;
        //if(resourceBank.getResourceList().listHasAmountOfType(total1,))

        for(int index = 0; index < players.length; index++){

        }
    }

    //GETTERS
    public int getVersion() {return version;}
    public int getWinner() {return winner;}
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
    public void setVersion(int newModVer) {
        version = newModVer;}
    public void setWinner(int newGameWinner) {
        winner = newGameWinner;}
    public void setResourceBank(ResourceBank newResBank) {resourceBank = newResBank;}
    public void setTurnTracker(TurnTracker newTurnTracker) {turnTracker = newTurnTracker;}
    /*Not sure if I can just set the TurnTracker so easily or if I need to break it down into smaller parts*/
    public void setChat(MessageList newChat) {chat = newChat;}
    public void setLog(MessageList newLog) {log = newLog;}
    // ....???? Come back to this

}



