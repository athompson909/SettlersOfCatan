package client.model.player;

import client.model.resourcebank.DevCardList;
import client.model.resourcebank.ResourceList;

/**
 * The Player class represents each player's status, including the player's identity, points, available pieces, and cards.
 */
public class Player {

    /**
     *  The color of this player.
     */
    private PlayerColor color;

    /**
     * The players name.
     */
    String name;

    /**
     * The unique playerID. This is used to pick the client player apart from the
     others. This is only used here and in your cookie.,
     */
    int playerID;

    /**
     * What place in the array is this player? 0-3. It determines their turn order.
     */
    int playerIndex;

    /**
     * How many the victory points the player has.
     */
    int victoryPoints = 0;

    /**
     * How many monuments this player has played.,
     */
    int monuments = 0;

    /**
     * How many soldiers the player has played.
     */
    int solidersPlayed = 0;

    /**
     * How many cities this player has left to play,
     */
    int cities = 4;

    /**
     * How many settlements this player has left to play,
     */
    int settlements = 5;

    /**
     * How many roads this player has left to play,
     */
    int roads = 15;

    /**
     * Cards in the players hand
     */
    ResourceList playerResourceList = new ResourceList();

    /**
     * The dev cards the player bought this turn
     */
    DevCardList newDevCardList = new DevCardList();

    /**
     * The dev cards the player had when the turn started
     */
    DevCardList oldDevCardList = new DevCardList();

    /**
     * Whether the player has played a dev card this turn.,
     */
    boolean playedDevCard = false;

    /**
     * Whether this player has discarded or not already this discard phase.
     */
    boolean discarded = false;


    /**
     * Constructor for creating a new player.
     * @param color
     * @param name
     * @param playerID
     * @param playerIndex
     */
    public Player(PlayerColor color, String name, int playerID, int playerIndex){
        this.color = color;
        this.name = name;
        this.playerID = playerID;
        this.playerIndex = playerIndex;

    }

    /**
     * Updates the player through the server.
     * @param player The new player object to use.
     */
    public void update(Player player){

    }


    //SERVER SIDE FUNCTIONS:

    /**
     * Purchases a new road, which uses 1 Brick and 1 Wood
     */
    public void purchaseRoad(){

    }

    /**
     * Purchases a new settlement, which uses 1 Wood, 1 Brick, 1 Sheep, and 1 Wheat
     */
    public void purchaseSettlement(){

    }

    /**
     * Purchases a new road, which uses 2 Wheat and 3 Ore
     */
    public void purchaseCity(){

    }

    /**
     * Purchases a new dev card, which uses 1 Sheep, 1 Wheat, and 1 Ore
     */
    public void purchaseDevelopmentCard(){

    }


    //CLIENT SIDE FUNCTIONS

    /**
     * @return true if the player has the required resources to purchase a road.
     */
    public boolean canPurchaseRoad() {
       return false;
    }

    /**
     * @return true if the player has the required resources to purchase a settlement.
     */
    public boolean canPurchaseSettlement() {
        return false;
    }

    /**
     * @return true if the player has the required resources to purchase a city.
     */
    public boolean canPurchaseCity() {
        return false;
    }

    /**
     * @return true if the player has the required resources to purchase a Development Card.
     */
    public boolean canPurchaseDevelopmentCard() {
        return false;
    }



    /**
     * @return true if the player can play development cards
     */
    public boolean canPlayDevelopmentCards() {
        return false;
    }

    /**
     * @return true if the player can play a solider card.
     */
    public boolean canPlaySoliderCard() {
        return false;
    }

    /**
     * @return true if the player can play a monument card.
     */
    public boolean canPlayMonumentCard() {
        return false;
    }

    /**
     * @return true if the player can play a YearOfPlenty card.
     */
    public boolean canPlayYearOfPlentyCard() {
        return false;
    }

    /**
     * @return true if the player can play a RoadBuilding card.
     */
    public boolean canPlayRoadBuildingCard() {
        return false;
    }

    /**
     * @return true if the player can play a Monopoly card.
     */
    public boolean canPlayMonopolyCard() {
        return false;
    }


    //GETTERS
    public PlayerColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getMonuments() {
        return monuments;
    }

    public int getSolidersPlayed() {
        return solidersPlayed;
    }

    public int getCities() {
        return cities;
    }

    public int getSettlements() {
        return settlements;
    }

    public int getRoads() {
        return roads;
    }

    public ResourceList getPlayerResourceList() {
        return playerResourceList;
    }

    public DevCardList getNewDevCardList() {
        return newDevCardList;
    }

    public DevCardList getOldDevCardList() {
        return oldDevCardList;
    }

    public boolean isPlayedDevCard() {
        return playedDevCard;
    }

    public boolean isDiscarded() {
        return discarded;
    }
}
