package client.utils;

import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;

/**
 * Created by adamthompson on 11/9/16.
 */
public class Converter {

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
