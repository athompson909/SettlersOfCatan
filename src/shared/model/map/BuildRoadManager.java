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
        return (isRoadEmpty(edgeLocation) && isRoadConnected(playerID, edgeLocation));
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

                break;
            case NorthEast:

                break;
            default:
                System.out.println("ERROR in isRoadConnected!");
                return false;
        }
        return false;
    }

    private boolean areNorthEdgeNeighborsConnected(int playerID, EdgeLocation currentEdgeLocation){
        //Iterate through all edgeLocation objects...
        for (EdgeLocation key : map.getEdgeObjects().keySet()) {
            EdgeLocation tempEdgeLocation = map.getEdgeObjects().get(key).getEdgeLocation();
            EdgeValue tempEdgeValue = map.getEdgeObjects().get(key);

            //Check if edgeobject belongs to the same hex
            if(tempEdgeLocation.getHexLoc().equals(currentEdgeLocation.getHexLoc())){
                //Check if the same hexes northwest neighbor belongs to the player
                if(tempEdgeLocation.getDir() == EdgeDirection.NorthWest){
                    if(tempEdgeValue.getOwner() == playerID){
                        return true;
                    }
                }
                //Check if the same hexes northeast neighbor belongs to the player
                if(tempEdgeLocation.getDir() == EdgeDirection.NorthEast){
                    if(tempEdgeValue.getOwner() == playerID){
                        return true;
                    }
                }
            }

            //Check if the currentedgeObjects northweset neighbor's north east direction belongs to the player
            else if(currentEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(tempEdgeLocation.getHexLoc())){
                if(tempEdgeLocation.getDir() == EdgeDirection.NorthEast){
                    if(tempEdgeValue.getOwner() == playerID){
                        return true;
                    }
                }
            }

            //Check if the currentedgeObjects northeast neighbor's north west direction belongs to the player
            else if(currentEdgeLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(tempEdgeLocation.getHexLoc())){
                if(tempEdgeLocation.getDir() == EdgeDirection.NorthWest){
                    if(tempEdgeValue.getOwner() == playerID){
                        return true;
                    }
                }
            }
        }
        return false; //If you iterate through all and never get true, then return false
    }

    /**
     * Checks to validate if edgeLocation is already occupied or not
     *
     * @return true if location is empty
     */
    private boolean isRoadEmpty(EdgeLocation edgeLocation) {
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
     * @param loc      location of new road
     * @param playerID ID of player who owns road
     */
    public void build(EdgeLocation loc, int playerID) {

    }
}
