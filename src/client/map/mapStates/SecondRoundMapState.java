package client.map.mapStates;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * Used for placing road and settlement during the first second of the game
 * Created by Alise on 10/8/2016.
 */
public class SecondRoundMapState extends MapState {
    public SecondRoundMapState(MapController mapController) {
        super(mapController);
    }
}
