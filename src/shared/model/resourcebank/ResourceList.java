package shared.model.resourcebank;

import com.google.gson.annotations.SerializedName;
import shared.definitions.ResourceType;

import java.util.Random;

/**
 * A ResourceCardList contains a list of all different types of resource cards and how many of each one are in the list.
 */
public class ResourceList {

    @SerializedName("wood")
    private int woodCardCount;

    @SerializedName("brick")
    private int brickCardCount;

    @SerializedName("sheep")
    private int sheepCardCount;

    @SerializedName("wheat")
    private int wheatCardCount;

    @SerializedName("ore")
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
     * @param newResourceList to replace the existing resource list.
     */
    public void update(ResourceList newResourceList) {
        //this = newResourceList;
    }

    /**
     * Checks if the list has a certain amount of a specifed resource.
     * @param amount
     * @param resource
     * @return
     */
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

    /**
     * Lose all cards of a specific resrouce, called when a Monopoly is played.
     * @param resource that will be lost.
     * @return how many of that resource was lost.
     */
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

    /**
     * Removes a card from the resource list.
     * @param resource to be removed from the list.
     */
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

    /**
     * Adds a specific card to the resource list.
     * @param resource to add to the list.
     */
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
     * @param amount to increment woodCardCount by.
     */
    public void incWoodCardCount(int amount) {
        woodCardCount += amount;
    }

    /**
     * Increments the brickCardCount in the resource list.
     * @param amount to increment brickCardCount by.
     */
    public void incBrickCardCount(int amount) {
        brickCardCount += amount;
    }

    /**
     * Increments the sheepCardCount in the resource list.
     * @param amount to increment sheepCardCount by.
     */
    public void incSheepCardCount(int amount) {
        sheepCardCount += amount;
    }

    /**
     * Increments the wheatCardCount in the resource list.
     * @param amount to increment wheatCardCount by.
     */
    public void incWheatCardCount(int amount) {
        wheatCardCount += amount;
    }

    /**
     * Increments the oreCardCount in the resource list.
     * @param amount to increment oreCardCount by.
     */
    public void incOreCardCount(int amount) {
        oreCardCount += amount;
    }

    /**
     * Decrements the woodCardCount in the resource list.
     * @param amount to decrement woodCardCount by.
     */
    public void decWoodCardCount(int amount) {woodCardCount -= amount;}

    /**
     * Decrements the brickCardCount in the resource list.
     * @param amount to decrement brickCardCount by.
     */
    public void decBrickCardCount(int amount) {
        brickCardCount -= amount;
    }

    /**
     * Decrements the sheepCardCount in the resource list.
     * @param amount to decrement sheepCardCount by.
     */
    public void decSheepCardCount(int amount) {
        sheepCardCount -= amount;
    }

    /**
     * Decrements the wheatCardCount in the resource list.
     * @param amount to decrement wheatCardCount by.
     */
    public void decWheatCardCount(int amount) {
        wheatCardCount -= amount;
    }

    /**
     * Decrements the oreCardCount in the resource list.
     * @param amount to decrement oreCardCount by.
     */
    public void decOreCardCount(int amount) {
        oreCardCount -= amount;
    }


    /**
     * Remove a random card from the list. To be used when the player is getting robbed.
     * @return the ResourceType card removed from the list.
     */
    public ResourceType removeRandomCard(){
        Random rand = new Random();
        int randomIndex = rand.nextInt(getCardCount()); //Get a random number between 1 and the total # of dev cards.

        if (woodCardCount - randomIndex > 0){
            woodCardCount--;
            return ResourceType.WOOD;
        } else if (woodCardCount + brickCardCount - randomIndex > 0) {
            brickCardCount--;
            return ResourceType.BRICK;
        } else if (woodCardCount + brickCardCount + sheepCardCount - randomIndex > 0){
            sheepCardCount--;
            return ResourceType.SHEEP;
        } else if (woodCardCount + brickCardCount + sheepCardCount + wheatCardCount - randomIndex > 0){
            wheatCardCount--;
            return ResourceType.WHEAT;
        } else {
            oreCardCount--;
            return ResourceType.ORE;
        }
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

    public int getCardCount() { return (woodCardCount+ brickCardCount + sheepCardCount + wheatCardCount + oreCardCount);}

    public void setWoodCardCount(int woodCardCount) {
        this.woodCardCount = woodCardCount;
    }

    public void setBrickCardCount(int brickCardCount) {
        this.brickCardCount = brickCardCount;
    }

    public void setSheepCardCount(int sheepCardCount) {
        this.sheepCardCount = sheepCardCount;
    }

    public void setWheatCardCount(int wheatCardCount) {
        this.wheatCardCount = wheatCardCount;
    }

    public void setOreCardCount(int oreCardCount) {
        this.oreCardCount = oreCardCount;
    }

    @Override
    public String toString() {
        return "ResourceList{" +
                "woodCt=" + woodCardCount +
                ", brickCt=" + brickCardCount +
                ", sheepCt=" + sheepCardCount +
                ", wheatCt=" + wheatCardCount +
                ", oreCt=" + oreCardCount +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceList)) return false;

        ResourceList that = (ResourceList) o;

        if (woodCardCount != that.woodCardCount) return false;
        if (brickCardCount != that.brickCardCount) return false;
        if (sheepCardCount != that.sheepCardCount) return false;
        if (wheatCardCount != that.wheatCardCount) return false;
        return oreCardCount == that.oreCardCount;

    }

}
