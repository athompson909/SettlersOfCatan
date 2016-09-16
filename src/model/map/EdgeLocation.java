package model.map;

import java.awt.*;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * the location of an edge of a hex
 * the location is based off of an x y coordinate system and is connected to a hex's location
 */
public class EdgeLocation {

    private int x;

    private int y;

    private Direction direction;

    /**
     * decides whether or not a user is allowed to place an edge item (road) on a specified edge
     * @param color the color corresponding to the player
     * @return whether or not the player is allowed to place the item at a hex's edge's location
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
