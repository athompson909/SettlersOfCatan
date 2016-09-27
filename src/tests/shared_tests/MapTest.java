package tests.shared_tests;

import junit.framework.TestCase;
import shared.definitions.HexType;
import shared.locations.HexLocation;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class MapTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize

    public void testMap() {
        assert (map.getHexes().get(new HexLocation(0,-2)).getHexType() == HexType.DESERT);

    }


}




