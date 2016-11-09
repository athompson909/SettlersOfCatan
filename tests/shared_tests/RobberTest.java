package shared_tests;

import shared.locations.*;
import junit.framework.TestCase;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class RobberTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize

    public void testRobber() {
        System.out.println("testRobber");
        HexLocation desert = new HexLocation(0, -2);
        HexLocation belowDesert = new HexLocation(0, -1);
        HexLocation waterHex = new HexLocation(0, -3);
        assert (map.getRobber().getLocation().equals(desert));

        assert (!map.getRobber().canPlaceRobber(desert));
        assert (map.getRobber().canPlaceRobber(belowDesert));
        map.getRobber().placeRobber(belowDesert);
        assert (!map.getRobber().canPlaceRobber(belowDesert));
        assert (map.getRobber().canPlaceRobber(desert));

        //assert (!map.getRobber().canPlaceRobber(waterHex));



    }

}