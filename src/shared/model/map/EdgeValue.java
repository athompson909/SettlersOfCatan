package shared.model.map;

import com.google.gson.annotations.SerializedName;
import shared.locations.EdgeLocation;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields relevant to the location of objects placed on edges of hexes (roads)
 */
public class EdgeValue {

    /**
     * PlayerID of player who is build on this hex edge
     * By default, no one owns any unoccupied road.
     */
    private int owner = -1;

    /**
     * X and Y coordinates and direction of hex this edge is associated with
     */
    @SerializedName("location")
    private EdgeLocation edgelocation;

    //constructor
    public EdgeValue(EdgeLocation edgeLocation){
        this.edgelocation = edgeLocation;
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
    public EdgeLocation getEdgeLocation() {
        return edgelocation;
    }

    /**
     * Sets Location of edgeValue to correct X and Y coordinates and direction
     * @param location
     */
    public void setLocation(EdgeLocation location) {
        this.edgelocation = location;
    }

    @Override
    public String toString() {
        return "EdgeValue{" +
                "owner=" + owner +
                ", edgelocation=" + edgelocation +
                '}';
    }
}
