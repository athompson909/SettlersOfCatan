package client.map.mapStates;

import client.Client;
import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;

/**
 * Used when you roll a 7 or play a soldier card
 * Created by Alise on 10/8/2016.
 */
public class RobbingMapState extends MapState {
    public RobbingMapState(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initFromModel(Map updatedMap) {
        super.initFromModel(updatedMap);

        //TODO: Only do this if it is your turn?
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(PieceType.ROBBER, color, true);
    }
}
