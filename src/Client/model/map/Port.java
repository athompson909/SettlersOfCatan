package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields relevant to hexes of HexType PORT
 */
public class Port {

    /**
     * Client.model.resourcebank.Resource that can be traded at this port
     */
    private String resource;

    /**
     * XY location of this port hex
     */
    private HexLocation location;

    /**
     * Direction of hex that port occupies
     */
    private String direction;

    /**
     * Ratio that resources may be traded at when using this port
     */
    private int ratio;

    /**
     * Constructor that sets all variables within port
     *
     * @param res Sets resource data member
     * @param loc Sets location data member
     * @param dir Sets direction data member
     * @param rat Sets ratio data member
     */
    Port(String res, HexLocation loc, String dir, int rat) {
        resource = res;
        location = loc;
        direction = dir;
        ratio = rat;
    }

    /**
     * @return Client.model.resourcebank.Resource type that can be traded
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return Location of port
     */
    public HexLocation getLocation() {
        return location;
    }

    /**
     * @return Direction of port
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @return Ratio of port
     */
    public int getRatio() {
        return ratio;
    }

}
