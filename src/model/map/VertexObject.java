package model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields related to the locations of objects placed on the vertexes of hexes (settlements and cities)
 */
public class VertexObject {

    /**
     * the player id of the owner of the object
     */
    private int owner;

    private EdgeLocation location;

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public EdgeLocation getLocation() {
        return location;
    }

    public void setLocation(EdgeLocation location) {
        this.location = location;
    }
}
