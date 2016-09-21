package client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * the location of a hex, each hex has a distinct HexLocation
 * the location is based off of an x y coordinate system
 */
public class HexLocation {

    /**
     * X value of hex's location
     */
    private int x;

    /**
     * Y value of hex's location
     */
    private int y;

    /**
     * @return X value of hex's location
     */
    public int getX() {
        return x;
    }

    /**
     * Sets X value of hex's location
     * @param x value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Y value of hex's location
     */
    public int getY() {
        return y;
    }

    /**
     * Sets Y value of hex's location
     * @param y value
     */
    public void setY(int y) {
        this.y = y;
    }
}
