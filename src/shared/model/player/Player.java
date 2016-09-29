package shared.model.player;

import com.google.gson.annotations.SerializedName;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceList;

/**
 * The Player class represents each player's status, including the player's identity, points, available pieces, and cards.
 */
public class Player { //


    /**
     * The color of this player.
     */
    private CatanColor color;

    /**
     * The players name.
     */
    private String name;

    /**
     * The unique playerID. This is used to pick the client player apart from the
     * others. This is only used here and in your cookie.,
     */
    private String playerID;

    /**
     * What place in the array is this player? 0-3. It determines their turn order.
     */
    private int playerIndex;

    /**
     * How many the victory points the player has.
     */
    private int victoryPoints = 0;

    /**
     * How many monuments this player has played.,
     */
    private int monuments = 0;

    /**
     * How many soldiers the player has played.
     */
    private int soldiersPlayed = 0;

    /**
     * How many cities this player has left to play,
     */
    @SerializedName("cities")
    private int cityCount = 4;

    /**
     * How many settlements this player has left to play,
     */
    @SerializedName("settlements")
    private int settlementCount = 5;

    /**
     * How many roads this player has left to play,
     */
    @SerializedName("roads")
    private int roadCount = 15;

    /**
     * Cards in the players hand
     */
    @SerializedName("resources")
    private ResourceList playerResourceList = new ResourceList();

    /**
     * The dev cards the player bought this turn
     */
    @SerializedName("newDevCards")
    private DevCardList newDevCardList = new DevCardList();

    /**
     * The dev cards the player had when the turn started
     */
    @SerializedName("oldDevCards")
    private DevCardList oldDevCardList = new DevCardList();

    /**
     * Whether the player has played a dev card this turn.,
     */
    private boolean playedDevCard = false;

    /**
     * Whether this player has discarded or not already this discard phase.
     */
    private boolean discarded = false;

    private MaritimeTradeManager maritimeTradeManager = new MaritimeTradeManager();


    /**
     * Constructor for creating a new player.
     *
     * @param color
     * @param name
     * @param playerID
     * @param playerIndex
     */
    public Player(CatanColor color, String name, String playerID, int playerIndex) {
        this.color = color;
        this.name = name;
        this.playerID = playerID;
        this.playerIndex = playerIndex;
    }

    /**
     * Updates the player through the server.
     *
     * @param player The new player object to use.
     */
    public void update(Player player) {

    }


