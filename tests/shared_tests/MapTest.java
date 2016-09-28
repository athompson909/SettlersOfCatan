package shared_tests;

import junit.framework.TestCase;
import shared.locations.HexLocation;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class MapTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize

    public void testMap() {
        testMapHexes();
        testMapPorts();
    }

    public void testMapHexes() {
        System.out.println(map.getHexes().size());
        assert (map.getHexes().size() == 37); //There should be 37 hexes.

        //I have personally verified this test case is accurate.
        String defaultHexesString = "HexType: WATER Number= 0 HexLocation [x=2, y=1]\n" +
                "HexType: WOOD Number= 3 HexLocation [x=0, y=-1]\n" +
                "HexType: WHEAT Number= 11 HexLocation [x=0, y=0]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-2, y=-1]\n" +
                "HexType: WOOD Number= 4 HexLocation [x=0, y=1]\n" +
                "HexType: ORE Number= 5 HexLocation [x=-2, y=0]\n" +
                "HexType: WHEAT Number= 8 HexLocation [x=0, y=2]\n" +
                "HexType: WATER Number= 0 HexLocation [x=0, y=3]\n" +
                "HexType: WHEAT Number= 2 HexLocation [x=-2, y=1]\n" +
                "HexType: WOOD Number= 6 HexLocation [x=-2, y=2]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-2, y=3]\n" +
                "HexType: WATER Number= 0 HexLocation [x=3, y=-3]\n" +
                "HexType: WATER Number= 0 HexLocation [x=3, y=-2]\n" +
                "HexType: WATER Number= 0 HexLocation [x=3, y=-1]\n" +
                "HexType: WATER Number= 0 HexLocation [x=1, y=-3]\n" +
                "HexType: WATER Number= 0 HexLocation [x=3, y=0]\n" +
                "HexType: BRICK Number= 4 HexLocation [x=1, y=-2]\n" +
                "HexType: ORE Number= 6 HexLocation [x=1, y=-1]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-1, y=-2]\n" +
                "HexType: BRICK Number= 5 HexLocation [x=1, y=0]\n" +
                "HexType: SHEEP Number= 10 HexLocation [x=1, y=1]\n" +
                "HexType: BRICK Number= 8 HexLocation [x=-1, y=-1]\n" +
                "HexType: WATER Number= 0 HexLocation [x=1, y=2]\n" +
                "HexType: SHEEP Number= 10 HexLocation [x=-1, y=0]\n" +
                "HexType: SHEEP Number= 9 HexLocation [x=-1, y=1]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-3, y=0]\n" +
                "HexType: ORE Number= 3 HexLocation [x=-1, y=2]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-3, y=1]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-1, y=3]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-3, y=2]\n" +
                "HexType: WATER Number= 0 HexLocation [x=-3, y=3]\n" +
                "HexType: WATER Number= 0 HexLocation [x=2, y=-3]\n" +
                "HexType: WOOD Number= 11 HexLocation [x=2, y=-2]\n" +
                "HexType: WATER Number= 0 HexLocation [x=0, y=-3]\n" +
                "HexType: SHEEP Number= 12 HexLocation [x=2, y=-1]\n" +
                "HexType: WHEAT Number= 9 HexLocation [x=2, y=0]\n" +
                "HexType: DESERT Number= 0 HexLocation [x=0, y=-2]";

        for (HexLocation key : map.getHexes().keySet()) {
            System.out.println(map.getHexes().get(key).toString());
            assert (defaultHexesString.contains(map.getHexes().get(key).toString()));
        }
    }

    private void testMapPorts() {
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
            System.out.println(map.getPorts().get(key).toString());
            assert (defaultPortString.contains(map.getPorts().get(key).toString()));
        }
    }


}




