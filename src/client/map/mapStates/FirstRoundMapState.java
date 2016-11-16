package client.map.mapStates;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.definitions.State;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.commandmanager.moves.BuildRoadCommand;
import shared.model.commandmanager.moves.BuildSettlementCommand;
import shared.model.commandmanager.moves.FinishTurnCommand;
import shared.model.map.Map;
import shared.model.map.VertexObject;

/**
 * Used for placing road and settlement during the first round of the game
 * Created by Alise on 10/8/2016.
 */
public class FirstRoundMapState extends MapState {

    public FirstRoundMapState(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initFromModel(Map updatedMap) {
        super.initFromModel(updatedMap);

        if(ClientUser.getInstance().getIndex() == 0) {
            if(Client.getInstance().getStartGame() &&
                    Client.getInstance().getGameState().equals(State.FIRSTROUND)
                    && Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn() == ClientUser.getInstance().getIndex()
                    && getFirstVertexLocation() == null) {
                startGame();
            }
        }
        else if(Client.getInstance().getGameState().equals(State.FIRSTROUND)
                && Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn() == ClientUser.getInstance().getIndex()
                && getFirstVertexLocation() == null) {
            startGame();
        }
    }

    public void startGame() {
            startMove(PieceType.SETTLEMENT, true, true);

    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = mapController.clientModel.getClientPlayer().getColor();
        mapController.getView().startDrop(pieceType, color, false);
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return mapController.clientModel.canPlaceSetUpRoad(edgeLoc, super.getFirstVertexLocation());
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        //return true;
       int currentPlayerId = mapController.clientModel.getClientPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceSetUpSettlement(currentPlayerId, vertLoc);
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        //This should send it to the server
        int currentPlayerIndex = mapController.clientModel.getClientPlayer().getPlayerIndex();
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, currentPlayerIndex, true);

        ClientFacade.getInstance().buildRoad(buildRoadCommand);
        FinishTurnCommand finishTurnCommand = new FinishTurnCommand(currentPlayerIndex);
        ClientFacade.getInstance().finishTurn(finishTurnCommand);
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        super.setFirstVertexLocation(vertLoc);
        //super.placeSettlement(vertLoc);

        int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.SETTLEMENT);

        BuildSettlementCommand buildSettlementCommand = new BuildSettlementCommand(vertObj, true);
        ClientFacade.getInstance().buildSettlement(buildSettlementCommand);


        startMove(PieceType.ROAD, true, true);

    }
}
