package shared.model.map;

import com.google.gson.annotations.SerializedName;
import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;
import java.util.Set;


/**
 * JSON COMING IN
 * resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What type
 resource this port trades for. If it's omitted, then it's for any resource.,
 location (HexLocation): Which hex this port is on. This shows the (ocean/non-existent) hex to
 draw the port on.,
 direction (string) = ['NW' or 'N' or 'NE' or 'E' or 'SE' or 'SW']: Which edge this port is on.,
 ratio (integer): The ratio for trade in (ie, if this is 2, then it's a 2:1 port.
 */

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains fields relevant to hexes of HexType PORT
 */
public class Port {

    /**
     * Resource that can be traded at this port
     *
     * By default, all ports are 2:1 ratio. If the port JSON is read in as a ratio 3
     * then we set the portType to enum THREE.
     *
     */
    @SerializedName("resource")
    private PortType portType;

    /**
     * XY location of this port hex
     */
    private HexLocation location;

    /**
     * Direction of hex that port occupies
     */
    @SerializedName("direction")
    private EdgeDirection edgeDirection;

    /**
     * Constructor that sets all variables within port
     *
     * @param res Sets resource data member
     * @param loc Sets location data member
     * @param dir Sets direction data member
     */
    public Port(PortType res, HexLocation loc, EdgeDirection dir) {
        portType = res;
        location = loc;
        edgeDirection = dir;
    }

    @Override
    public String toString()
    {
        return "Port: " + portType.toString() + " at " + location.toString() + " facing " + edgeDirection.toString();
    }

    public void update() {
        //CONVERT JSON STRING TO ENUM TYPES.
    }

    /**
     * @return Resource type that can be traded
     */
    public PortType getResource() {
        return portType;
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
    public EdgeDirection getEdgeDirection() {
        return edgeDirection;
    }

}
