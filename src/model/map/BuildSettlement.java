package model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * updates model to show that a settlement is built at specified location
 */
public class BuildSettlement implements BuildingManager {

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
     * @return whether or not a road is connected to the vertex corresponding to the desired location of where the player wants to place a settlement
     */
    @Override
    public boolean isRoadConnected() {
        return false;
    }

    /**
     * this is executed after canBuild(...) and isRoadConnected() are verified
     * @param location the location where the settlement will be built
     */
    public void build(HexLocation location) {

    }
}