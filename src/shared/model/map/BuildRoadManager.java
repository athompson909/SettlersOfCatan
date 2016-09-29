package shared.model.map;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class BuildRoadManager {

    private Map map;

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

    /**
     * Checks to see if the player has roads connected to a northEdge Value
     * @param playerID of the player.
     * @param desiredEdgeLocation the player wishes to build on.
     * @return
     */
    private boolean areNorthEdgeNeighborsConnected(int playerID, EdgeLocation desiredEdgeLocation){
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the Northwest or Northeast edgeValues on the same hex, then the road is connected.
            if(tempEdgeLocation.getHexLoc().equals(desiredEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)) {
                    return true;
                }
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)){
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northwest neighbor's northeast edgeValue, then the road is connected.
            else if(desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(tempEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)){
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northeast neighbor's northwest edgeValue, then the road is connected.
            else if(desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(tempEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)){
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    private boolean areNorthWestEdgeNeighborsConnected(int playerID, EdgeLocation desiredEdgeLocation){
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the North edgeValue on the same hex, then the road is connected.
            if(tempEdgeLocation.getHexLoc().equals(desiredEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northwest neighbor's northeast edgeValue, then the road is connected.
            else if(desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(tempEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)){
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes southeast neighbor's north or northeast edge value, then the road is conected.
            else if(desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest).equals(tempEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthEast, playerID)){
                    return true;
                }
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.North, playerID)){
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    private boolean areNorthEastEdgeNeighborsConnected(int playerID, EdgeLocation desiredEdgeLocation){
        //Iterate through all edgeValues...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);
            EdgeLocation tempEdgeLocation = tempEdgeValue.getEdgeLocation();

            //If the player owns the North edgeValue on the same hex, then the road is connected.
            if(tempEdgeLocation.getHexLoc().equals(desiredEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.North, playerID)) {
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes northeast neighbor's northwest edgeValue, then the road is connected.
            else if(desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(tempEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)){
                    return true;
                }
            }
            //If the player owns the edgeValue on hexes southe neighbor's north or northeast edge value, then the road is conected.
            else if(desiredEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast).equals(tempEdgeLocation.getHexLoc())){
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.NorthWest, playerID)){
                    return true;
                }
                if(doesPlayerOwnDirection(tempEdgeValue, EdgeDirection.North, playerID)){
                    return true;
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    private boolean doesPlayerOwnDirection(EdgeValue tempEdgeValue, EdgeDirection edgeDirection, int playerID){
        if(tempEdgeValue.getEdgeLocation().getDir() == edgeDirection){
            if(tempEdgeValue.getOwner() == playerID){
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
        if(map.getEdgeObjects().containsKey(edgeLocation)){
            if(map.getEdgeObjects().get(edgeLocation).getOwner() == -1){
                return true;
            }
        }
        return false;
    }

    /**
     * Executes on the server side
     * Updates model to reflect new road at specified location
     *
     * @param edgeLocation     location of new road
     * @param playerID ID of player who owns road
     */
    public void placeRoad(int playerID, EdgeLocation edgeLocation) {
        map.getEdgeObjects().get(edgeLocation).setOwner(playerID);
    }
}
