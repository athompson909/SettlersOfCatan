package shared.model.map;

import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 * Created by Mitchell on 9/15/2016.
 * <p>
 * contains the current location of the robber
 */
public class Robber {

    /**
     * Contains the current location of the robber
     */
    private HexLocation currentHexlocation;
    private Map map;

    public Robber(Map map) {
        this.map = map;
    }

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
     *
     * @return current location of robber
     */
    public HexLocation getLocation() {
        System.out.println("Robber is at " + currentHexlocation.toString());
        return currentHexlocation;
    }

}
