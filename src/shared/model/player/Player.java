package shared.model.player;

import com.google.gson.annotations.SerializedName;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceList;

import java.util.*;

import static shared.definitions.PortType.*;

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
    private int playerID;

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
    @SerializedName("soldiers")
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
    private int availableRoadCount = 15;

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

    /**
     * Maritime Trade manager so the player can trade with the bank using ports.
     */
    private MaritimeTradeManager maritimeTradeManager = new MaritimeTradeManager();


    /**
     * Constructor for creating a new player.
     *
     * @param color of the player.
     * @param name of the player.
     * @param playerID of the player.
     * @param playerIndex of the player.
     */
    public Player(CatanColor color, String name, int playerID, int playerIndex) {
        this.color = color;
        this.name = name;
        this.playerID = playerID;
        this.playerIndex = playerIndex;
    }

    /**
     * Update the player with a newPlayer.
     * @param newPlayer
     */
    public void updatePlayer(Player newPlayer) {
        setVictoryPoints(newPlayer.getVictoryPoints());
        setMonuments(newPlayer.getMonuments());
        setSoldiersPlayed(newPlayer.getSoldiersPlayed());
        setCityCount(newPlayer.getCityCount());
        setSettlementCount(newPlayer.getSettlementCount());
        setAvailableRoadCount(newPlayer.getAvailableRoadCount());
        setPlayerResourceList(newPlayer.getPlayerResourceList());
        setOldDevCardList(newPlayer.getOldDevCardList());
        setNewDevCardList(newPlayer.getNewDevCardList());
        setPlayedDevCard(newPlayer.hasPlayedDevCard());
        setDiscarded(newPlayer.hasDiscarded());
    }

    //CAN FUNCTIONS
    /**
     * @return true if the player has the required resources to purchase a road.
     */
    public boolean canPurchaseRoad() {
        return (playerResourceList.getBrickCardCount() >= 1
                && playerResourceList.getWoodCardCount() >= 1
                && availableRoadCount > 0);
    }

    /**
     * @return true if the player has the required resources to purchase a settlement.
     */
    public boolean canPurchaseSettlement() {
        return (playerResourceList.getBrickCardCount() >= 1
                && playerResourceList.getWoodCardCount() >= 1
                && playerResourceList.getSheepCardCount() >= 1
                && playerResourceList.getWheatCardCount() >= 1
                && settlementCount > 0);
    }

    /**
     * @return true if the player has the required resources to purchase a city.
     */
    public boolean canPurchaseCity() {
        return (playerResourceList.getWheatCardCount() >= 2
                && playerResourceList.getOreCardCount() >= 3
                && cityCount > 0);
    }

    /**
     * @return true if the player has the required resources to purchase a Development Card.
     */
    public boolean canPurchaseDevelopmentCard() {
        return (playerResourceList.getOreCardCount() >= 1
                && playerResourceList.getSheepCardCount() >= 1
                && playerResourceList.getWheatCardCount() >= 1);
    }

    /**
     * @return true if the player can play development cards
     */
    public boolean canPlayDevelopmentCards() {
        //If the player has already played a Dev card
        return playedDevCard;
    }

    /**
     * @return true if the player can play a solider card.
     */
    public boolean canPlaySoldierCard() {
        return (oldDevCardList.getSoldierCardCount() > 0);
    }

    /**
     * @return true if the player can play a monument card.
     */
    public boolean canPlayMonumentCard() {
        return (oldDevCardList.getMonumentCardCount() > 0);
    }

    /**
     * @return true if the player can play a YearOfPlenty card.
     */
    public boolean canPlayYearOfPlentyCard() {
        return (oldDevCardList.getYearOfPlentyCardCount() > 0);
    }

    /**
     * @return true if the player can play a RoadBuilding card.
     */
    public boolean canPlayRoadBuildingCard() {
        return (oldDevCardList.getRoadBuildingCardCount() > 0);
    }

    /**
     * @return true if the player can play a Monopoly card.
     */
    public boolean canPlayMonopolyCard() {
        return (oldDevCardList.getMonopolyCardCount() > 0);
    }

    //DO METHODS:
    /**
     * Purchases a new road, which uses 1 Brick and 1 Wood
     */
    public void purchaseRoad() {
        playerResourceList.decWoodCardCount(1);
        playerResourceList.decBrickCardCount(1);
        availableRoadCount--;
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

    /**
     * Play a solider card from the player's hand.
     */
    public void playSoldierCard() {
        oldDevCardList.removeDevCard(DevCardType.SOLDIER);
        soldiersPlayed++;
        playedDevCard = true;
    }

    /**
     * play a monument card from the player's hand.
     */
    public void playMonumentCard() {
        oldDevCardList.removeDevCard(DevCardType.MONUMENT);
        monuments++;
        victoryPoints++;
        //playedDevCard = true; //Monument
    }

    /**
     * Play a road building card from the player's hand, and lose the amount of roads.
     * @param roadsUsed when playing the card. Usually will be 2, unless the player has 0-1 road pieces.
     */
    public void playRoadBuildingCard(int roadsUsed) {
        availableRoadCount = availableRoadCount - roadsUsed;
        oldDevCardList.removeDevCard(DevCardType.ROAD_BUILD);
        playedDevCard = true;
    }

    /**
     * Play a Monopoly card from the player's hand.
     * @param monopolizedResource the player will receive.
     * @param cardsGained How many cards of the specified resource the player will gain.
     */
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

    /**
     * Called when a year of plenty card is played.
     * @param resource1 First resource the player desires from the bank.
     * @param resource2 Second resource the player desires from the bank.
     */
    public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
        oldDevCardList.removeDevCard(DevCardType.YEAR_OF_PLENTY);
        playerResourceList.addCardByType(resource1);
        playerResourceList.addCardByType(resource2);
        playedDevCard = true;
    }

    public HashMap<PortType, boolean[]> canMaritimeTrade(Set ports) {
        maritimeTradeManager = new MaritimeTradeManager();
        maritimeTradeManager.setPorts(ports);
        HashMap<PortType, boolean[]> enoughCards = new HashMap<>();

        //enoughCards<WOOD> -> CanTradeWood [2:1, 3:1, 4:1]
        //enoughCards<WHEAT> -> CanTradeWheat [2:1, 3:1, 4:1]
        //enoughCards<BRICK> -> CanTradeBrick [2:1, 3:1, 4:1]
        //enoughCards<ORE> -> CanTradeOre [2:1, 3:1, 4:1]
        //enoughCards<SHEEP> -> CanTradeSheep [2:1, 3:1, 4:1]

        boolean threePort = maritimeTradeManager.getHasThreePort();
        boolean woodPort = maritimeTradeManager.getHasWoodPort();
        boolean wheatPort = maritimeTradeManager.getHasWheatPort();
        boolean brickPort = maritimeTradeManager.getHasBrickPort();
        boolean orePort = maritimeTradeManager.getHasOrePort();
        boolean sheepPort = maritimeTradeManager.getHasSheepPort();

        boolean[] woodCards = new boolean[3];
        int woodCount = playerResourceList.getWoodCardCount();
        if(woodPort && (woodCount >= 2)) {woodCards[0] = true;}
        if(threePort && (woodCount >= 3)) {woodCards[1] = true;}
        if(woodCount >= 4) {woodCards[2] = true;}
        enoughCards.put(WOOD, woodCards);

        boolean[] wheatCards = new boolean[3];
        int wheatCount = playerResourceList.getWheatCardCount();
        if(wheatPort && (wheatCount >= 2)) {wheatCards[0] = true;}
        if(threePort && (wheatCount >= 3)) {wheatCards[1] = true;}
        if(wheatCount >= 4) {wheatCards[2] = true;}
        enoughCards.put(WHEAT, wheatCards);

        boolean[] brickCards = new boolean[3];
        int brickCount = playerResourceList.getBrickCardCount();
        if(brickPort && (brickCount >= 2)) {brickCards[0] = true;}
        if(threePort && (brickCount >= 3)) {brickCards[1] = true;}
        if(brickCount >= 4) {brickCards[2] = true;}
        enoughCards.put(BRICK, brickCards);

        boolean[] oreCards = new boolean[3];
        int oreCount = playerResourceList.getOreCardCount();
        if(orePort && (oreCount >= 2)) {oreCards[0] = true;}
        if(threePort && (oreCount >= 3)) {oreCards[1] = true;}
        if(oreCount >= 4) {oreCards[2] = true;}
        enoughCards.put(ORE, oreCards);

        boolean[] sheepCards = new boolean[3];
        int sheepCount = playerResourceList.getSheepCardCount();
        if(sheepPort && (sheepCount >= 2)) {sheepCards[0] = true;}
        if(threePort && (sheepCount >= 3)) {sheepCards[1] = true;}
        if(sheepCount >= 4) {sheepCards[2] = true;}
        enoughCards.put(SHEEP, sheepCards);

        return enoughCards;
    }

    public boolean canOfferTrade(ResourceList offer) {
        if(canTradeWood(offer.getWoodCardCount()) &&
                canTradeWheat(offer.getWoodCardCount()) &&
                canTradeBrick(offer.getBrickCardCount()) &&
                canTradeOre(offer.getOreCardCount()) &&
                canTradeSheep(offer.getSheepCardCount())){
            return true;
        }
        else {return false;}
    }

    public boolean canAcceptTrade(ResourceList offer) {
        if(canTradeWood(-offer.getWoodCardCount()) &&
                canTradeWheat(-offer.getWoodCardCount()) &&
                canTradeBrick(-offer.getBrickCardCount()) &&
                canTradeOre(-offer.getOreCardCount()) &&
                canTradeSheep(-offer.getSheepCardCount())){
            return true;
        }
        else {return false;}
    }

    public void trade(ResourceList trade, boolean sender){
        int addOrSub = 1;
        if(sender){addOrSub = -1;}
        playerResourceList.incWoodCardCount(trade.getWoodCardCount()*addOrSub);
        playerResourceList.incBrickCardCount(trade.getBrickCardCount()*addOrSub);
        playerResourceList.incWheatCardCount(trade.getWheatCardCount()*addOrSub);
        playerResourceList.incOreCardCount(trade.getOreCardCount()*addOrSub);
        playerResourceList.incSheepCardCount(trade.getSheepCardCount()*addOrSub);
    }

    private boolean canTradeWood(int toTrade) {
        return (playerResourceList.getWoodCardCount() >= toTrade);
    }
    private boolean canTradeWheat(int toTrade) {
        return (playerResourceList.getWheatCardCount()>= toTrade);
    }
    private boolean canTradeBrick(int toTrade) {
        return (playerResourceList.getBrickCardCount()>= toTrade);
    }
    private boolean canTradeOre(int toTrade) {
        return (playerResourceList.getOreCardCount()>= toTrade);
    }
    private boolean canTradeSheep(int toTrade) {
        return (playerResourceList.getSheepCardCount()>= toTrade);
    }


    public void receiveCardsFromDiceRoll(ResourceList resourceList){
        playerResourceList.incWoodCardCount(resourceList.getWoodCardCount());
        playerResourceList.incBrickCardCount(resourceList.getBrickCardCount());
        playerResourceList.incSheepCardCount(resourceList.getSheepCardCount());
        playerResourceList.incWheatCardCount(resourceList.getWheatCardCount());
        playerResourceList.incOreCardCount(resourceList.getOreCardCount());
    }

    public void gainTwoVictoryPoints(){
        victoryPoints = (victoryPoints + 2);
    }

    public void loseTwoVictoryPoints(){
        victoryPoints = (victoryPoints - 2);
    }



    //GETTERS
    public MaritimeTradeManager getMaritimeTradeManager() {return maritimeTradeManager;}

    public CatanColor getColor() {
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

    public int getSoldiersPlayed() {
        return soldiersPlayed;
    }

    public int getCityCount() {
        return cityCount;
    }

    public int getSettlementCount() {
        return settlementCount;
    }

    public int getAvailableRoadCount() {
        return availableRoadCount;
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

    //SETTERS

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

    private void setAvailableRoadCount(int numRoads) {
        availableRoadCount = numRoads;
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
