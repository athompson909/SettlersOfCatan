package shared_tests;

import shared.locations.*;
import junit.framework.TestCase;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class BuildSettlementManagerTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;

    public void testBuildingOnNorthWestEdgeVertex() {
        System.out.println("testBuildingOnNorthWestEdgeVertex");
        //Test building vertex at northwest vertex of (-1,1)
        VertexLocation desiredLocation1 = new VertexLocation(new HexLocation(-1, 1), VertexDirection.NorthWest);
        EdgeLocation adjacentRoad1 = new EdgeLocation(new HexLocation(-1, 1), EdgeDirection.North);
        EdgeLocation adjacentRoad2 = new EdgeLocation(new HexLocation(-1, 1), EdgeDirection.NorthWest);

        assert (!map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER2, adjacentRoad2);
        assert (!map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, adjacentRoad1);
        assert (map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
//        map.buildSettlementManager.placeSettlement(PLAYER1, desiredLocation1);

        //Space is already occupied
       assert (!map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
    }

    public void testBuildingOnNorthEastEdgeVertex() {
        System.out.println("testBuildingOnNorthWestEdgeVertex");
        //Test building vertex at northe vertex of (1,2)
        VertexLocation desiredLocation1 = new VertexLocation(new HexLocation(1, 2), VertexDirection.NorthEast);
        EdgeLocation adjacentRoad1 = new EdgeLocation(new HexLocation(1, 2), EdgeDirection.North);
        EdgeLocation adjacentRoad2 = new EdgeLocation(new HexLocation(2, 1), EdgeDirection.NorthWest);

        assert (!map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER2, adjacentRoad2);
        assert (!map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, adjacentRoad1);
        assert (map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
//        map.buildSettlementManager.placeSettlement(PLAYER1, desiredLocation1 );

        //Space is already occupied
        assert (!map.buildSettlementManager.canPlace(PLAYER1, desiredLocation1));
    }
}