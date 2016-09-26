package shared.model.map;

import com.google.gson.annotations.SerializedName;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields related to the locations of objects placed on the vertexes of hexes (settlements and cities)
 */
public class VertexObject {

    /**
     * PlayerID of the player who has built on this location
     */
    private transient int owner;

    /**
     * Location of this vertex
     */
    private transient EdgeLocation location;

    //TEST for serialization only
    int x;
    int y;
    EdgeDirection direction;

    public VertexObject(int owner_in, EdgeLocation el_in)
    {
        setOwner(owner_in);
        setLocation(el_in);
        x = el_in.getX();
        y = el_in.getY();
        direction = el_in.getDir();
    }



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
