package shared.model;

import client.ClientUser;
import client.data.RobPlayerInfo;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;
import shared.model.map.VertexObject;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.*;


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
    private int version = 0;

    /**
     * The current games specific number
     */
    private int gameNumber;

    /**
     * The index of the player who won the game. -1 if no one has won yet.
     */
    private int winner = -1;
    private boolean changed = false;
    private ResourceBank resourceBank;
    private MessageManager messageManager;
    private TurnTracker turnTracker;
    /**
     * The messageList object holding all the Chat messages
     */
    private MessageList chat = new MessageList();

    /**
     * The messageList object holding all the GameLog messages
     */
    private MessageList log = new MessageList();

    /**
     * the Map object holding all the aspects of the map for this game
     */
    private Map map;

    /**
     * An array of all the players included in this current game.
     */
    private Player[] players;

    /**
     * The current Trade offer, if there is one.
     */
    private TradeOffer tradeOffer;


    /**
     * The ClientModel's ClientUpdateManager, which will take the update objects coming from the JSON translator
     * after the server has sent a newly updated model, and appropriately distribute the
     * updated data throughout the ClientModel.
     */
    private ClientUpdateManager updateManager;

    //Constructor
    public ClientModel (int gameNumber){
        this.gameNumber = gameNumber;
        map = new Map(false, false, false);
        messageManager = new MessageManager();
        //Temporary making 4 default players so we can test stuff - Mitch
        players = new Player[4];
        tradeOffer = null;
        turnTracker = new TurnTracker();
        resourceBank = new ResourceBank();

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
    public boolean canOfferTrade(int playerIndex, ResourceList offer, int receiverIndex) {
        // 0:WOOD, 1:WHEAT, 2:BRICK, 3:ORE, 4:SHEEP

        //must be their turn
        //receiverIndex is not them and is a valid index
        //need to check they have the values offering- positive ones
        if(playerIndex != receiverIndex && receiverIndex >=0 && receiverIndex < 4) {
            if(turnTracker.getCurrentTurn() == playerIndex) {
                return players[playerIndex].canOfferTrade(offer);
            }
        }
        return false;
    }

    public boolean canAcceptTrade(int index){
        //valid index
        if(index >=0 && index < 4){
            return players[index].canAcceptTrade(tradeOffer.getTradeOfferList());
        }
        return false;
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

    /**
     * @param playerIndex of player performing the action.
     * @param edgeLocation to place the road.
     * @return true if the player can place a road at the specified edgeLocation.
     */
    public boolean canPlaceRoad(int playerIndex, EdgeLocation edgeLocation){
        return map.buildRoadManager.canPlace(playerIndex, edgeLocation);
    }

    /**
     * @param edgeLocation to place the road.
     * @return true if the specified edge is not taken.
     */
    public boolean canPlaceSetUpRoad(EdgeLocation edgeLocation, VertexLocation firstVertexLocation){
        return map.buildRoadManager.canPlaceSetUpRound(edgeLocation, firstVertexLocation);
    }

    /**
     * @param playerIndex of player performing the action.
     * @param vertexLocation to place the settlement.
     * @return true if the player can place a settlement at the specified vertexLocation
     */
    public boolean canPlaceSettlement(int playerIndex, VertexLocation vertexLocation){
        return map.buildSettlementManager.canPlace(playerIndex, vertexLocation);
    }

    public boolean canPlaceSetUpSettlement(int playerIndex, VertexLocation vertexLocation){
        return map.buildSettlementManager.canPlaceSetUp(playerIndex, vertexLocation);
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
     * Player building a new road.
     * @param edgeLocation EdgeValue, which contains the player ID and location of the road.
     */
    public void buildRoad(EdgeLocation edgeLocation, int index, boolean free) {
        map.buildRoadManager.placeRoad(index, edgeLocation);
        if(!free){
            players[index].purchaseRoad();
        }
        recalculateLongestRoad(index);
    }

    /**
     * Called each time a road is built, and recalculates who now has the longest road.
     * The model is adjusted accordingly.
     */
    private void recalculateLongestRoad(int index){
        int playerWithLongestRoadIndex = turnTracker.getLongestRoadHolder();
        if(playerWithLongestRoadIndex == -1) {
            turnTracker.setLongestRoadHolder(index);
            return;
        }
        if(playerWithLongestRoadIndex != index){
            //If the player who built the road now has less available roads, then they have the most used road pieces.
            if(players[index].getAvailableRoadCount() < players[playerWithLongestRoadIndex].getAvailableRoadCount()){
                turnTracker.setLongestRoadHolder(index);
                //TODO: Where should the victory points be adjusted?
            }
        }
    }

    /**
     * Player building a settlement.
     * @param newSettlement VertexObject, which contains the player ID and location of the settlement.
     */
    public void buildSettlement(VertexObject newSettlement, boolean free){
        map.buildSettlementManager.placeSettlement(newSettlement);
        if(!free){
            players[newSettlement.getOwner()].purchaseSettlement();
        }
    }

    /**
     * Player building a city.
     * @param newCity VertexObject, which contains the player ID and location of the City.
     */
    public void buildCity(VertexObject newCity){
        map.buildCityManager.placeCity(newCity);
        players[newCity.getOwner()].purchaseCity();
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
     * @param index
     */
    public void playSoldierCard(int index, HexLocation robberLocation, int victimIndex){
        players[index].playSoldierCard();
        placeRobber(index, robberLocation, victimIndex);
        recalculateLargestArmy(index);
    }

    /**
     * Places the robber at the desired Hex Location.
     * @param robberLocation to place the robber.
     */
    public void placeRobber(int index, HexLocation robberLocation, int victimIndex){
        map.placeRobber(robberLocation);
        ResourceType stolenResource = players[victimIndex].getPlayerResourceList().removeRandomCard();
        players[index].getPlayerResourceList().addCardByType(stolenResource);
    }

    /**
     * Calculates which players are adjacent to a hex, how many cards they have, and other important
     * information for RobPlayerInfo.
     * @param hexLoc that is getting robbed.
     * @return An array of RobPLayerInfo.
     */
    public RobPlayerInfo[] calculateRobPlayerInfo(HexLocation hexLoc){
        ArrayList<Integer> adjacentPlayers = map.getPlayersAdjacentToHex(hexLoc);

        //Remove the current player from the robbing list.
        if(adjacentPlayers.contains(getCurrentPlayer().getPlayerIndex())){
            Integer currentPlayerIndex= getCurrentPlayer().getPlayerIndex();
            adjacentPlayers.remove(currentPlayerIndex);

        }

        //Remove players that have 0 cards
        ArrayList<Integer> adjacentPlayersWithCards = new ArrayList<>();
        for(int i=0; i < adjacentPlayers.size(); i++){
            if(players[adjacentPlayers.get(i)].getPlayerResourceList().getCardCount() > 0){
                adjacentPlayersWithCards.add(adjacentPlayers.get(i));
            }
        }

        //Create the Array of RobberPlayerInfo
        RobPlayerInfo[] victims = new RobPlayerInfo[adjacentPlayersWithCards.size()];
        for(int i=0; i < adjacentPlayersWithCards.size(); i++){
            int playerIndex = adjacentPlayersWithCards.get(i);
            victims[i] = new RobPlayerInfo();
            victims[i].setPlayerIndex(playerIndex);
            victims[i].setNumCards(getPlayers()[playerIndex].getPlayerResourceList().getCardCount());
            victims[i].setName(getPlayers()[playerIndex].getName());
            victims[i].setColor(getPlayers()[playerIndex].getColor());
        }

        return victims;
    }

    /**
     * Called each time a soldier card is played, and recalculates who now has the largest army.
     * The model is adjusted accordingly.
     */
    private void recalculateLargestArmy(int index){
        int playerWithLargestArmyIndex = turnTracker.getLargestArmyHolder();
        if(playerWithLargestArmyIndex == -1){
            turnTracker.setLargestArmyHolder(index);
            return;
        }
        if(playerWithLargestArmyIndex != index){
            //If the player who built the road now has less available roads, then they have the most used road pieces.
            if(players[index].getAvailableRoadCount() < players[playerWithLargestArmyIndex].getAvailableRoadCount()){
                turnTracker.setLongestRoadHolder(index);
                //TODO: Where should the victory points be adjusted?
            }
        }
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
    public void playRoadBuildingCard(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2){
        int roadsUsed = 0;
        if(edgeLocation1 != null) {
            buildRoad(edgeLocation1, playerIndex, true);
            roadsUsed++;
        }
        if(edgeLocation2 != null) {
            buildRoad(edgeLocation2, playerIndex, true);
            roadsUsed++;
        }
        players[playerIndex].playRoadBuildingCard(roadsUsed);
        recalculateLongestRoad(playerIndex);
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
     * @param receiverPlayerIndex
     * @param monopolizedResource
     */
    public void playMonopolyCard(int receiverPlayerIndex, ResourceType monopolizedResource){
        int totalCardsGained = 0;
        //Take all cards of specified resource from each opposing player
        for(int index = 0; index < players.length; index++){
            if(index != receiverPlayerIndex){
                totalCardsGained += players[index].loseAllCardsOfType(monopolizedResource);
            }
        }
        //Give those cards to the player who used the monopoly card.
        players[receiverPlayerIndex].playMonopolyCard(monopolizedResource, totalCardsGained);
    }

    /**
    Rulebook: If there are not enough of a resource type, then no one receive any of that resource
    (unless it only affects one player, then that player gets the remaining resources from the bank)
     */
    public void receiveResourcesFromDiceRoll(int diceRoll){
        //TODO: Go to map and calculate how many cards each player gets


        //TODO: Calculate resource production, and consider special rulebook exception.
        //int total1;
        //int total2;
        //if(resourceBank.getResourceList().listHasAmountOfType(total1,))



        for(int index = 0; index < players.length; index++){

        }
    }

    /**
     * Send a chat message.
     * @param index of the player sending the message.
     * @param message the player wants to display.
     */
    public void sendChat(int index, String message){

    }

    /**
     * Discarding cards from rolling a 7
     * @param index of the player discarding.
     * @param discarded cards the player has selected to discard.
     */
    public void discardCards(int index, ResourceList discarded){

    }

    /**
     * Player offering a trade.
     * @param index of the player offering the trade.
     * @param off resource list offer.
     * @param receiverIndex index of the player receiving the offer
     */
    public boolean offerTrade(int index, ResourceList off, int receiverIndex){
        if(canOfferTrade(index, off, receiverIndex)){
            tradeOffer = new TradeOffer(index, receiverIndex, off);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Player choosing whether or not to accept a trade.
     * @param index of the player choosing.
     * @param accept returns true if they accept.
     */
    public boolean acceptTrade(int index, boolean accept){
        if(!accept){
            tradeOffer = null;
            return true;
        }else if(canAcceptTrade(index)){
            //switch resources

            return true;
        }
        return false;
    }

    /**
     * Maratime Trade Request
     * @param index of the player trading.
     * @param ratio of the trade.
     * @param inputResource to trade.
     * @param outputResource to recieve.
     */
    public void martimeTrade(int index, int ratio, ResourceType inputResource, ResourceType outputResource){

    }

    /**
     * Finishes the players turn, and changes to the next turn.
     * @param index of the player ending their turn.
     */
    public void finishTurn(int index){

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
    public Player getCurrentPlayer() {return players[ClientUser.getInstance().getIndex()];}
    public int getGameNumber() {
        return gameNumber;
    }
    public Player[] getPlayers() {
        return players;
    }

    //SETTERS
    public void setChanged(boolean set) {changed = set;}
    public void setVersion(int newModVer) {version = newModVer;}
    public void setWinner(int newGameWinner) {winner = newGameWinner;}
    public void setResourceBank(ResourceBank newResBank) {resourceBank = newResBank;}
    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }
    public void setMap(Map map) {
        this.map = map;
    }
    public void setPlayers(Player[] players) {
        this.players = players;

        System.out.print("\t\t>CLIENTMODEL: just set PlayersArr with new content: ");
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                System.out.print(players[i].getName() + ", ");
            }
        }
        System.out.println();
    }
    public void setTradeOffer(TradeOffer tradeOffer) {
        this.tradeOffer = tradeOffer;
    }
    public void setTurnTracker(TurnTracker newTurnTracker) {turnTracker = newTurnTracker;}
    public void setChat(MessageList newChat) {chat = newChat;}
    public void setLog(MessageList newLog) {log = newLog;}

    // Observable override methods
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

    @Override
    public void notifyObservers(Object arg) {
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



