package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields relevant to the location of objects placed on edges of hexes (roads)
 */
public class EdgeValue {

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
