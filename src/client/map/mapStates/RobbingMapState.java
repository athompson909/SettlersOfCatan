package client.map.mapStates;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * Used when you roll a 7 or play a soldier card
 * Created by Alise on 10/8/2016.
 */
public class RobbingMapState extends MapState {
    public RobbingMapState(MapController mapController) {
        super(mapController);
    }
}
