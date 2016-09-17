package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains the current location of the robber
 */
public class Robber {

    private HexLocation location;

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
