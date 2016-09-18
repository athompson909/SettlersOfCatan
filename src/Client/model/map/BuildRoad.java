package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class BuildRoad implements BuildingManager {

    /**
     * Validates whether or not player can build road in specified location
     * Checks that edgeLocation is empty
     * Checks that there is an adjacent road of the correct color
     *
     * @param playerID the id corresponding to the player who is building
     * @param location Location where road will be built
     * @param direction Direction where road will be built
     * @return true if location is available for player
     */
    @Override
    public boolean canPlace(int playerID, EdgeLocation location, String direction) {
        return false;
    }

    /**
     * Checks that there is a road of the correct color adjacent to
     * the vertex where player is trying to build
     *
     * @return true if there is an adjacent road
     * */
    @Override
    public boolean isRoadConnected() {
        return false;
    }

    /**
     * Checks to validate if edgeLocation is already occupied or not
     *
     * @return true if location is empty
     */
    public boolean isRoadEmpty() {
        return false;
    }

    /**
     * Executes on the server side
     * Updates model to reflect new road at specified location
     *
     * @param loc location of new road
     * @param playerID ID of player who owns road
     */
    public void build(EdgeLocation loc, int playerID) {

    }
}
