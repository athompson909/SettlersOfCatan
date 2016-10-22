package client.map.mapStates;

import client.map.MapController;
import shared.definitions.PieceType;
import shared.model.map.Map;

/**
 * Created by Mitchell on 10/15/2016.
 */
public class WaitingMapState extends MapState {
    public WaitingMapState(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initFromModel(Map updatedMap) {
        super.initFromModel(updatedMap);

    }
}
