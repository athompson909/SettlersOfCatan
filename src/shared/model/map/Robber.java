package shared.model.map;

import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 * Created by Mitchell on 9/15/2016.
 * contains the current location of the robber
 */
public class Robber {

    /**
     * Contains the current location of the robber
     */
    private HexLocation currentHexlocation;

    /**
     * A reference to the map.
     */
    private transient Map map;

    public Robber(Map map) {
        this.map = map;
    }

    /**
     * Checks to see if the Robber can be placed at a desired Hex Location.
     * @param desiredHexLocation to place the robber.
     * @return true if the location is not water, and not currently where the robber is at.
     */
    public boolean canPlaceRobber(HexLocation desiredHexLocation) {
        if (map.getHexes().get(desiredHexLocation).getResource().equals(HexType.WATER)) {
            return false;
        } else if (desiredHexLocation.equals(currentHexlocation)) {
            return false;
        }
        return true;
    }

    public void placeRobber(HexLocation hexLocation) {
        this.currentHexlocation = hexLocation;
    }


    /**
     * Gives the current location of the robber
     * @return current location of robber
     */
    public HexLocation getLocation() {
        System.out.println("Robber is at " + currentHexlocation.toString());
        return currentHexlocation;
    }


    public HexLocation getCurrentHexlocation() {
        return currentHexlocation;
    }

    public void setCurrentHexlocation(HexLocation currentHexlocation) {
        this.currentHexlocation = currentHexlocation;
    }

    @Override
    public String toString() {
        return "Robber{" +
                "currentHexloc=" + currentHexlocation +
                ", map=" + map +
                '}';
    }
}
