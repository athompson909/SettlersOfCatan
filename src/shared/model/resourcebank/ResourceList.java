package shared.model.resourcebank;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

/**
 * A ResourceCardList contains a list of all different types of resource cards and how many of each one are in the list.
 */
public class ResourceList {

    private int woodCardCount;
    private int brickCardCount;
    private int sheepCardCount;
    private int wheatCardCount;
    private int oreCardCount;

    /**
     * Creates an empty ResourceList for the players hand.
     */
    public ResourceList() {
    }

    /**
     * Creates a resource list with the specified amounts, to be used for the bank.
     *
     * @param woodCardCount
     * @param brickCardCount
     * @param sheepCardCount
     * @param wheatCardCount
     * @param oreCardCount
     */
    public ResourceList(int woodCardCount, int brickCardCount, int sheepCardCount, int wheatCardCount, int oreCardCount) {
        this.woodCardCount = woodCardCount;
        this.brickCardCount = brickCardCount;
        this.sheepCardCount = sheepCardCount;
        this.wheatCardCount = wheatCardCount;
        this.oreCardCount = oreCardCount;
    }


    /**
     * Update function to modify the model.
     *
     * @param newResourceList to replace the existing resource list.
     */
    public void update(ResourceList newResourceList) {

    }

    public boolean listHasAmountOfType(int amount, ResourceType resource){
        switch (resource) {
            case WOOD:
                if(woodCardCount >= amount){return true;}
                break;
            case BRICK:
                if(brickCardCount >= amount){return true;}
                break;
            case SHEEP:
                if(sheepCardCount >= amount){return true;}
                break;
            case WHEAT:
                if(wheatCardCount >= amount){return true;}
                break;
            case ORE:
                if(oreCardCount >= amount){return true;}
                break;
        }
        return false;
    }

    public int loseAllCardsOfType(ResourceType resource) {
        int total = 0;
        switch (resource) {
            case WOOD:
                total = getWoodCardCount();
                decWoodCardCount(total);
                break;
            case BRICK:
                total = getBrickCardCount();
                decBrickCardCount(total);
                break;
            case SHEEP:
                total = getSheepCardCount();
                decSheepCardCount(total);
                break;
            case WHEAT:
                total = getWheatCardCount();
                decWheatCardCount(total);
                break;
            case ORE:
                total = getWoodCardCount();
                decWheatCardCount(total);
                break;
        }
        return total;
    }

    public void removeCardByType(ResourceType resource){
        switch (resource) {
            case WOOD:
                woodCardCount--;
                break;
            case BRICK:
                brickCardCount--;
                break;
            case SHEEP:
                sheepCardCount--;
                break;
            case WHEAT:
                wheatCardCount--;
                break;
            case ORE:
                oreCardCount--;
                break;
        }
    }

    public void addCardByType(ResourceType resource){
        switch (resource) {
            case WOOD:
                woodCardCount++;
                break;
            case BRICK:
                brickCardCount++;
                break;
            case SHEEP:
                sheepCardCount++;
                break;
            case WHEAT:
                wheatCardCount++;
                break;
            case ORE:
                oreCardCount++;
                break;
        }
    }

    //INCREMENT AND DECREMENT
    /**
     * Increments the woodCardCount in the resource list.
     *
     * @param amount to increment woodCardCount by.
     */
    public void incWoodCardCount(int amount) {
        woodCardCount += amount;
    }

    /**
     * Increments the brickCardCount in the resource list.
     *
     * @param amount to increment brickCardCount by.
     */
    public void incBrickCardCount(int amount) {
        brickCardCount += amount;
    }

    /**
     * Increments the sheepCardCount in the resource list.
     *
     * @param amount to increment sheepCardCount by.
     */
    public void incSheepCardCount(int amount) {
        sheepCardCount += amount;
    }

    /**
     * Increments the wheatCardCount in the resource list.
     *
     * @param amount to increment wheatCardCount by.
     */
    public void incWheatCardCount(int amount) {
        wheatCardCount += amount;
    }

    /**
     * Increments the oreCardCount in the resource list.
     *
     * @param amount to increment oreCardCount by.
     */
    public void incOreCardCount(int amount) {
        oreCardCount += amount;
    }

    /**
     * Decrements the woodCardCount in the resource list.
     *
     * @param amount to decrement woodCardCount by.
     * @return false if the resource list woodCardCount is less than the amount.
     */
    public boolean decWoodCardCount(int amount) {
        return false;
    }

    /**
     * Decrements the brickCardCount in the resource list.
     *
     * @param amount to decrement brickCardCount by.
     * @return false if the resource list brickCardCount is less than the amount.
     */
    public boolean decBrickCardCount(int amount) {
        return false;
    }

    /**
     * Decrements the sheepCardCount in the resource list.
     *
     * @param amount to decrement sheepCardCount by.
     * @return false if the resource list sheepCardCount is less than the amount.
     */
    public boolean decSheepCardCount(int amount) {
        return false;
    }

    /**
     * Decrements the wheatCardCount in the resource list.
     *
     * @param amount to decrement wheatCardCount by.
     * @return false if the resource list wheatCardCount is less than the amount.
     */
    public boolean decWheatCardCount(int amount) {
        return false;
    }

    /**
     * Decrements the oreCardCount in the resource list.
     *
     * @param amount to decrement oreCardCount by.
     * @return false if the resource list oreCardCount is less than the amount.
     */
    public boolean decOreCardCount(int amount) {
        return false;
    }


    //GETTERS
    public int getWoodCardCount() {
        return woodCardCount;
    }

    public int getBrickCardCount() {
        return brickCardCount;
    }

    public int getSheepCardCount() {
        return sheepCardCount;
    }

    public int getWheatCardCount() {
        return wheatCardCount;
    }

    public int getOreCardCount() {
        return oreCardCount;
    }

}
