package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields related to the locations of objects placed on the vertexes of hexes (settlements and cities)
 */
public class VertexObject {

    /**
     * PlayerID of the player who has built on this location
     */
    private int owner;

    /**
     * Location of this vertex
     */
    private EdgeLocation location;

    /**
     * @return PlayerID of owner
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Sets owner to player who built on this vertex
     *
     * @param owner PlayerID of player who built on this vertex
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * @return location of vertex
     */
    public EdgeLocation getLocation() {
        return location;
    }

    /**
     * Sets location of vertex
     *
     * @param location Location of vertex
     */
    public void setLocation(EdgeLocation location) {
        this.location = location;
    }
}
