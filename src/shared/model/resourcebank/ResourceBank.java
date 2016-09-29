package shared.model.resourcebank;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

/**
 * The Resource Bank contains a DevCardList and ResourceCardList,
 * and keeps track of all the available cards not owned by any player.
 *
 * The JSON for the ResourceBank doesn't have any data on what Dev Cards are used/available...
 */
public class ResourceBank {

    private DevCardList devCardList;
    private ResourceList resourceList;

    private final int AVAILABLE_SOLDIER_CARDS = 15;
    private final int AVAILABLE_MONUMENT_CARDS = 5;
    private final int AVAILABLE_YEAROFPLENTY_CARDS = 2;
    private final int AVAILABLE_ROADBUILDING_CARDS = 2;
    private final int AVAILABLE_MONOPOLY_CARDS = 2;

    private final int AVAILABLE_WOOD_CARDS = 19;
    private final int AVAILABLE_BRICK_CARDS = 19;
    private final int AVAILABLE_SHEEP_CARDS = 19;
    private final int AVAILABLE_WHEAT_CARDS = 19;
    private final int AVAILABLE_ORE_CARDS = 19;

    /**
     * Creates a new ResourceBank for the game using the available card constants for the devCardList and ResourceList
     */
    public ResourceBank() {
        devCardList = new DevCardList(
                AVAILABLE_SOLDIER_CARDS, AVAILABLE_MONUMENT_CARDS, AVAILABLE_YEAROFPLENTY_CARDS, AVAILABLE_ROADBUILDING_CARDS, AVAILABLE_MONOPOLY_CARDS);
        resourceList = new ResourceList(
                AVAILABLE_WOOD_CARDS, AVAILABLE_BRICK_CARDS, AVAILABLE_SHEEP_CARDS, AVAILABLE_WHEAT_CARDS, AVAILABLE_ORE_CARDS);
    }

    public void updateResourceBank(ResourceBank newResBank) {
        setDevCardList(newResBank.getDevCardList());
        setResourceList(newResBank.getResourceList());
    }

    public boolean hasDevCards() {
        return devCardList.isEmpty();
    }

    public DevCardType removeRandomDevCard() {
        return devCardList.removeRandomCard();
    }

    public void playYearOfPlenty(ResourceType resource1, ResourceType resource2) {
        resourceList.removeCardByType(resource1);
        resourceList.removeCardByType(resource2);
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
