package shared.model;

import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceBank;
import shared.model.turntracker.TurnTracker;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


/**
 * ClientModel holds all the data about the client that can be updated and changed by the gameplay
 * and that will be updated every time the ServerPoller requests a copy of the model.
 *
 * Created by Mitchell on 9/15/2016.
 *
 * from spec: "1. Use the Observer pattern to make your model observable. Each time the model is updated
 from the server, it should notify its observers."
 */
public class ClientModel extends Observable {

    /**
     * The current version of the ClientModel
     * .
     * Updated every time the Poller or user requests a new copy of the ClientModel from the server.
     */
    public int version = 0;

    /**
     * The current games specific number
     */
    public int gameNumber;

    /**
     * The index of the player who won the game. -1 if no one has won yet.
     */
    public int winner = -1;
    public boolean changed = false;
    public ResourceBank resourceBank = new ResourceBank();
    public MessageManager messageManager;
    public TurnTracker turnTracker;
    /**
     * The messageList object holding all the Chat messages
     */
    public MessageList chat = new MessageList();

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

    //Constructor
    public ClientModel (int gameNumber){
        this.gameNumber = gameNumber;
        map = new Map(false, false, false);

        messageManager = new MessageManager();

        //Temporary making 4 default players so we can test stuff - Mitch
        players = new Player[4];
        //players[0] = new Player(CatanColor.ORANGE, "Bob", "ID0", 0);
        //players[1] = new Player(CatanColor.BLUE, "Carl", "ID1", 1);
       // players[2] = new Player(CatanColor.GREEN, "Don", "ID2", 2);
        //players[3] = new Player(CatanColor.RED, "Egbert", "ID3", 3);

        tradeOffer = new TradeOffer();
        turnTracker = new TurnTracker();

        //this.addObserver(Controller); //How do we get a reference to the controller?
    }

    /**
     * Takes the incoming JSONObject (newModel) coming from the server and forwards it on to
     * ClientUpdateManager so the data can be distributed properly to the objects in ClientModel.
     *
     * @param newModel - the JSON object representing the newly updated ClientModel being sent from the server
     */
    public void updateClientModel(ClientModel newModel){
        updateManager.delegateUpdates(newModel);
    }



