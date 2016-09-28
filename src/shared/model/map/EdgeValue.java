package shared.model.map;

import shared.locations.EdgeLocation;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields relevant to the location of objects placed on edges of hexes (roads)
 */
public class EdgeValue {

    /**
     * PlayerID of player who is build on this hex edge
     */
    private int owner;

    /**
     * X and Y coordinates and direction of hex this edge is associated with
     */
    private EdgeLocation location;

    public EdgeValue(EdgeLocation edgeLocation){
        this.location = edgeLocation;
    }

    /**
     * @return PlayerID of owner of this edge
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Sets Owner to PlayerID of player who is built
     * @param owner PlayerID of player
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * @return location of edge
     */
    public EdgeLocation getLocation() {
        return location;
    }

    /**
     * Sets Location of edgeValue to correct X and Y coordinates and direction
     * @param location
     */
    public void setLocation(EdgeLocation location) {
        this.location = location;
    }
}
