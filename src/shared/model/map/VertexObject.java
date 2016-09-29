package shared.model.map;

import com.google.gson.annotations.SerializedName;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields related to the locations of objects placed on the vertexes of hexes (settlements and cities)
 */
public class VertexObject {

    /**
     * PlayerID of the player who has built on this location
     */
    private transient int owner = -1; //By default, no one owns it.

    private transient VertexLocation vertexLocation;

    private PieceType pieceType = null;

    public VertexObject(VertexLocation location){
        vertexLocation = location;

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

    public VertexLocation getVertexLocation() {
        return vertexLocation;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
