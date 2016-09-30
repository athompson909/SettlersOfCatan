package shared.model.resourcebank;

import com.google.gson.annotations.SerializedName;
import shared.definitions.DevCardType;

import java.util.ArrayList;
import java.util.Random;

import java.util.Random;

/**
 * A DevCardList contains a list of all different types of development cards and how many of each one are in the list.
 */
public class DevCardList {

    @SerializedName("soldier")
    private int soldierCardCount;

    @SerializedName("monument")
    private int monumentCardCount;

    @SerializedName("yearOfPlenty")
    private int yearOfPlentyCardCount;

    @SerializedName("roadBuilding")
    private int roadBuildingCardCount;

    @SerializedName("monopoly")
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
        //TODO: Update the newDevCardList
    }

    /**
     * Check if the devCardList has not dev cards.
     */
    public boolean hasDevCards(){
        if(getTotalCardCount() > 0){
            return true;
        }
        return false;
    }

    /**
     * Add a dev card to the list.
     * @param newDevCard The specified dev card to add.
     */
    public void addDevCard(DevCardType newDevCard){
        switch (newDevCard) {
            case SOLDIER:
                soldierCardCount++;
                break;
            case MONUMENT:
                monumentCardCount++;
                break;
            case MONOPOLY:
                monopolyCardCount++;
                break;
            case ROAD_BUILD:
                roadBuildingCardCount++;
                break;
            case YEAR_OF_PLENTY:
                yearOfPlentyCardCount++;
        }
    }

    /**
     * Remove a random dev card from the list. To be used when purchasing a new dev card.
     * @return the DevCard Type removed from the list.
     */
    public DevCardType removeRandomCard(){
        Random rand = new Random();
        int randomIndex = rand.nextInt(getTotalCardCount()); //Get a random number between 1 and the total

        if (soldierCardCount - randomIndex > 0){
            soldierCardCount--;
            return DevCardType.SOLDIER;
        } else if (soldierCardCount + monumentCardCount - randomIndex > 0) {
            monumentCardCount--;
            return DevCardType.MONUMENT;
        } else if (soldierCardCount + monumentCardCount + yearOfPlentyCardCount - randomIndex > 0){
            yearOfPlentyCardCount--;
            return DevCardType.YEAR_OF_PLENTY;
        } else if (soldierCardCount + monumentCardCount + yearOfPlentyCardCount + roadBuildingCardCount - randomIndex > 0){
            roadBuildingCardCount--;
            return DevCardType.ROAD_BUILD;
        } else {
            monopolyCardCount--;
            return DevCardType.MONOPOLY;
        }
    }

    /**
     * Remove a specific devcard from the list.
     * @param devCard to remove from the list.
     */
    public void removeDevCard(DevCardType devCard){
        switch (devCard) {
            case SOLDIER:
                soldierCardCount--;
                break;
            case MONUMENT:
                monumentCardCount--;
                break;
            case MONOPOLY:
                monopolyCardCount--;
                break;
            case ROAD_BUILD:
                roadBuildingCardCount--;
                break;
            case YEAR_OF_PLENTY:
                yearOfPlentyCardCount--;
        }
    }

    //GETTERS
    public int getTotalCardCount(){
         return (soldierCardCount + monumentCardCount + yearOfPlentyCardCount + roadBuildingCardCount + monopolyCardCount);
    }

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

    @Override
    public String toString() {
        return "DevCardList{" +
                "soldierCt=" + soldierCardCount +
                ", monumentCt=" + monumentCardCount +
                ", yearOfPlentyCt=" + yearOfPlentyCardCount +
                ", roadBuildingCt=" + roadBuildingCardCount +
                ", monopolyCt=" + monopolyCardCount +
                '}';
    }
}
