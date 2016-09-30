package shared_tests;

import shared.locations.*;
import junit.framework.TestCase;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class BuildCityManagerTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;

    public void testBuildingCity() {
        //Test building City
        VertexLocation desiredCityLocation = new VertexLocation(new HexLocation(0, -2), VertexDirection.NorthWest);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, desiredCityLocation));
        map.buildSettlementManager.placeSettlement(PLAYER1, desiredCityLocation);
        assert (map.buildCityManager.canPlaceCity(PLAYER1, desiredCityLocation));
        map.buildCityManager.placeCity(PLAYER1, desiredCityLocation);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, desiredCityLocation));
    }

    public void testTwoPlayersBuildingCities() {
        //Test Two players trying to build cities
        VertexLocation enemyCityLocation = new VertexLocation(new HexLocation(-3, 3), VertexDirection.NorthEast);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, enemyCityLocation));
        map.buildSettlementManager.placeSettlement(PLAYER2, enemyCityLocation);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, enemyCityLocation));
        map.buildCityManager.placeCity(PLAYER2, enemyCityLocation);

        //Already Occupied
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, enemyCityLocation));
        assert (!map.buildCityManager.canPlaceCity(PLAYER2, enemyCityLocation));
    }
}