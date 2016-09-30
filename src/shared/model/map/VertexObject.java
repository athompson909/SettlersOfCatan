package shared.model.map;

import com.google.gson.annotations.SerializedName;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Created by Mitchell on 9/15/2016.
 * contains fields related to the locations of objects placed on the vertexes of hexes (settlements and cities)
 */
public class VertexObject {

    /**
     * PlayerID of the player who has built on this location
     * By default, no one owns it, so the value is -1.
     */
    private transient int owner = -1;

    /**
     * Vertex location of the vertex object.
     */
    private transient VertexLocation vertexLocation;

    /**
     * By default, the piece type is null since nothing is there until a player builds on it.
     */
    private PieceType pieceType = null;

    //constructor
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


    @Override
    public String toString() {
        return "VertexObject{" +
                "owner=" + owner +
                ", vertexLoc=" + vertexLocation +
                ", pieceType=" + pieceType +
                '}';
    }
}
