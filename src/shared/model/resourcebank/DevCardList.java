package shared.model.resourcebank;

import shared.definitions.DevCardType;
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


    //TODO: This function still needs to get implemented
    /*
    Pre-condition:
     */
    public DevCardType removeRandomCard(){
        /*
        int totalCards = soldierCardCount + monumentCardCount + yearOfPlentyCardCount + roadBuildingCardCount + monopolyCardCount;
        Random rand = new Random();
        int randomIndex = rand.nextInt(totalCards) + 1;
        if (soldierCardCount - randomIndex == 0){
            return DevCardType.SOLDIER;
        }
        */
        soldierCardCount--;
        return DevCardType.SOLDIER;


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
