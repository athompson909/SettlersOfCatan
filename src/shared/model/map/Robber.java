package shared.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains the current location of the robber
 */
public class Robber {

    /**
     * Contains the current location of the robber
     */
    private HexLocation location;

    /**
     * Gives the current location of the robber
     *
     * @return current location of robber
     */
    public HexLocation getLocation() {
        return location;
    }

    /**
     * Resets the location of the robber to wherever
     * the player has moved it
     *
     * @param location New location of the robber
     */
    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
