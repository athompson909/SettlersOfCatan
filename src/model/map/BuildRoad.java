package model.map;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class BuildRoad implements BuildingManager {

    /**
     *
     * @param playerId the id corresponding to the player
     * @return whether or not a player can build
     */
    @Override
    public boolean canBuild(int playerId) {
        return false;
    }

    /**
     *
     * @return whether or not a road is connected to either of the vertices corresponding to the desired location of where the player wants to place a road
     */
    @Override
    public boolean isRoadConnected() {
        return false;
    }

    /**
     * this is executed after canBuild(...) and isRoadConnected() are verified
     * @param location the location where the road will be built
     */
    public void build(EdgeLocation location) {

    }
}
