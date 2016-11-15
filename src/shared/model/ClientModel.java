package shared.model;

import client.ClientUser;
import client.data.RobPlayerInfo;
import client.utils.Converter;
import server.User;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;
import shared.model.map.VertexObject;
import shared.model.messagemanager.MessageLine;
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
 * <p>
 * Created by Mitchell on 9/15/2016.
 * <p>
 * from spec: "1. Use the Observer pattern to make your model observable. Each time the model is updated
 * from the server, it should notify its observers."
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
    public TradeOffer tradeOffer = null;


    /**
     * The ClientModel's ClientUpdateManager, which will take the update objects coming from the JSON translator
     * after the server has sent a newly updated model, and appropriately distribute the
     * updated data throughout the ClientModel.
     */
    private ClientUpdateManager updateManager;

    //Constructor
    public ClientModel(int gameNumber) {
        this.gameNumber = gameNumber;
        map = new Map(false, false, false);
        messageManager = new MessageManager();
        players = new Player[4];
        tradeOffer = null;
        turnTracker = new TurnTracker();
        resourceBank = new ResourceBank();
    }

    public ClientModel(int gameNumber, boolean randTiles, boolean randNumbers, boolean randPorts) {
        this.gameNumber = gameNumber;
        map = new Map(randTiles, randNumbers, randPorts);
        messageManager = new MessageManager();
        players = new Player[4];
        tradeOffer = null;
        turnTracker = new TurnTracker();
        resourceBank = new ResourceBank();
    }

    /**
     * Takes the incoming JSONObject (newModel) coming from the server and forwards it on to
     * ClientUpdateManager so the data can be distributed properly to the objects in ClientModel.
     *
     * @param newModel - the JSON object representing the newly updated ClientModel being sent from the server
     */
    public void updateClientModel(ClientModel newModel) {
        updateManager.delegateUpdates(newModel);
    }


    //CAN METHODS

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purchase a road.
     */
    public boolean canPurchaseRoad(int playerIndex) {
        return players[playerIndex].canPurchaseRoad();
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purchase a settlement.
     */
    public boolean canPurchaseSettlement(int playerIndex) {
        return players[playerIndex].canPurchaseSettlement();
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purchase a city.
     */
    public boolean canPurchaseCity(int playerIndex) {
        return players[playerIndex].canPurchaseCity();
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can purcahse a development card.
     */
    public boolean canPurchaseDevCard(int playerIndex) {
        return (players[playerIndex].canPurchaseDevelopmentCard() && resourceBank.hasDevCards());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can play a soldier card.
     */
    public boolean canPlaySolider(int playerIndex) {
        return (players[playerIndex].canPlaySoldierCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can play a monument card.
     */
    public boolean canPlayMonument(int playerIndex) {
        return (players[playerIndex].canPlayMonumentCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the player can play a road building card.
     */
    public boolean canPlayRoadBuilding(int playerIndex) {
        return (players[playerIndex].canPlayRoadBuildingCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the palyer can play a monopoly card.
     */
    public boolean canPlayMonopoly(int playerIndex) {
        return (players[playerIndex].canPlayMonopolyCard());
    }

    /**
     * @param playerIndex of player performing the action.
     * @return true if the palyer can play a year of plenty card.
     */
    public boolean canPlayYearOfPlenty(int playerIndex) {
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
        if (playerIndex != receiverIndex && receiverIndex >= 0 && receiverIndex < 4) {
            if (turnTracker.getCurrentTurn() == playerIndex) {
                return players[playerIndex].canOfferTrade(offer);
            }
        }
        return false;
    }

    public boolean canAcceptTrade(int index) {
        //valid index
        if (index >= 0 && index < 4) {
            return players[index].canAcceptTrade(tradeOffer.getTradeOfferList());
        }
        return false;
    }

    /**
     * @param playerIndex of player performing trade
     * @return true if player owns ports to trade from and enough cards to trade
     * IN THE FUTURE: This should return a set of PortTypes that player is able to trade
     */
    public boolean canMaritimeTrade(int playerIndex, int ratio, ResourceType inputResource) {
        Set<PortType> ports = map.getPlayersPorts(playerIndex);
        players[playerIndex].getMaritimeTradeManager().setPorts(ports);
        HashMap<PortType, boolean[]> enoughCards = players[playerIndex].canMaritimeTrade(ports);

        boolean[] values = enoughCards.get(Converter.resourceTypeToPortType(inputResource));
        //todo - check that they don't trade more than needed?
        return values[ratio - 2];
    }

    /**
     * @param playerIndex  of player performing the action.
     * @param edgeLocation to place the road.
     * @return true if the player can place a road at the specified edgeLocation.
     */
    public boolean canPlaceRoad(int playerIndex, EdgeLocation edgeLocation) {
        return map.buildRoadManager.canPlace(playerIndex, edgeLocation);
    }

    /**
     * @param edgeLocation to place the road.
     * @return true if the specified edge is not taken.
     */
    public boolean canPlaceSetUpRoad(EdgeLocation edgeLocation, VertexLocation firstVertexLocation) {
        return map.buildRoadManager.canPlaceSetUpRound(edgeLocation, firstVertexLocation);
    }

    /**
     * @param playerIndex    of player performing the action.
     * @param vertexLocation to place the settlement.
     * @return true if the player can place a settlement at the specified vertexLocation
     */
    public boolean canPlaceSettlement(int playerIndex, VertexLocation vertexLocation) {
        return map.buildSettlementManager.canPlace(playerIndex, vertexLocation);
    }

    public boolean canPlaceSetUpSettlement(int playerIndex, VertexLocation vertexLocation) {
        return map.buildSettlementManager.canPlaceSetUp(playerIndex, vertexLocation);
    }

    /**
     * @param playerIndex    of player performing the action.
     * @param vertexLocation to place the city.
     * @return true if the player can place a city at the specified vertexLocation
     */
    public boolean canPlaceCity(int playerIndex, VertexLocation vertexLocation) {
        return map.buildCityManager.canPlaceCity(playerIndex, vertexLocation);

    }

    /**
     * @param desiredHexLoc to place the robber.
     * @return true if its a new location that's not water.
     */
    public boolean canPlaceRobber(HexLocation desiredHexLoc) {
        return map.canPlaceRobber(desiredHexLoc);
    }


    //DO METHODS

    /**
     * Player building a new road.
     *
     * @param edgeLocation EdgeValue, which contains the player ID and location of the road.
     */
    public boolean buildRoad(EdgeLocation edgeLocation, int index, boolean free) {
        if (turnTracker.getStatus().equals("FirstRound") || turnTracker.getStatus().equals("SecondRound")) {
            if (map.getAllValidEdgeLocations().containsKey(edgeLocation) && checkIfValidIndex(index)) {
                buildRoadFunctionality(edgeLocation, index, free);
                return true;
            }
        } else if (canPlaceRoad(index, edgeLocation)) {
            buildRoadFunctionality(edgeLocation, index, free);
            return true;
        }
        return false;
    }

    private void buildRoadFunctionality(EdgeLocation edgeLocation, int index, boolean free) {
        map.buildRoadManager.placeRoad(index, edgeLocation);
        players[index].purchaseRoad(free);
        if (!free) {
            resourceBank.receiveRoadResources();
        }
        recalculateLongestRoad(index);
    }


    /*public boolean buildSetUpRoad(EdgeLocation desiredEdgeLocation, VertexLocation adjacentSettlementLocation){
        if(canPlaceSetUpRoad(desiredEdgeLocation, adjacentSettlementLocation)){
            map.buildRoadManager.placeRoad(in
            return true;
        }
        return false;
    }*/

    /**
     * Called each time a road is built, and recalculates who now has the longest road.
     * The model is adjusted accordingly.
     */
    private void recalculateLongestRoad(int index) {
        if (players[index].getAvailableRoadCount() <= 10) {
            int playerWithLongestRoadIndex = turnTracker.getLongestRoadHolder();
            if (playerWithLongestRoadIndex == -1) {
                turnTracker.setLongestRoadHolder(index);
                return;
            }
            if (playerWithLongestRoadIndex != index) {
                //If the player who built the road now has less available roads, then they have the most used road pieces.
                if (players[index].getAvailableRoadCount() < players[playerWithLongestRoadIndex].getAvailableRoadCount()) {
                    players[turnTracker.getLongestRoadHolder()].adjustVictoryPoints(-2);
                    turnTracker.setLongestRoadHolder(index);
                    players[index].adjustVictoryPoints(2);
                    calculateIfWinner(index);
                }
            }
        }
    }

    /**
     * Player building a settlement.
     *
     * @param newSettlement VertexObject, which contains the player ID and location of the settlement.
     */
    public boolean buildSettlement(VertexObject newSettlement, boolean free) {
        // if (canPlaceSettlement(newSettlement.getOwner(), newSettlement.getVertexLocation())) {
        map.buildSettlementManager.placeSettlement(newSettlement);
        players[newSettlement.getOwner()].purchaseSettlement(free);
        if (turnTracker.getStatus().equals("SecondRoundState")) {
            ResourceList secondSettlementResources = map.calculateSecondSettlementResources(newSettlement.getVertexLocation());
            players[newSettlement.getOwner()].receiveCardsFromDiceRoll(secondSettlementResources);
            //decrement resourceBank
            resourceBank.removeResources(secondSettlementResources);
        }else if(!free){
            resourceBank.receiveSettlementResources();
        }

        calculateIfWinner(newSettlement.getOwner());
        return true;
        // }
        // return false;

    }

    /**
     * Player building a city.
     *
     * @param newCity VertexObject, which contains the player ID and location of the City.
     */
    public boolean buildCity(VertexObject newCity) {
        if (canPlaceCity(newCity.getOwner(), newCity.getVertexLocation())) {
            map.buildCityManager.placeCity(newCity);
            players[newCity.getOwner()].purchaseCity();
            resourceBank.receiveCityResources();
            calculateIfWinner(newCity.getOwner());
            return true;
        }
        return false;
    }

    /**
     * Purchase a dev card from the bank.
     *
     * @param playerIndex of player purchasing the card.
     */
    public boolean purchaseDevCard(int playerIndex) {
        if (canPurchaseDevCard(playerIndex)) {
            DevCardType purcahsedDevCard = resourceBank.removeRandomDevCard(); //Remove from bank
            players[playerIndex].purchaseDevelopmentCard(purcahsedDevCard); //Send to player
            resourceBank.receiveDevCardResources();
            return true;
        }
        return false;
    }

    /**
     * Soldier card functionality.
     *
     * @param index
     */
    public boolean playSoldierCard(int index, HexLocation robberLocation, int victimIndex) {
        if (canPlaySolider(index)) {
            players[index].playSoldierCard();
            placeRobber(index, robberLocation, victimIndex);
            recalculateLargestArmy(index);
            return true;
        }
        return false;
    }

    /**
     * Places the robber at the desired Hex Location.
     *
     * @param robberLocation to place the robber.
     */
    public boolean placeRobber(int index, HexLocation robberLocation, int victimIndex) {
        if (canPlaceRobber(robberLocation)) {
            map.placeRobber(robberLocation);
            ResourceType stolenResource = players[victimIndex].getPlayerResourceList().removeRandomCard();
            players[index].getPlayerResourceList().addCardByType(stolenResource);
            turnTracker.setStatus("Playing");
            return true;
        }
        return false;
    }

    /**
     * Calculates which players are adjacent to a hex, how many cards they have, and other important
     * information for RobPlayerInfo.
     *
     * @param hexLoc that is getting robbed.
     * @return An array of RobPLayerInfo.
     */
    public RobPlayerInfo[] calculateRobPlayerInfo(HexLocation hexLoc) {
        ArrayList<Integer> adjacentPlayers = map.getPlayersAdjacentToHex(hexLoc);

        //Remove the current player from the robbing list.
        if (adjacentPlayers.contains(getClientPlayer().getPlayerIndex())) {
            Integer currentPlayerIndex = getClientPlayer().getPlayerIndex();
            adjacentPlayers.remove(currentPlayerIndex);

        }

        //Remove players that have 0 cards
        ArrayList<Integer> adjacentPlayersWithCards = new ArrayList<>();
        for (int i = 0; i < adjacentPlayers.size(); i++) {
            if (players[adjacentPlayers.get(i)].getPlayerResourceList().getCardCount() > 0) {
                adjacentPlayersWithCards.add(adjacentPlayers.get(i));
            }
        }

        //Create the Array of RobberPlayerInfo
        RobPlayerInfo[] victims = new RobPlayerInfo[adjacentPlayersWithCards.size()];
        for (int i = 0; i < adjacentPlayersWithCards.size(); i++) {
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
    private void recalculateLargestArmy(int index) {
        if (players[index].getSoldiersPlayed() >= 3) {
            int playerWithLargestArmyIndex = turnTracker.getLargestArmyHolder();
            if (playerWithLargestArmyIndex == -1) {
                turnTracker.setLargestArmyHolder(index);
                return;
            }
            if (playerWithLargestArmyIndex != index) {
                //If the player who built the road now has less available roads, then they have the most used road pieces.
                if (players[index].getAvailableRoadCount() < players[playerWithLargestArmyIndex].getAvailableRoadCount()) {
                    players[turnTracker.getLargestArmyHolder()].adjustVictoryPoints(-2);
                    turnTracker.setLongestRoadHolder(index);
                    players[index].adjustVictoryPoints(2);
                    calculateIfWinner(index);
                }
            }
        }
    }

    /**
     * Play a monument card.
     *
     * @param playerIndex
     */
    public boolean playMonumentCard(int playerIndex) {
        if (canPlayMonument(playerIndex)) {
            players[playerIndex].playMonumentCard();
            calculateIfWinner(playerIndex);
            return true;
        }
        return false;
    }

    /**
     * Play a road building card.
     *
     * @param playerIndex
     */
    public boolean playRoadBuildingCard(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2) {
        if (canPlayRoadBuilding(playerIndex)) {
            int roadsUsed = 0;
            if (edgeLocation1 != null) {
                buildRoad(edgeLocation1, playerIndex, true);
                roadsUsed++;
            }
            if (edgeLocation2 != null) {
                buildRoad(edgeLocation2, playerIndex, true);
                roadsUsed++;
            }
            players[playerIndex].playRoadBuildingCard(roadsUsed);
            recalculateLongestRoad(playerIndex);
            return true;
        }
        return false;
    }

    /**
     * Play a year of plenty card.
     *
     * @param playerIndex
     * @param resource1
     * @param resource2
     */
    public boolean playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2) {
        //TODO: Need a method or way that can make sure the bank has those resource cards. Need a case if bank only has 0-1 cards.
        if (canPlayYearOfPlenty(playerIndex)) {
            resourceBank.playYearOfPlenty(resource1, resource2);
            players[playerIndex].playYearOfPlentyCard(resource1, resource2);
            return true;
        }
        return false;
    }

    /**
     * Play Monopoly card.
     *
     * @param receiverPlayerIndex
     * @param monopolizedResource
     */
    public boolean playMonopolyCard(int receiverPlayerIndex, ResourceType monopolizedResource) {
        if (canPlayMonopoly(receiverPlayerIndex)) {
            int totalCardsGained = 0;
            //Take all cards of specified resource from each opposing player
            for (int index = 0; index < players.length; index++) {
                if (index != receiverPlayerIndex) {
                    totalCardsGained += players[index].loseAllCardsOfType(monopolizedResource);
                }
            }
            //Give those cards to the player who used the monopoly card.
            players[receiverPlayerIndex].playMonopolyCard(monopolizedResource, totalCardsGained);
            return true;
        }
        return false;
    }

    /**
     * Rulebook: If there are not enough of a resource type, then no one receive any of that resource
     * (unless it only affects one player, then that player gets the remaining resources from the bank)
     */
    public boolean receiveResourcesFromDiceRoll(int diceRoll) {
        if (diceRoll >= 2 && diceRoll <= 12) {
            ResourceList[] results = map.getDiceRollResults(diceRoll);
            for (int i = 0; i < players.length; i++) {
                players[i].receiveCardsFromDiceRoll(results[i]);
            }
            turnTracker.setStatus("Playing");
            return true;
        }
        return false;
    }

    public boolean roll7() {
        //determine if anyone needs to discard
        boolean needToDiscard = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getPlayerResourceList().getCardCount() > 7) {
                //mark player as having not discarded
                players[i].setDiscarded(false);
                needToDiscard = true;
            }
        }
        turnTracker.roll7(needToDiscard);
        return true;
    }

    /**
     * Send a chat message.
     *
     * @param index   of the player sending the message.
     * @param message the player wants to display.
     */
    public boolean sendChat(int index, String message) {
        //todo
        //no longer than 100 char
        if (message.length() < 100) {
            //no sql statements
            //regex check?

            //add chat to list
            messageManager.getChat().insertMessageLine(new MessageLine(message, players[index].getName()));
            return true;
        }
        return false;
    }

    /**
     * Discarding cards from rolling a 7
     *
     * @param index     of the player discarding.
     * @param discarded cards the player has selected to discard.
     */
    public boolean discardCards(int index, ResourceList discarded) {
        //index must be valid
        if (index >= 0 && index < 4) {
            //discarded values must be positive
            if (discarded.getWoodCardCount() >= 0 && discarded.getBrickCardCount() >= 0
                    && discarded.getWheatCardCount() >= 0 && discarded.getOreCardCount() >= 0
                    && discarded.getSheepCardCount() >= 0) {

                Player player = players[index];
                //if they haven't discarded/need to discard still
                if (!player.hasDiscarded()) {
                    //if right number discarded
                    ResourceList resources = player.getPlayerResourceList();
                    int numToDiscard = resources.getCardCount() / 2;
                    if (discarded.getCardCount() == numToDiscard) {
                        //if you have enough resources
                        if (resources.getWoodCardCount() >= discarded.getWoodCardCount()
                                && resources.getBrickCardCount() >= discarded.getBrickCardCount()
                                && resources.getWheatCardCount() >= discarded.getWheatCardCount()
                                && resources.getOreCardCount() >= discarded.getOreCardCount()
                                && resources.getSheepCardCount() >= discarded.getSheepCardCount()
                                ) {
                            //decrement player resources and set discarded to true
                            resources.decWoodCardCount(discarded.getWoodCardCount());
                            resources.decBrickCardCount(discarded.getBrickCardCount());
                            resources.decWheatCardCount(discarded.getWheatCardCount());
                            resources.decOreCardCount(discarded.getOreCardCount());
                            resources.decSheepCardCount(discarded.getSheepCardCount());
                            player.setDiscarded(true);

                            //if no one else needs to discard fix turn tracker
                            boolean discardDone = true;
                            for (int i = 0; i < players.length; i++) {
                                if (!players[i].hasDiscarded()) {
                                    discardDone = false;
                                    break;
                                }
                            }
                            if (discardDone) {
                                turnTracker.setStatus("Robbing");
                            }

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Player offering a trade.
     *
     * @param index         of the player offering the trade.
     * @param off           resource list offer.
     * @param receiverIndex index of the player receiving the offer
     */
    public boolean offerTrade(int index, ResourceList off, int receiverIndex) {
        if (canOfferTrade(index, off, receiverIndex)) {
            tradeOffer = new TradeOffer(index, receiverIndex, off);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Player choosing whether or not to accept a trade.
     *
     * @param index  of the player choosing.
     * @param accept returns true if they accept.
     */
    public boolean acceptTrade(int index, boolean accept) {
        //must be the receiver to accept or reject
        if (index == tradeOffer.getReceiverIndex()) {
            if (!accept) {
                tradeOffer = null;
                return true;
            } else if (canAcceptTrade(index)) {
                //switch resources
                //sender
                players[tradeOffer.getSenderIndex()].trade(tradeOffer.getTradeOfferList(), true);
                //receiver
                players[index].trade(tradeOffer.getTradeOfferList(), false);
                return true;
            }
        }
        return false;
    }

    /**
     * Maratime Trade Request
     *
     * @param index          of the player trading.
     * @param ratio          of the trade.
     * @param inputResource  to trade.
     * @param outputResource to receive.
     */
    public boolean maritimeTrade(int index, int ratio, ResourceType inputResource, ResourceType outputResource) {
        //it is their turn, index is valid, ratio correct based on their port, they have enough input, bank has output
        if (turnTracker.getCurrentTurn() == index && index >= 0 && index < 4) {
            if (inputResource != outputResource) {//can't trade brick for brick etc.
                if (players[index].getPlayerResourceList().hasResource(inputResource, ratio)
                        && resourceBank.getResourceList().hasResource(outputResource, 1)) {
                    if (ratio >= 2 && ratio <= 4) {//valid ratio
                        if (canMaritimeTrade(index, ratio, inputResource)) {
                            //trade with bank
                            for (int i = 0; i < ratio; i++) {
                                players[index].getPlayerResourceList().removeCardByType(inputResource);
                                resourceBank.getResourceList().addCardByType(inputResource);
                            }
                            players[index].getPlayerResourceList().addCardByType(outputResource);
                            resourceBank.getResourceList().removeCardByType(outputResource);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finishes the players turn, and changes to the next turn.
     *
     * @param index of the player ending their turn.
     */
    public boolean finishTurn(int index) {
        //index must be valid
        if (index >= 0 && index < 4) {
            //must be their turn
            if (turnTracker.getCurrentTurn() == index) {
                //must be in playing state/or setup
                if (turnTracker.getStatus().equals("Playing") ||
                        turnTracker.getStatus().equals("FirstRound") ||
                        turnTracker.getStatus().equals("SecondRound")) {

                    turnTracker.finishTurn();
                    return true;
                }
            }
        }
        return false;
    }

    private void calculateIfWinner(int playerIndex) {
        if (players[playerIndex].getVictoryPoints() >= 10) {
            setWinner(players[playerIndex].getPlayerID());
        }
    }

    private boolean checkIfValidIndex(int index) {
        if (index >= 0 && index < 4) {
            return true;
        }
        return false;
    }

    /**
     * Sets a player in the game.
     *
     * @param color of the player.
     * @param user  player, needed to determine the players name and ID.
     * @return true if the player succesfully joined, otherwise false.
     */
    public boolean joinGame(CatanColor color, User user) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(color, user.getUserName(), user.getUserID(), i);
                version++;
                return true;
            }
        }
        return false; //If 4 players have already joined.
    }


    //GETTERS
    public boolean getChanged() {
        return changed;
    }

    public int getVersion() {
        return version;
    }

    public int getWinner() {
        return winner;
    }

    public ResourceBank getResourceBank() {
        return resourceBank;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public TurnTracker getTurnTracker() {
        return turnTracker;
    }

    public MessageList getChat() {
        return chat;
    }

    public MessageList getLog() {
        return log;
    }

    public Map getMap() {
        return map;
    }

    public TradeOffer getTradeOffer() {
        return tradeOffer;
    }

    public ClientUpdateManager getUpdateManager() {
        return updateManager;
    }

    public Player getClientPlayer() {
        return players[ClientUser.getInstance().getIndex()];
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public Player[] getPlayers() {
        return players;
    }

    //SETTERS
    public void setChanged(boolean set) {
        changed = set;
    }

    public void setVersion(int newModVer) {
        version = newModVer;
    }

    public void setWinner(int newGameWinner) {
        winner = newGameWinner;
    }

    public void setResourceBank(ResourceBank newResBank) {
        resourceBank = newResBank;
    }

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
    }

    public void setTradeOffer(TradeOffer tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTurnTracker(TurnTracker newTurnTracker) {
        turnTracker = newTurnTracker;
    }

    public void setChat(MessageList newChat) {
        chat = newChat;
    }

    public void setLog(MessageList newLog) {
        log = newLog;
    }

    public void incrementVersion() {
        version++;
    }

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


    //FOR TESTING ONLY
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientModel)) return false;

        ClientModel that = (ClientModel) o;

        //primitives
        if (version != that.version) return false;
        if (gameNumber != that.gameNumber) return false;
        if (winner != that.winner) return false;
        if (changed != that.changed) return false;

        //check ResourceBank
        if (resourceBank != null) {
            //check DevCardList: first check total numCards, then check the individual counts
            if (resourceBank.getDevCardList().getTotalCardCount() == that.resourceBank.getDevCardList().getTotalCardCount()) {
                if (resourceBank.getDevCardList().getMonopolyCardCount() != that.resourceBank.getDevCardList().getMonopolyCardCount())
                    return false;
                if (resourceBank.getDevCardList().getMonumentCardCount() != that.resourceBank.getDevCardList().getMonumentCardCount())
                    return false;
                if (resourceBank.getDevCardList().getRoadBuildingCardCount() != that.resourceBank.getDevCardList().getRoadBuildingCardCount())
                    return false;
                if (resourceBank.getDevCardList().getSoldierCardCount() != that.resourceBank.getDevCardList().getSoldierCardCount())
                    return false;
                if (resourceBank.getDevCardList().getYearOfPlentyCardCount() != that.resourceBank.getDevCardList().getYearOfPlentyCardCount())
                    return false;
            } else {
                return false;
            }

            //check ResourceList card counts
            if (resourceBank.getResourceList().getBrickCardCount() != that.resourceBank.getResourceList().getBrickCardCount())
                return false;
            if (resourceBank.getResourceList().getWoodCardCount() != that.resourceBank.getResourceList().getWoodCardCount())
                return false;
            if (resourceBank.getResourceList().getSheepCardCount() != that.resourceBank.getResourceList().getSheepCardCount())
                return false;
            if (resourceBank.getResourceList().getWheatCardCount() != that.resourceBank.getResourceList().getWheatCardCount())
                return false;
            if (resourceBank.getResourceList().getOreCardCount() != that.resourceBank.getResourceList().getOreCardCount())
                return false;
        }

        //check MessageManager
        if (!messageManager.getChat().equals(that.messageManager.getChat()))
            return false;
        if (!messageManager.getLog().equals(that.messageManager.getLog()))
            return false;
        //check chat and log too, idk why they are even in here
        if (chat.getLines().size() != that.getChat().getLines().size())
            return false;
        if (log.getLines().size() != that.getLog().getLines().size())
            return false;

        //check TurnTracker
        if (!turnTracker.equals(that.getTurnTracker()))
            return false;

        //check Map
        if (!map.equals(that.getMap()))
            return false;

        //check players[]
        if (players.length != that.getPlayers().length)
            return false;

        for (int p = 0; p < players.length; p++) {
            if (!players[p].equals(that.getPlayers()[p]))
                return false;
        }

        //check tradeOffer
        if (tradeOffer != null ? !tradeOffer.equals(that.tradeOffer) : that.tradeOffer != null) return false;

        return true;
    }

}