    //CAN METHODS

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purchase a road.
     */
    public boolean canPurchaseRoad(int playerIndex){
        return players[playerIndex].canPurchaseRoad();
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purchase a settlement.
     */
    public boolean canPurchaseSettlement(int playerIndex){
        return players[playerIndex].canPurchaseSettlement();
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purchase a city.
     */
    public boolean canPurchaseCity(int playerIndex){
        return players[playerIndex].canPurchaseCity();
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purcahse a development card.
     */
    public boolean canPurchaseDevCard(int playerIndex){
        return (players[playerIndex].canPurchaseDevelopmentCard() && resourceBank.hasDevCards());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can play a soldier card.
     */
    public boolean canPlaySolider(int playerIndex){
        return (players[playerIndex].canPlaySoldierCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can play a monument card.
     */
    public boolean canPlayMonument(int playerIndex){
        return (players[playerIndex].canPlayMonumentCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can play a road building card.
     */
    public boolean canPlayRoadBuilding(int playerIndex){
        return (players[playerIndex].canPlayRoadBuildingCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the palyer can play a monopoly card.
     */
    public boolean canPlayMonopoly(int playerIndex){
        return (players[playerIndex].canPlayMonopolyCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the palyer can play a year of plenty card.
     */
    public boolean canPlayYearOfPlenty(int playerIndex){
        return (players[playerIndex].canPlayYearOfPlentyCard());
    }

    /**
     * @param playerIndex of player performing trade
     * @return true if player has cards to trade
     * IN THE FUTURE: This should return an array of integers that lists
     * the number of cards of each resource type available
     */
    public boolean canDomesticTrade(int playerIndex) {
        // 0:WOOD, 1:WHEAT, 2:BRICK, 3:ORE, 4:SHEEP
        return players[playerIndex].canDomesticTrade();
    }

    /**
     * @param playerIndex of player performing trade
     * @return true if player owns ports to trade from and enough cards to trade
     * IN THE FUTURE: This should return a set of PortTypes that player is able to trade
     */
    public HashMap<PortType, boolean[]> canMaritimeTrade(int playerIndex) {
        Set<PortType> ports = map.getPlayersPorts(playerIndex);
        players[playerIndex].getMaritimeTradeManager().setPorts(ports);
        HashMap<PortType, boolean[]> enoughCards = players[playerIndex].canMaritimeTrade(ports);
        return enoughCards;
    }

    //Map can Methods

    /**
     * @param playerIndex of player performing the action.
     * @param edgeLocation to place the road.
     * @return true if the player can place a road at the specified edgeLocation.
     */
    public boolean canPlaceRoad(int playerIndex, EdgeLocation edgeLocation){
        return map.buildRoadManager.canPlace(playerIndex, edgeLocation);
    }

    /**
     * @param playerIndex of player performing the action.
     * @param edgeLocation to place the road.
     * @return true if the specified edge is not taken.
     */
    public boolean canPlaceRoadDuringSetUp(int playerIndex, EdgeLocation edgeLocation){
        return map.buildRoadManager.canPlaceSetUpRound(playerIndex, edgeLocation);
    }

    /**
     * @param playerIndex of player performing the action.
     * @param vertexLocation to place the settlement.
     * @return true if the player can place a settlement at the specified vertexLocation
     */
    public boolean canPlaceSettlement(int playerIndex, VertexLocation vertexLocation){
        return map.buildSettlementManager.canPlace(playerIndex, vertexLocation);
    }

    /**
     * @param playerIndex of player performing the action.
     * @param vertexLocation to place the city.
     * @return true if the player can place a city at the specified vertexLocation
     */
    public boolean canPlaceCity(int playerIndex,  VertexLocation vertexLocation ){
        return map.buildCityManager.canPlaceCity(playerIndex, vertexLocation);

    }

    /**
     * @param desiredHexLoc to place the robber.
     * @return true if its a new location that's not water.
     */
    public boolean canPlaceRobber(HexLocation desiredHexLoc){
        return map.canPlaceRobber(desiredHexLoc);
    }


    //DO METHODS

    /**
     * Purchase and place a road.
     * @param playerIndex of player performing the action.
     * @param edgeLocation of where to place the road.
     */
    public void purchaseAndPlaceRoad(int playerIndex, EdgeLocation edgeLocation){
        players[playerIndex].purchaseRoad();
        map.buildRoadManager.placeRoad(playerIndex, edgeLocation);
    }

    /*
    public void placeInitialRoad(){
        map.buildRoadManager.placeRoad(playerIndex, edgeLocation);
    }
*/

    /**
     * Purchase and place a settlement.
     * @param playerIndex
     * @param vertexLocation of where to place settlement.
     */
    public void purchaseAndPlaceSettlement(int playerIndex, VertexLocation vertexLocation){
        players[playerIndex].purchaseSettlement();
        map.buildSettlementManager.placeSettlement(playerIndex, vertexLocation);
    }

    /**
     * Purchase and place a city.
     * @param playerIndex
     * @param vertexLocation of where to place the city.
     */
    public void purchaseAndPlaceCity(int playerIndex, VertexLocation vertexLocation){
        players[playerIndex].purchaseCity();
        map.buildCityManager.placeCity(playerIndex, vertexLocation);
    }

    /**
     * Purchase a dev card from the bank.
     * @param playerIndex of player purchasing the card.
     */
    public void purchaseDevCard(int playerIndex){
        DevCardType purcahsedDevCard = resourceBank.removeRandomDevCard(); //Remove from bank
        players[playerIndex].purchaseDevelopmentCard(purcahsedDevCard); //Send to player

    }

    /**
     * Soldier card functionality.
     * @param playerIndex
     */
    public void playSoldierCard(int playerIndex){
        //TODO: Access map and move soldier
        //TODO: Steal random card from potential players
        players[playerIndex].playSoldierCard();
    }

    /**
     * Play a monument card.
     * @param playerIndex
     */
    public void playMonumentCard(int playerIndex){
        players[playerIndex].playMonumentCard();
    }

    /**
     * Play a road building card.
     * @param playerIndex
     */
    public void playRoadBuildingCard(int playerIndex){
        int roadsUsed = 0;
        if(players[playerIndex].getRoadCount() > 0){
            roadsUsed++;
        }
        if(players[playerIndex].getRoadCount() > 0){
            //TODO: Call maps road building function
            roadsUsed++;
        }
        players[playerIndex].playRoadBuildingCard(roadsUsed);
    }

    /**
     * Play a year of plenty card.
     * @param playerIndex
     * @param resource1
     * @param resource2
     */
    public void playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2){
        //TODO: Need a method or way that can make sure the bank has those resource cards. Need a case if bank only has 0-1 cards.
        resourceBank.playYearOfPlenty(resource1, resource2);
        players[playerIndex].playYearOfPlentyCard(resource1, resource2);
    }

    /**
     * Play Monopoly card.
     * @param recieverPlayerIndex
     * @param monopolizedResource
     */
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

    /**
     * Places the robber at the desired Hex Location.
     * @param desiredHexLoc to place the robber.
     */
    public void placeRobber(HexLocation desiredHexLoc){
        map.placeRobber(desiredHexLoc);
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
    public boolean getChanged() {return changed;}
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
    public Player getCurrentPlayer() {return players[turnTracker.getCurrentTurn()];}

    //SETTERS
    public void setChanged(boolean set) {
        notifyObservers();//the observers need to be aware of the change//todo: test
        changed = set;
    }
    public void setVersion(int newModVer) {version = newModVer;}
    public void setWinner(int newGameWinner) {
        winner = newGameWinner;}
    public void setResourceBank(ResourceBank newResBank) {resourceBank = newResBank;}

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void setTradeOffer(TradeOffer tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTurnTracker(TurnTracker newTurnTracker) {turnTracker = newTurnTracker;}
    /*Not sure if I can just set the TurnTracker so easily or if I need to break it down into smaller parts*/
    public void setChat(MessageList newChat) {chat = newChat;}
    public void setLog(MessageList newLog) {log = newLog;}



    // observable override methods

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }

    @Override
    public void notifyObservers() {

        super.notifyObservers();
    }

    /**
     * I'm added an increment to the version everytime here because I think this goes eeverytime the model is changed
     * @param arg
     */
    @Override
    public void notifyObservers(Object arg) {
        version++;
        super.notifyObservers(arg);
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged();
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged();
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers();
    }
}



