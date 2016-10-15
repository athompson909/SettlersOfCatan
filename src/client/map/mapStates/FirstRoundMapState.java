package client.map.mapStates;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.base.IView;
import client.data.RobPlayerInfo;
import client.map.MapComponent;
import client.map.MapController;
import client.turntracker.IState;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.commandmanager.moves.FinishTurnCommand;
import shared.model.map.Map;

/**
 * Used for placing road and settlement during the first round of the game
 * Created by Alise on 10/8/2016.
 */
//todo write all methods
public class FirstRoundMapState extends MapState{


    public FirstRoundMapState(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initFromModel(Map updatedMap) {
        super.initFromModel(updatedMap);

        if(Client.getInstance().getStartGame()) {
            startGame();
        }
    }

    public void startGame() {
        startMove(PieceType.ROAD, true, true);

        //Can't figure out how to force a settlement placement and then switch to next user
    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(pieceType, color, false);
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return mapController.clientModel.canPlaceSetUpRoad(edgeLoc);
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        return super.canPlaceSettlement(vertLoc);
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        super.placeRoad(edgeLoc);
        startMove(PieceType.SETTLEMENT, true, true);
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        super.placeSettlement(vertLoc);

        int currentPlayerIndex = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        FinishTurnCommand finishTurnCommand = new FinishTurnCommand(currentPlayerIndex);
        ClientFacade.getInstance().finishTurn(finishTurnCommand);
    }
}
