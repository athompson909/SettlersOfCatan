package shared.model.resourcebank;

import com.google.gson.annotations.SerializedName;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

/**
 * The Resource Bank contains a DevCardList and ResourceCardList,
 * and keeps track of all the available cards not owned by any player.
 *
 * The JSON for the ResourceBank doesn't have any data on what Dev Cards are used/available...
 */
public class ResourceBank {

    //TESTING ONLY
    @SerializedName("deck")
    private DevCardList devCardList;
    @SerializedName("bank")
    private ResourceList resourceList;
    //

    private transient final int AVAILABLE_SOLDIER_CARDS = 15;
    private transient final int AVAILABLE_MONUMENT_CARDS = 5;
    private transient final int AVAILABLE_YEAROFPLENTY_CARDS = 2;
    private transient final int AVAILABLE_ROADBUILDING_CARDS = 2;
    private transient final int AVAILABLE_MONOPOLY_CARDS = 2;

    private transient final int AVAILABLE_WOOD_CARDS = 19;
    private transient final int AVAILABLE_BRICK_CARDS = 19;
    private transient final int AVAILABLE_SHEEP_CARDS = 19;
    private transient final int AVAILABLE_WHEAT_CARDS = 19;
    private transient final int AVAILABLE_ORE_CARDS = 19;

    /**
     * Creates a new ResourceBank for the game using the available card constants for the devCardList and ResourceList
     */
    public ResourceBank() {
        devCardList = new DevCardList(
                AVAILABLE_SOLDIER_CARDS, AVAILABLE_MONUMENT_CARDS, AVAILABLE_YEAROFPLENTY_CARDS, AVAILABLE_ROADBUILDING_CARDS, AVAILABLE_MONOPOLY_CARDS);
        resourceList = new ResourceList(
                AVAILABLE_WOOD_CARDS, AVAILABLE_BRICK_CARDS, AVAILABLE_SHEEP_CARDS, AVAILABLE_WHEAT_CARDS, AVAILABLE_ORE_CARDS);
    }

    /**
     * Updates the resource bank.
     * @param newResBank to replace the existing resource bank.
     */
    public void updateResourceBank(ResourceBank newResBank) {
        setDevCardList(newResBank.getDevCardList());
        setResourceList(newResBank.getResourceList());
    }

    /**
     * Check if the bank has Development cards available to purcahse.
     * @return true if the bank has dev cards.
     */
    public boolean hasDevCards() {
        return devCardList.hasDevCards();
    }

    /**
     * Remove a random dev Card from the bank when purchased.
     * @return the devcard the player will receive.
     */
    public DevCardType removeRandomDevCard() {
        return devCardList.removeRandomCard();
    }

    /**
     * Remove the cards from the bank the player will receive.
     * @param resource1
     * @param resource2
     */
    public void playYearOfPlenty(ResourceType resource1, ResourceType resource2) {
        resourceList.removeCardByType(resource1);
        resourceList.removeCardByType(resource2);
    }

    public void receiveRoadResources(){
        resourceList.incWoodCardCount(1);
        resourceList.incBrickCardCount(1);
    }

    public void receiveSettlementResources(){
        resourceList.incWoodCardCount(1);
        resourceList.incBrickCardCount(1);
        resourceList.incSheepCardCount(1);
        resourceList.incOreCardCount(1);
    }

    public void receiveDevCardResources(){
        resourceList.incSheepCardCount(1);
        resourceList.incWheatCardCount(1);
        resourceList.incOreCardCount(1);
    }

    public void receiveCityResources(){
        resourceList.incWheatCardCount(2);
        resourceList.incOreCardCount(3);
    }

    public void removeResources(ResourceList list){
        resourceList.decWoodCardCount(list.getWoodCardCount());
        resourceList.decBrickCardCount(list.getBrickCardCount());
        resourceList.decWheatCardCount(list.getWheatCardCount());
        resourceList.decOreCardCount(list.getOreCardCount());
        resourceList.decSheepCardCount(list.getSheepCardCount());
    }


    //GETTERS
    public DevCardList getDevCardList() {
        return devCardList;
    }

    public ResourceList getResourceList() {
        return resourceList;
    }

    //SETTERS
    public void setDevCardList(DevCardList newDevCardList) {
        devCardList = newDevCardList;
    }

    public void setResourceList(ResourceList newResourceList) {
        resourceList = newResourceList;
    }
}
