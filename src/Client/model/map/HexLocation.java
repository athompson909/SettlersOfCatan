package Client.model.map;

import java.awt.*;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * the location of a hex, each hex has a distinct HexLocation
 * the location is based off of an x y coordinate system
 */
public class HexLocation {

    // it looks tempting to make x and y public...
    private int x;

    private int y;

    /**
     * decides whether or not user can place an item at a particular hex location
     * @param color the color corresponding to the player's id
     * @return whether or not the player is allowed to place the item at hex's location
     */
    public boolean canPlace(Color color) {
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
