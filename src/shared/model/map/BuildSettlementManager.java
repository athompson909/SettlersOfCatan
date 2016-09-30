package shared.model.map;

import shared.definitions.PieceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 * Created by Mitchell on 9/15/2016.
 * <p>
 * updates shared.model to show that a settlement is built at specified location
 */
public class BuildSettlementManager {

    private Map map;

    public BuildSettlementManager(Map map) {
        this.map = map;
    }


    /**
     * Validates whether or not player can build settlement in specified location
     * Checks that no other settlement is built at an adjacent vertex
     * Checks that there is a conneting road of the correct color
     */

    public boolean canPlace(int playerID, VertexLocation vertexLocation) {
        return (isVertexLocationAvailable(vertexLocation)
                && isVertexObjectTwoAway(vertexLocation)
                && isRoadConnected(playerID, vertexLocation)
        );
    }


    /**
     * Checks to validate if edgeLocation is already occupied or not
     *
     * @return true if location is empty
     */
    private boolean isVertexLocationAvailable(VertexLocation vertexLocation) {
        if (map.getVertexObjects().containsKey(vertexLocation)) {
            if (map.getVertexObjects().get(vertexLocation).getOwner() == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if there are any settlements (ANY color) located at a
     * vertex adjacent to location where player is trying to build
     *
     * @return true if there are no settlements located at adjacent vertex
     */
    private boolean isVertexObjectTwoAway(VertexLocation desiredVertexLocation) {
        //If the vertexLocation is a Northwest
        if (desiredVertexLocation.getDir().equals(VertexDirection.NorthWest)) {
            return isNorthWestVertexTwoAway(desiredVertexLocation);
        } else { //It is north east.
            return isNorthEastVertexTwoAway(desiredVertexLocation);
        }
    }

    private boolean isNorthWestVertexTwoAway(VertexLocation desiredVertexLocation) {
        //Iterate through all vertex objects...
        for (VertexLocation key : map.getVertexObjects().keySet()) {
            VertexObject tempVertexObject = map.getVertexObjects().get(key);
            VertexLocation tempVertexLocation = tempVertexObject.getVertexLocation();

            //If the desiredVertexObject has a neighbor on it's same hexe's northeast vertex, return false
            if (tempVertexLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc())) {
                if (tempVertexLocation.getDir().equals(VertexDirection.NorthEast)) {
                    if (tempVertexObject.getOwner() != -1) {
                        return false;
                    }
                }
            }
            //If the desiredVertexObject has a neighbor on it's hexes' southwest neighbor's northeast vertex, return false
            if (tempVertexLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest))) {
                if (tempVertexLocation.getDir().equals(VertexDirection.NorthEast)) {
                    if (tempVertexObject.getOwner() != -1) {
                        return false;
                    }
                }
            }
            //If the desiredVertexObject has a neighbor on it's hexes' northwest neighbor's northeast vertex, return false
            if (tempVertexLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest))) {
                if (tempVertexLocation.getDir().equals(VertexDirection.NorthEast)) {
                    if (tempVertexObject.getOwner() != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isNorthEastVertexTwoAway(VertexLocation desiredVertexLocation) {
        //Iterate through all vertex objects...
        for (VertexLocation key : map.getVertexObjects().keySet()) {
            VertexObject tempVertexObject = map.getVertexObjects().get(key);
            VertexLocation tempVertexLocation = tempVertexObject.getVertexLocation();

            //If the desiredVertexObject has a neighbor on it's same hexe's northeast vertex, return false
            if (tempVertexLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc())) {
                if (tempVertexLocation.getDir().equals(VertexDirection.NorthWest)) {
                    if (tempVertexObject.getOwner() != -1) {
                        return false;
                    }
                }
            }
            //If the desiredVertexObject has a neighbor on it's hexes' southwest neighbor's northeast vertex, return false
            if (tempVertexLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast))) {
                if (tempVertexLocation.getDir().equals(VertexDirection.NorthWest)) {
                    if (tempVertexObject.getOwner() != -1) {
                        return false;
                    }
                }
            }
            //If the desiredVertexObject has a neighbor on it's hexes' northwest neighbor's northeast vertex, return false
            if (tempVertexLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast))) {
                if (tempVertexLocation.getDir().equals(VertexDirection.NorthWest)) {
                    if (tempVertexObject.getOwner() != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Checks that there is a road of the correct color adjacent to
     * the vertex where player is trying to build
     *
     * @return true if there is an adjacent road
     */

    public boolean isRoadConnected(int playerID, VertexLocation vertexLocation) {
        if(vertexLocation.getDir().equals(VertexDirection.NorthWest)) {
            return doesNorthWestVertexHaveAdjacentRoads(playerID, vertexLocation);
        } else {
            return doesNorthEastVertexHaveAdjacentRoads(playerID, vertexLocation);
        }
    }

    private boolean doesNorthWestVertexHaveAdjacentRoads(int playerID, VertexLocation desiredVertexLocation) {
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the Northwest or Northeast edgeValues on the same hex, then the road is connected.
            if (tempEdgeLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeValue(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
                if (doesPlayerOwnEdgeValue(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northwest neighbor's northeast edgeValue, then the road is connected.
            else if (desiredVertexLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeValue(tempEdgeValue, EdgeDirection.NorthEast, playerID)) {
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    private boolean doesNorthEastVertexHaveAdjacentRoads(int playerID, VertexLocation desiredVertexLocation) {
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the Northwest or Northeast edgeValues on the same hex, then the road is connected.
            if (tempEdgeLocation.getHexLoc().equals(desiredVertexLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeValue(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
                if (doesPlayerOwnEdgeValue(tempEdgeValue, EdgeDirection.NorthEast, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northwest neighbor's northeast edgeValue, then the road is connected.
            else if (desiredVertexLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeValue(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }


    private boolean doesPlayerOwnEdgeValue(EdgeValue tempEdgeValue, EdgeDirection edgeDirection, int playerID) {
        if (tempEdgeValue.getEdgeLocation().getDir() == edgeDirection) {
            if (tempEdgeValue.getOwner() == playerID) {
                return true;
            }
        }
        return false;
    }


    /**
     * Executes on the server side
     * Updates model to reflect new settlement at specified location
     *
     * @param vertexLocation Location of new settlement
     * @param playerID       ID of player who owns settlement
     */
    public void placeSettlement(VertexLocation vertexLocation, int playerID) {
        map.getVertexObjects().get(vertexLocation).setOwner(playerID);
        map.getVertexObjects().get(vertexLocation).setPieceType(PieceType.SETTLEMENT);
    }
}