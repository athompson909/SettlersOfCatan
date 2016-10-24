package client.map.mapStates;

import client.ClientFacade;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
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

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        if (edgeLocation1 == null) {
            edgeLocation1 = edgeLoc;
            //Draw it
            mapController.getView().placeRoad(edgeLoc, mapController.clientModel.getCurrentPlayer().getColor());

            EdgeValue edgeValue = new EdgeValue(edgeLoc);
            edgeValue.setOwner(mapController.clientModel.getCurrentPlayer().getPlayerIndex());
            mapController.clientModel.getMap().buildRoadManager.addTempRoad(edgeValue);
        } else {
            mapController.clientModel.getMap().buildRoadManager.removeTempRoad(edgeLocation1);
            int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
            PlayRoadBuilderCommand playRoadBuilderCommand = new PlayRoadBuilderCommand(currentPlayerId, edgeLocation1, edgeLoc);
            ClientFacade.getInstance().playRoadBuilding(playRoadBuilderCommand);
        }
    }

    @Override
    public void cancelMove() {
        System.out.println("MAPSTATE: CANCELMOVE");
        mapController.clientModel.getMap().buildRoadManager.removeTempRoad(edgeLocation1);
        initFromModel(mapController.clientModel.getMap());
    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        if(getFirstVertexLocation() == null) {
            mapController.getView().startDrop(pieceType, color, true);
        } else {
            mapController.getView().startDrop(pieceType, color, false);
        }
    }
}



