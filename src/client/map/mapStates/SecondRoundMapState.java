package client.map.mapStates;

import client.Client;
import client.ClientFacade;
import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.commandmanager.moves.FinishTurnCommand;
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
        if(getSecondVertexLocation() == null) {
            mapController.startMove(PieceType.SETTLEMENT, true, false);
        }
     //   mapController.startMove(PieceType.SETTLEMENT, true, false);

    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return mapController.clientModel.canPlaceSetUpRoad(edgeLoc, super.getSecondVertexLocation());
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceSetUpSettlement(currentPlayerId, vertLoc);
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        super.placeRoad(edgeLoc);

        int currentPlayerIndex = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        FinishTurnCommand finishTurnCommand = new FinishTurnCommand(currentPlayerIndex);
        ClientFacade.getInstance().finishTurn(finishTurnCommand);

    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        super.setSecondVertexLocation(vertLoc);
        super.placeSettlement(vertLoc);
        startMove(PieceType.ROAD, true, true);
    }
}
