package shared.model.resourcebank;

/**
 * A DevCardList contains a list of all different types of development cards and how many of each one are in the list.
 */
public class DevCardList {

    private int soldierCardCount;
    private int monumentCardCount;
    private int yearOfPlentyCardCount;
    private int roadBuildingCardCount;
    private int monopolyCardCount;

    /**
     * Creates an empty DevCardList, to be used for the players hand.
     */
    public DevCardList(){}


    /**
     * Initializes a DevCardList with the specified values, to be used for the resource bank.
     * @param soldierCardCount
     * @param monumentCardCount
     * @param yearOfPlentyCardCount
     * @param roadBuildingCardCount
     * @param monopolyCardCount
     */
    public DevCardList(int soldierCardCount, int monumentCardCount, int yearOfPlentyCardCount, int roadBuildingCardCount, int monopolyCardCount){
        this.soldierCardCount = soldierCardCount;
        this.monumentCardCount = monumentCardCount;
        this.yearOfPlentyCardCount = yearOfPlentyCardCount;
        this.roadBuildingCardCount = roadBuildingCardCount;
        this.monopolyCardCount = monopolyCardCount;
    }

    /**
     * Update function to modify the model.
     * @param newDevCardList to replace the existing resource list.
     */
    public void update(DevCardList newDevCardList){

    }


    /**
     * Increments the soldierCardCount by one.
     */
    public void addSoldierCard(){

    }

    /**
     * Increments the monumentCardCount by one.
     */
    public void addMonumentCard(){

    }

    /**
     * Increments the yearOfPlentyCardCount by one.
     */
    public void addYearOfPlentyCard(){

    }

    /**
     * Increments the roadBuildingCardCount by one.
     */
    public void addRoadBuildingCard(){

    }

    /**
     * Increments the monopolyCardCount by one.
     */
    public void addMonopolyCard(){

    }


    /**
     * Remove Soldier Card from list.
     */
    public void removeSoldierCard(){

    }

    /**
     * Remove Monument Card from list.
     */
    public void removeMonumentCard(){

    }

    /**
     * Remove Year Of Plenty Card from list.
     */
    public void removeYearOfPlentyCard(){

    }

    /**
     * Remove Road Building Card from list.
     */
    public void removeRoadBuildingCard(){

    }

    /**
     * Remove Monopoly Card from list.
     */
    public void removeMonopolyCard(){

    }

    //GETTERS
    public int getSoldierCardCount() {
        return soldierCardCount;
    }

    public int getMonumentCardCount() {
        return monumentCardCount;
    }

    public int getYearOfPlentyCardCount() {
        return yearOfPlentyCardCount;
    }

    public int getRoadBuildingCardCount() {
        return roadBuildingCardCount;
    }

    public int getMonopolyCardCount() {
        return monopolyCardCount;
    }
}
