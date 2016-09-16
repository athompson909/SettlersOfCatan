package model.map;

/**
 * Created by Mitchell on 9/15/2016.
 */
public interface BuildingManager {

    /**
     * decides if the player can build an item
     * @param playerId the id corresponding to the player
     * @return whether or not the player is allowed to build
     */
    public boolean canBuild(int playerId);

    /**
     *
     * @return whether or not a road is connected to the item the player wants to build
     */
    public boolean isRoadConnected();
}
