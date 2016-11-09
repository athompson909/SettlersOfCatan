package shared.shared_utils;

import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;

/**
 * Created by adamthompson on 11/9/16.
 */
public class Converter {

    public static HexType stringToHexType(String hexType){
        switch (hexType){
            case "wood":
                return HexType.WOOD;
            case "brick":
                return HexType.BRICK;
            case "sheep":
                return HexType.SHEEP;
            case "wheat":
                return HexType.WHEAT;
            case "ore":
                return HexType.ORE;
            default:
                return null;
        }
    }


    public static PortType stringToPortType(String portType){
        switch (portType){
            case "wood":
                return PortType.WOOD;
            case "brick":
                return PortType.BRICK;
            case "sheep":
                return PortType.SHEEP;
            case "wheat":
                return PortType.WHEAT;
            case "ore":
                return PortType.ORE;
            case "three":
                return PortType.THREE;
            default:
                return null;
        }
    }


    public static ResourceType stringToResourceType(String resource) {
        switch (resource) {
            case "wood":
                return ResourceType.WOOD;
            case "brick":
                return ResourceType.BRICK;
            case "sheep":
                return ResourceType.SHEEP;
            case "wheat":
                return ResourceType.WHEAT;
            case "ore":
                return ResourceType.ORE;
            default:
                assert false;
        }
        return null;//should never reach here
    }

    public static EdgeDirection stringToEdgeDirection(String dir) {
        switch (dir) {
            case "NW":
                return EdgeDirection.NorthWest;
            case "N":
                return EdgeDirection.North;
            case "NE":
                return EdgeDirection.NorthEast;
            case "SE":
                return EdgeDirection.SouthEast;
            case "S":
                return EdgeDirection.South;
            case "SW":
                return EdgeDirection.SouthWest;
            default:
                assert false;
        }
        return null;//should never reach here
    }


    public static VertexDirection stringToVertexDirection(String dir) {
        switch (dir) {
            case "NW":
                return VertexDirection.NorthWest;
            case "W":
                return VertexDirection.West;
            case "NE":
                return VertexDirection.NorthEast;
            case "SE":
                return VertexDirection.SouthEast;
            case "E":
                return VertexDirection.East;
            case "SW":
                return VertexDirection.SouthWest;
            default:
                assert false;
        }
        return null;//should never reach here
    }

}
