package client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * the location of an edge of a hex
 * the location is based off of an x y coordinate system and is connected to a hex's location
 */
public class EdgeLocation {

    /**
     * X value of hex associated with this edge
     */
    private int x;

    /**
     * Y value of hex associated with thie edge
     */
    private int y;

    /**
     * Direction of hex associated with this edge
     */
    private Direction direction;

    /**
     * @return X value associated with this location
     */
    public int getX() {
        return x;
    }

    /**
     * Sets X value
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Y value associated with this location
     */
    public int getY() {
        return y;
    }

    /**
     * Sets Y value
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return Direction associated with this location
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets Direction
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
