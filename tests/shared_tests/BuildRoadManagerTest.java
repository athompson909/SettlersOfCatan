package shared_tests;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import junit.framework.TestCase;
import shared.model.map.Map;


/**
 * Created by Mitchell on 9/26/2016.
 */
public class BuildRoadManagerTest extends TestCase {

    private Map map = new Map(false, false, false); //Don't randomize
    private final int PLAYER1 = 1;

    public void testBuildingOnNorthEdgeValue(){
        //Test building road at north of (-1,0)
        EdgeLocation desiredLocation1 = new EdgeLocation(new HexLocation(-1,0), EdgeDirection.North);
        EdgeLocation desiredLocation1neighbor = new EdgeLocation(new HexLocation(-1,0), EdgeDirection.NorthWest);

        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation1neighbor);
        assert (map.buildRoadManager.canPlace(PLAYER1,desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation1);

        //Spaces are already occupied
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1));
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1neighbor));

        //Test building road on border/ocean at north of (0,3)
        EdgeLocation desiredLocation2 =  new EdgeLocation(new HexLocation(0,3), EdgeDirection.North);
        EdgeLocation desiredLocation2neighbor = new EdgeLocation(new HexLocation(-1,3), EdgeDirection.NorthEast);

        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation2neighbor);
        assert (map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation2);

        //Spaces are already occupied
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2neighbor));
    }

    public void testBuildingOnNorthWestEdgeValue(){
        //Test building road at northwest of (1,-1)
        EdgeLocation desiredLocation1 = new EdgeLocation(new HexLocation(1,-1), EdgeDirection.NorthWest);
        EdgeLocation desiredLocation1neighbor = new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthEast);

        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation1neighbor);
        assert (map.buildRoadManager.canPlace(PLAYER1,desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation1);

        //Spaces are already occupied
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1));
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1neighbor));

        //Test building road on border/ocean at northwest of (2,1)
        EdgeLocation desiredLocation2 =  new EdgeLocation(new HexLocation(2,1), EdgeDirection.NorthWest);
        EdgeLocation desiredLocation2neighbor = new EdgeLocation(new HexLocation(2,1), EdgeDirection.North);

        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation2neighbor);
        assert (map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation2);

        //Spaces are already occupied
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2neighbor));
    }

    public void testBuildingOnNorthEastEdgeValue(){
        //Test building road at northeast of (-2,0)
        EdgeLocation desiredLocation1 = new EdgeLocation(new HexLocation(-2,0), EdgeDirection.NorthEast);
        EdgeLocation desiredLocation1neighbor = new EdgeLocation(new HexLocation(-2,0), EdgeDirection.North);

        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation1neighbor);
        assert (map.buildRoadManager.canPlace(PLAYER1,desiredLocation1));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation1);

        //Spaces are already occupied
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1));
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation1neighbor));

        //Test building road on border/ocean at northeast of (-3,2)
        EdgeLocation desiredLocation2 =  new EdgeLocation(new HexLocation(-3,2), EdgeDirection.NorthEast);
        EdgeLocation desiredLocation2neighbor = new EdgeLocation(new HexLocation(-2,2), EdgeDirection.North);

        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation2neighbor);
        assert (map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        map.buildRoadManager.placeRoad(PLAYER1, desiredLocation2);

        //Spaces are already occupied
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2));
        assert (!map.buildRoadManager.canPlace(PLAYER1, desiredLocation2neighbor));
    }




}