    //CAN FUNCTIONS
    /**
     * @return true if the player has the required resources to purchase a road.
     */
    public boolean canPurchaseRoad() {
        if (playerResourceList.getBrickCardCount() >= 1
                && playerResourceList.getWoodCardCount() >= 1
                && roadCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player has the required resources to purchase a settlement.
     */
    public boolean canPurchaseSettlement() {
        if (playerResourceList.getBrickCardCount() >= 1
                && playerResourceList.getWoodCardCount() >= 1
                && playerResourceList.getSheepCardCount() >= 1
                && playerResourceList.getWheatCardCount() >= 1
                && settlementCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player has the required resources to purchase a city.
     */
    public boolean canPurchaseCity() {
        if (playerResourceList.getWheatCardCount() >= 2
                && playerResourceList.getOreCardCount() >= 3
                && cityCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player has the required resources to purchase a Development Card.
     */
    public boolean canPurchaseDevelopmentCard() {
        if (playerResourceList.getOreCardCount() >= 1
                && playerResourceList.getSheepCardCount() >= 1
                && playerResourceList.getWheatCardCount() >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player can play development cards
     */
    public boolean canPlayDevelopmentCards() {
        //If the player has already played a Dev card
        if (playedDevCard) {
            return false;
        }
        return true;
    }

    /**
     * @return true if the player can play a solider card.
     */
    public boolean canPlaySoldierCard() {
        if (oldDevCardList.getSoldierCardCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player can play a monument card.
     */
    public boolean canPlayMonumentCard() {
        if (oldDevCardList.getMonumentCardCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player can play a YearOfPlenty card.
     */
    public boolean canPlayYearOfPlentyCard() {
        if (oldDevCardList.getYearOfPlentyCardCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player can play a RoadBuilding card.
     */
    public boolean canPlayRoadBuildingCard() {
        if (oldDevCardList.getRoadBuildingCardCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player can play a Monopoly card.
     */
    public boolean canPlayMonopolyCard() {
        if (oldDevCardList.getMonopolyCardCount() > 0) {
            return true;
        }
        return false;
    }

    public void updatePlayer(Player newPlayer) {
        setVictoryPoints(newPlayer.getVictoryPoints());
        setMonuments(newPlayer.getMonuments());
        setSoldiersPlayed(newPlayer.getSoldiersPlayed());
        setCityCount(newPlayer.getCityCount());
        setSettlementCount(newPlayer.getSettlementCount());
        setRoadCount(newPlayer.getRoadCount());
        setPlayerResourceList(newPlayer.getPlayerResourceList());
        setOldDevCardList(newPlayer.getOldDevCardList());
        setNewDevCardList(newPlayer.getNewDevCardList());
        setPlayedDevCard(newPlayer.hasPlayedDevCard());
        setDiscarded(newPlayer.hasDiscarded());
    }

    //DO METHODS:
    /**
     * Purchases a new road, which uses 1 Brick and 1 Wood
     */
    public void purchaseRoad() {
        playerResourceList.decWoodCardCount(1);
        playerResourceList.decBrickCardCount(1);
        roadCount--;
    }

    /**
     * Purchases a new settlement, which uses 1 Wood, 1 Brick, 1 Sheep, and 1 Wheat
     */
    public void purchaseSettlement() {
        playerResourceList.decBrickCardCount(1);
        playerResourceList.decWoodCardCount(1);
        playerResourceList.decSheepCardCount(1);
        playerResourceList.decWheatCardCount(1);
        settlementCount--;
        victoryPoints++;
    }

    /**
     * Purchases a new road, which uses 2 Wheat and 3 Ore
     */
    public void purchaseCity() {
        playerResourceList.decOreCardCount(3);
        playerResourceList.decWheatCardCount(2);
        cityCount--;
        settlementCount++; //Get your settlement piece back.
        victoryPoints++;

    }

    /**
     * Purchases a new dev card, which uses 1 Sheep, 1 Wheat, and 1 Ore
     */
    public void purchaseDevelopmentCard(DevCardType newDevCard) {
        playerResourceList.decOreCardCount(1);
        playerResourceList.decSheepCardCount(1);
        playerResourceList.decWheatCardCount(1);
        newDevCardList.addDevCard(newDevCard);
    }

    public void playSoldierCard() {
        oldDevCardList.removeDevCard(DevCardType.SOLDIER);
        soldiersPlayed++;
        playedDevCard = true;
    }

    public void playMonumentCard() {
        oldDevCardList.removeDevCard(DevCardType.MONUMENT);
        monuments++;
        victoryPoints++;
        //playedDevCard = true; //Monument
    }

    public void playRoadBuildingCard(int roadsUsed) {
        for(int i = 0; i < roadsUsed; i++){
            roadCount--;
        }
        oldDevCardList.removeDevCard(DevCardType.ROAD_BUILD);
        playedDevCard = true;
    }

    public void playMonopolyCard(ResourceType monopolizedResource, int cardsGained) {
        oldDevCardList.removeDevCard(DevCardType.MONOPOLY);
        switch (monopolizedResource) {
            case WOOD:
                playerResourceList.incWoodCardCount(cardsGained);
                break;
            case BRICK:
                playerResourceList.incBrickCardCount(cardsGained);
                break;
            case SHEEP:
                playerResourceList.incSheepCardCount(cardsGained);
                break;
            case WHEAT:
                playerResourceList.incWheatCardCount(cardsGained);
                break;
            case ORE:
                playerResourceList.incOreCardCount(cardsGained);
                break;
        }
        playedDevCard = true;
    }

    /**
     * Called when a monopoly is played.
     * @param resource the resource being monopolized.
     * @return the number of that resource in the player's hand.
     */
    public int loseAllCardsOfType(ResourceType resource) {
        return playerResourceList.loseAllCardsOfType(resource);
    }


    public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
        //TODO: Figure out where verification of these resources being available is done
        oldDevCardList.removeDevCard(DevCardType.YEAR_OF_PLENTY);
        playerResourceList.addCardByType(resource1);
        playerResourceList.addCardByType(resource2);
        playedDevCard = true;
    }




    //GETTERS
    public CatanColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getPlayerID() {
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

    public int getSoldiersPlayed() {
        return soldiersPlayed;
    }

    public int getCityCount() {
        return cityCount;
    }

    public int getSettlementCount() {
        return settlementCount;
    }

    public int getRoadCount() {
        return roadCount;
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

    public boolean hasPlayedDevCard() {
        return playedDevCard;
    }

    public boolean hasDiscarded() {
        return discarded;
    }

    //SETTERS - Possibly don't need these, leaving as private for now
    private void setVictoryPoints(int numVict) {
        victoryPoints = numVict;
    }

    private void setMonuments(int numMon) {
        monuments = numMon;
    }

    private void setSoldiersPlayed(int numSoldiers) {
        soldiersPlayed = numSoldiers;
    }

    private void setCityCount(int numCit) {
        cityCount = numCit;
    }

    private void setSettlementCount(int numSett) {
        settlementCount = numSett;
    }

    private void setRoadCount(int numRoads) {
        roadCount = numRoads;
    }

    private void setPlayerResourceList(ResourceList list) {
        playerResourceList = list;
    }

    private void setOldDevCardList(DevCardList list) {
        oldDevCardList = list;
    }

    private void setNewDevCardList(DevCardList list) {
        newDevCardList = list;
    }

    //Temporarily setting public for Junit test cases.
    public void setPlayedDevCard(boolean playedDev) {
        playedDevCard = playedDev;
    }

    private void setDiscarded(boolean disc) {
        discarded = disc;
    }


}
