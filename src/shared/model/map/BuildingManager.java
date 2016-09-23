package shared.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * updates shared.model to show that an item has been built, implemented by BuildCity, BuildRoad, and BuildSettlement
 */
public interface BuildingManager {

    /**
     *
     * @param playerID
     * @param location
     * @param direction
     */
    public boolean canPlace(int playerID, EdgeLocation location, String direction);

    /**
     *
     * @return whether or not a road is connected to the item the player wants to build
     */
    public boolean isRoadConnected();
}
