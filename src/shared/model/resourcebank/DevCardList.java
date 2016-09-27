package shared.model.resourcebank;

import shared.definitions.DevCardType;

import java.util.ArrayList;
import java.util.Random;

import java.util.Random;

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

    public boolean isEmpty(){
        if(soldierCardCount > 1
                && monopolyCardCount > 1
                && monumentCardCount > 1
                && roadBuildingCardCount > 1
                && yearOfPlentyCardCount > 1){
            return true;
        }
        return false;
    }

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


    public DevCardType removeRandomCard(){
        Random rand = new Random();
        int randomIndex = rand.nextInt(getTotalCardCount()) + 1; //Get a random number between 1 and the total

        //ArrayList<DevCardType> devCards = new ArrayList<DevCardType>();

        if (soldierCardCount - randomIndex > 0){
            soldierCardCount--;
            return DevCardType.SOLDIER;
        } else if (soldierCardCount + monumentCardCount - randomIndex > 0) {
            monumentCardCount--;
            return DevCardType.MONUMENT;
        } else if (soldierCardCount + monumentCardCount + yearOfPlentyCardCount - randomIndex > 0){
            monumentCardCount--;
            return DevCardType.MONUMENT;
        } else if (soldierCardCount + monumentCardCount + yearOfPlentyCardCount + roadBuildingCardCount - randomIndex > 0){
            roadBuildingCardCount--;
            return DevCardType.ROAD_BUILD;
        } else {
            monopolyCardCount--;
            return DevCardType.MONOPOLY;
        }
    }


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
}
