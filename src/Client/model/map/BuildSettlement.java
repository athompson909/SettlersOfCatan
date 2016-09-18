package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * updates Client.model to show that a settlement is built at specified location
 */
public class BuildSettlement implements BuildingManager {


    /**
     * Validates whether or not player can build settlement in specified location
     * Checks that no other settlement is built at an adjacent vertex
     * Checks that there is a conneting road of the correct color
     *
     * @param playerID ID of player trying to build
     * @param loc EdgeLocation where player is trying to build settlement
     * @param dir Direction of edgelocation where player is trying to build
     */
    @Override
    public boolean canPlace(int playerID, EdgeLocation loc, String dir) {
        return false;
    }

    /**
     * Checks that there is a road of the correct color adjacent to
     * the vertex where player is trying to build
     *
     * @return true if there is an adjacent road
     */
    @Override
    public boolean isRoadConnected() {
        return false;
    }

    /**
     * Checks to see if there are any settlements (ANY color) located at a
     * vertex adjacent to location where player is trying to build
     *
     * @return true if there are no settlements located at adjacent vertex
     */
    public boolean isTwoAway() {
        return false;
    }

    /**
     * Executes on the server side
     * Updates model to reflect new settlement at specified location
     *
     * @param loc Location of new settlement
     * @param playerID ID of player who owns settlement
     */
    public void build(EdgeLocation loc, int playerID) {

    }
}
