package client.map.mapStates;

import client.Client;
import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;

/**
 * Used for placing road and settlement during the first second of the game
 * Created by Alise on 10/8/2016.
 */
public class SecondRoundMapState extends MapState {
    public SecondRoundMapState(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initFromModel(Map updatedMap) {
        super.initFromModel(updatedMap);

        mapController.startMove(PieceType.ROAD, true, true);

    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return true;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        return super.canPlaceSettlement(vertLoc);
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        super.placeRoad(edgeLoc);
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        super.placeSettlement(vertLoc);
    }
}
