package shared.model.map;

import shared.definitions.PieceType;
import shared.locations.VertexLocation;

/**
 * Created by Mitchell on 9/15/2016.
 * <p>
 * updates shared.model to show that a city is built at specified location
 */
public class BuildCityManager {

    private Map map;

    public BuildCityManager(Map map) {
        this.map = map;
    }

    /**
     * Validates whether or not player may place city at location
     * Checks to see that player currently owns specified vertex
     * Checks that player has a settlemnt at specified location
     *
     * @param desiredVertexLocation VertexObject where player is trying to build city
     * @return true if player currently owns location AND has a settlement on it
     */
    public boolean canPlaceCity(int playerID, VertexLocation desiredVertexLocation) {
        VertexObject currentVertexObject = map.getVertexObjects().get(desiredVertexLocation);
        return (currentVertexObject.getPieceType() != null &&
                currentVertexObject.getPieceType().equals(PieceType.SETTLEMENT) &&
                currentVertexObject.getOwner() == playerID);
    }


    /**
     * executed on the server side
     * Updates model to reflect a new city
     *
     * @param desiredVertexLocation Vertex where city is built
     */
    public void placeCity(int playerID, VertexLocation desiredVertexLocation) {
        map.getVertexObjects().get(desiredVertexLocation).setPieceType(PieceType.CITY);
    }
}