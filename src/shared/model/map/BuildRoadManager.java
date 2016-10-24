package shared.model.map;

import shared.locations.*;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class BuildRoadManager {

    /**
     * Pointer reference to the original map.
     */
    private Map map;

    /**
     * Constructor for the BuildRoadManager
     *
     * @param map reference to the game map.
     */
    public BuildRoadManager(Map map) {
        this.map = map;
    }

    /**
     * Validates whether or not player can build road in specified location
     * Checks that edgeLocation is empty
     * Checks that there is an adjacent road of the correct color
     *
     * @param playerID the id corresponding to the player who is building
     * @return true if location is available for player
     */
    public boolean canPlace(int playerID, EdgeLocation edgeLocation) {
        return (isEdgeLocationAvailable(edgeLocation) && isRoadConnected(playerID, edgeLocation));
    }

    /**
     * Checks for valid road placement during the setup round.
     *
     * @param desiredEdgeLocation to place the road.
     * @param firstVertexLocation of the settlement placed before.
     * @return true if the road is connected to the settlement the player has placed.
     */
    public boolean canPlaceSetUpRound(EdgeLocation desiredEdgeLocation, VertexLocation firstVertexLocation) {
        return (isEdgeLocationAvailable(desiredEdgeLocation) && isSettlementConnected(firstVertexLocation, desiredEdgeLocation));
    }


    /**
     * Checks that there is a road of the correct color adjacent to
     * the vertex where player is trying to build
     *
     * @return true if there is an adjacent road
     */
    private boolean isRoadConnected(int playerID, EdgeLocation currentEdgeLocation) {
        switch (currentEdgeLocation.getDir()) {
            //When looking at a North EdgeValue
            case North:
                return areNorthEdgeNeighborsConnected(playerID, currentEdgeLocation);
            case NorthWest:
                return areNorthWestEdgeNeighborsConnected(playerID, currentEdgeLocation);
            case NorthEast:
                return areNorthEastEdgeNeighborsConnected(playerID, currentEdgeLocation);
            default:
                System.out.println("ERROR in isRoadConnected!");
                return false;
        }
    }

    private boolean opposingPlayerOwnsSettlement(int playerID, EdgeLocation vertexLocation){
        if(map.getVertexObjects().containsKey(vertexLocation)){
            if(map.getVertexObjects().get(vertexLocation).getOwner() != playerID){
                return false;
            }
        }
        return true;
    }


    /**
     * Checks to see if the player has roads connected to a northEdge Value
     *
     * @param playerID            of the player.
     * @param desiredEdgeLocation the player wishes to build on.
     * @return
     */
    private boolean areNorthEdgeNeighborsConnected(int playerID, EdgeLocation desiredEdgeLocation) {
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the Northwest or Northeast edgeValues on the same hex, then the road is connected.
            if (tempEdgeLocation.getHexLoc().equals(desiredEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    if(opposingPlayerOwnsSettlement(playerID, desiredEdgeLocation))
                    return true;
                }
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northwest neighbor's northeast edgeValue, then the road is connected.
            else if (desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northeast neighbor's northwest edgeValue, then the road is connected.
            else if (desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    /**
     * Checks to see if the player has roads connected to a northWestEdge Value
     *
     * @param playerID            of the player.
     * @param desiredEdgeLocation the player wishes to build on.
     * @return
     */
    private boolean areNorthWestEdgeNeighborsConnected(int playerID, EdgeLocation desiredEdgeLocation) {
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the North edgeValue on the same hex, then the road is connected.
            if (tempEdgeLocation.getHexLoc().equals(desiredEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northwest neighbor's northeast edgeValue, then the road is connected.
            else if (desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes southeast neighbor's north or northeast edge value, then the road is conected.
            else if (desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)) {
                    return true;
                }
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    /**
     * Checks to see if the player has roads connected to a northEastEdge Value
     *
     * @param playerID            of the player.
     * @param desiredEdgeLocation the player wishes to build on.
     * @return
     */
    private boolean areNorthEastEdgeNeighborsConnected(int playerID, EdgeLocation desiredEdgeLocation) {
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the North edgeValue on the same hex, then the road is connected.
            if (tempEdgeLocation.getHexLoc().equals(desiredEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northeast neighbor's northwest edgeValue, then the road is connected.
            else if (desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes southe neighbor's north or northeast edge value, then the road is conected.
            else if (desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast).equals(tempEdgeLocation.getHexLoc())) {
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    return true;
                }
                if (doesPlayerOwnEdgeDirection(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    /**
     * Checks to see if the player owns the edgedirection
     *
     * @param tempEdgeValue Edge value being compared
     * @param edgeDirection Direction that value is in
     * @param playerID      of the player.
     * @return
     */
    private boolean doesPlayerOwnEdgeDirection(EdgeValue tempEdgeValue, EdgeDirection edgeDirection, int playerID) {
        if (tempEdgeValue.getEdgeLocation().getDir() == edgeDirection) {
            if (tempEdgeValue.getOwner() == playerID) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks to validate if edgeLocation is already occupied or not
     *
     * @return true if location is empty
     */
    private boolean isEdgeLocationAvailable(EdgeLocation edgeLocation) {
        if (map.getAllValidEdgeLocations().containsKey(edgeLocation)) {
            if (!map.getEdgeObjects().containsKey(edgeLocation)) {
                //if(map.getEdgeObjects().get(edgeLocation).getOwner() == -1){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a vertex location is adjacent to the desired edge location.
     *
     * @param firstVertexLocation of the settlement placed.
     * @param desiredEdgeLocation adjacent to tthe first vertex location.
     * @return
     */
    private boolean isSettlementConnected(VertexLocation firstVertexLocation, EdgeLocation desiredEdgeLocation) {
        HexLocation currentHexLocation = firstVertexLocation.getHexLoc();

        //If VertexObject is a Northwest vertex object
        if (firstVertexLocation.getDir().equals(VertexDirection.NorthWest)) {
            return isNorthWestVertexObjectHasAdjacentRoad(currentHexLocation, desiredEdgeLocation);
        } else {//If our vertex object is a north east vertex object
            return isNorthEastVertexObjectHasAdjacentRoad(currentHexLocation, desiredEdgeLocation);
        }
    }

    /**
     * Checks to see if a North West vertex object has an adjacent edge location
     *
     * @param desiredEdgeLocation of where the player wants to place the road.
     * @param currentHexLocation  of the vertex object.
     * @return true if the player has chosen an edge location adjacent to the chosen vertex location.
     */
    private boolean isNorthWestVertexObjectHasAdjacentRoad(HexLocation currentHexLocation, EdgeLocation desiredEdgeLocation) {

        EdgeLocation eastEdgeLocation = new EdgeLocation(currentHexLocation, EdgeDirection.North);
        EdgeLocation northWestEdgeLocation = new EdgeLocation(currentHexLocation.getNeighborLoc(EdgeDirection.NorthWest), EdgeDirection.NorthEast);
        EdgeLocation southWestEdgeLocation = new EdgeLocation(currentHexLocation, EdgeDirection.NorthWest);

        if (eastEdgeLocation.equals(desiredEdgeLocation)) {
            return true;
        } else if (northWestEdgeLocation.equals(desiredEdgeLocation)) {
            return true;
        } else if (southWestEdgeLocation.equals(desiredEdgeLocation)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks to see if a North East vertex object has an adjacent edge location
     *
     * @param desiredEdgeLocation of where the player wants to place the road.
     * @param currentHexLocation  of the vertex object.
     * @return true if the player has chosen an edge location adjacent to the chosen vertex location.
     */
    private boolean isNorthEastVertexObjectHasAdjacentRoad(HexLocation currentHexLocation, EdgeLocation desiredEdgeLocation) {
        EdgeLocation westEdgeLocation = new EdgeLocation(currentHexLocation, EdgeDirection.North);
        EdgeLocation northEastEdgeLocation = new EdgeLocation(currentHexLocation.getNeighborLoc(EdgeDirection.NorthEast), EdgeDirection.NorthWest);
        EdgeLocation southEastEdgeLocation = new EdgeLocation(currentHexLocation, EdgeDirection.NorthEast);

        if (westEdgeLocation.equals(desiredEdgeLocation)) {
            return true;
        } else if (northEastEdgeLocation.equals(desiredEdgeLocation)) {
            return true;
        } else if (southEastEdgeLocation.equals(desiredEdgeLocation)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Executes on the server side
     * Updates model to reflect new road at specified location
     *
     * @param edgeLocation location of new road
     * @param playerID     ID of player who owns road
     */
    public void placeRoad(int playerID, EdgeLocation edgeLocation) {
        map.getEdgeObjects().get(edgeLocation).setOwner(playerID);
    }

    public void addTempRoad(EdgeValue edgeValue){
        map.getEdgeObjects().put(edgeValue.getEdgeLocation(), edgeValue);
    }

    public void removeTempRoad(EdgeLocation edgeLocation){
        map.getEdgeObjects().remove(edgeLocation);
    }

}
