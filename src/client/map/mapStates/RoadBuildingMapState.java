package client.map.mapStates;

import client.ClientFacade;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.model.commandmanager.moves.PlayRoadBuilderCommand;
import shared.model.map.EdgeValue;

/**
 * Created by Mitchell on 10/15/2016.
 */
public class RoadBuildingMapState extends MapState {
    public RoadBuildingMapState(MapController mapController) {
        super(mapController);
    }

    private EdgeLocation edgeLocation1;

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        if (edgeLocation1 == null) {
            edgeLocation1 = edgeLoc;
            //Draw it
            mapController.getView().placeRoad(edgeLoc, mapController.clientModel.getClientPlayer().getColor());

            EdgeValue edgeValue = new EdgeValue(edgeLoc);
            edgeValue.setOwner(mapController.clientModel.getClientPlayer().getPlayerIndex());
            mapController.clientModel.getMap().buildRoadManager.addTempRoad(edgeValue);
            CatanColor color = mapController.clientModel.getClientPlayer().getColor();
            mapController.getView().startDrop(PieceType.ROAD, color, false);
        } else {
            mapController.clientModel.getMap().buildRoadManager.removeTempRoad(edgeLocation1);
            int currentPlayerId = mapController.clientModel.getClientPlayer().getPlayerIndex();
            PlayRoadBuilderCommand playRoadBuilderCommand = new PlayRoadBuilderCommand(currentPlayerId, edgeLocation1, edgeLoc);
            ClientFacade.getInstance().playRoadBuilding(playRoadBuilderCommand);
        }
    }

    @Override
    public void cancelMove() {
        System.out.println("MAPSTATE: CANCELMOVE");
        mapController.clientModel.getMap().buildRoadManager.removeTempRoad(edgeLocation1);
        super.cancelMove();
    }

}



