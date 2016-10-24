package shared_tests;

import junit.framework.TestCase;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class MapTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize
    private final int PLAYER1 = 1;

    public void testMapHexes() {
        System.out.println("testMapHexes");
        //System.out.println(map.getHexes().size());
        assert (map.getHexes().size() == 37); //There should be 37 hexes.

        //I have personally verified this test case is accurate.
        String defaultHexesString = "Hex{location=HexLocation [x=2, y=1], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=0, y=-1], number=3, resource=WOOD}\n" +
                "Hex{location=HexLocation [x=0, y=0], number=11, resource=WHEAT}\n" +
                "Hex{location=HexLocation [x=-2, y=-1], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=0, y=1], number=4, resource=WOOD}\n" +
                "Hex{location=HexLocation [x=-2, y=0], number=5, resource=ORE}\n" +
                "Hex{location=HexLocation [x=0, y=2], number=8, resource=WHEAT}\n" +
                "Hex{location=HexLocation [x=0, y=3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=-2, y=1], number=2, resource=WHEAT}\n" +
                "Hex{location=HexLocation [x=-2, y=2], number=6, resource=WOOD}\n" +
                "Hex{location=HexLocation [x=-2, y=3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=3, y=-3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=3, y=-2], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=3, y=-1], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=1, y=-3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=3, y=0], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=1, y=-2], number=4, resource=BRICK}\n" +
                "Hex{location=HexLocation [x=1, y=-1], number=6, resource=ORE}\n" +
                "Hex{location=HexLocation [x=-1, y=-2], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=1, y=0], number=5, resource=BRICK}\n" +
                "Hex{location=HexLocation [x=1, y=1], number=10, resource=SHEEP}\n" +
                "Hex{location=HexLocation [x=-1, y=-1], number=8, resource=BRICK}\n" +
                "Hex{location=HexLocation [x=1, y=2], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=-1, y=0], number=10, resource=SHEEP}\n" +
                "Hex{location=HexLocation [x=-1, y=1], number=9, resource=SHEEP}\n" +
                "Hex{location=HexLocation [x=-3, y=0], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=-1, y=2], number=3, resource=ORE}\n" +
                "Hex{location=HexLocation [x=-3, y=1], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=-1, y=3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=-3, y=2], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=-3, y=3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=2, y=-3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=2, y=-2], number=11, resource=WOOD}\n" +
                "Hex{location=HexLocation [x=0, y=-3], number=0, resource=WATER}\n" +
                "Hex{location=HexLocation [x=2, y=-1], number=12, resource=SHEEP}\n" +
                "Hex{location=HexLocation [x=2, y=0], number=9, resource=WHEAT}\n" +
                "Hex{location=HexLocation [x=0, y=-2], number=0, resource=DESERT}";

        for (HexLocation key : map.getHexes().keySet()) {
            //System.out.println(map.getHexes().get(key).toString());
            assert (defaultHexesString.contains(map.getHexes().get(key).toString()));
        }
    }

    public void testMapPorts() {
        System.out.println("testMapPorts");
        //I have personally verified this test case is accurate.
        String defaultPortString = "Port: WHEAT at HexLocation [x=2, y=1] facing NorthWest\n" +
                "Port: THREE at HexLocation [x=-1, y=-2] facing South\n" +
                "Port: ORE at HexLocation [x=-3, y=0] facing SouthEast\n" +
                "Port: WOOD at HexLocation [x=0, y=3] facing North\n" +
                "Port: BRICK at HexLocation [x=-3, y=2] facing NorthEast\n" +
                "Port: THREE at HexLocation [x=-2, y=3] facing NorthEast\n" +
                "Port: SHEEP at HexLocation [x=3, y=-3] facing SouthWest\n" +
                "Port: THREE at HexLocation [x=3, y=-1] facing NorthWest\n" +
                "Port: THREE at HexLocation [x=1, y=-3] facing South";

        for (HexLocation key : map.getPorts().keySet()) {
            //System.out.println(map.getPorts().get(key).toString());
            assert (defaultPortString.contains(map.getPorts().get(key).toString()));
        }
    }

    public void testMapVertices(){
        System.out.println("testMapVertices");
        assert (map.getVertexObjects().size() == 0);
    }

    public void testMapEdges(){
        System.out.println("testMapEdges");
        assert (map.getEdgeObjects().size() == 0);
    }



}




