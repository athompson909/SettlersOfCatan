package client.map.mapStates;

import client.ClientFacade;
import client.map.MapController;
import shared.locations.EdgeLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.BuildRoadCommand;
import shared.model.commandmanager.moves.PlayRoadBuilderCommand;
import shared.model.map.EdgeValue;
import shared.model.map.Map;

/**
 * Created by Mitchell on 10/15/2016.
 */
public class RoadBuildingMapState extends MapState {
    public RoadBuildingMapState(MapController mapController) {
        super(mapController);
    }

    private EdgeLocation edgeLocation1;

    public void placeRoad(EdgeLocation edgeLoc) {
        if(edgeLocation1 == null){
            edgeLocation1 = edgeLoc;
            //Draw it
            mapController.getView().placeRoad(edgeLoc,  mapController.clientModel.getCurrentPlayer().getColor());

            EdgeValue edgeValue = new EdgeValue(edgeLoc);
            edgeValue.setOwner(mapController.clientModel.getCurrentPlayer().getPlayerIndex());
            mapController.clientModel.getMap().buildRoadManager.addTempRoad(edgeValue);
        }
        else{
            int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
            PlayRoadBuilderCommand playRoadBuilderCommand = new PlayRoadBuilderCommand(currentPlayerId, edgeLocation1, edgeLoc);
            ClientFacade.getInstance().playRoadBuilding(playRoadBuilderCommand);
        }

    }
}
