package shared_tests;

import shared.definitions.PieceType;
import shared.locations.*;
import junit.framework.TestCase;
import shared.model.map.Map;
import shared.model.map.VertexObject;
import shared.model.player.Player;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class BuildCityManagerTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;

    public void testBuildingCity() {
        //Test building City
        System.out.println("testBuildingCity");
        VertexLocation desiredCityLocation = new VertexLocation(new HexLocation(0, -2), VertexDirection.NorthWest);
        VertexObject desiredVertexObject = new VertexObject(desiredCityLocation);
        desiredVertexObject.setOwner(PLAYER1);
        desiredVertexObject.setPieceType(PieceType.SETTLEMENT);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, desiredCityLocation));
        map.buildSettlementManager.placeSettlement(desiredVertexObject);
        assert (map.buildCityManager.canPlaceCity(PLAYER1, desiredCityLocation));
        desiredVertexObject.setPieceType(PieceType.CITY);
        map.buildCityManager.placeCity(desiredVertexObject);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, desiredCityLocation));
    }

    public void testTwoPlayersBuildingCities() {
        System.out.println("testTwoPlayersBuildingCities");
        //Test Two players trying to build cities
        VertexLocation enemyCityLocation = new VertexLocation(new HexLocation(-3, 3), VertexDirection.NorthEast);
        VertexObject enemyVertexObject = new VertexObject(enemyCityLocation);
        enemyVertexObject.setOwner(PLAYER2);
        enemyVertexObject.setPieceType(PieceType.SETTLEMENT);
        assert (!map.buildCityManager.canPlaceCity(PLAYER1, enemyCityLocation));
        map.buildSettlementManager.placeSettlement(enemyVertexObject);
        //assert (!map.buildCityManager.canPlaceCity(PLAYER1, enemyCityLocation));
       // map.buildCityManager.placeCity(PLAYER2, enemyCityLocation);

        //Already Occupied
        //assert (!map.buildCityManager.canPlaceCity(PLAYER1, enemyCityLocation));
       // assert (!map.buildCityManager.canPlaceCity(PLAYER2, enemyCityLocation));
    }
}