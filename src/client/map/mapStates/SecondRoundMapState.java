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
import sun.security.provider.certpath.Vertex;

import java.util.HashMap;

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
        if(Client.getInstance().getGameState().equals(State.SECONDROUND)
                && Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn() == ClientUser.getInstance().getIndex()
                && getSecondVertexLocation() == null) {
        //    Client.getInstance().stopServerPoller();
            // mapController.startMove(PieceType.SETTLEMENT, true, false);
            CatanColor color = mapController.getClientModel().getClientPlayer().getColor();
            if(Client.getInstance().getClientModel().getClientPlayer().getSettlementCount() == 4) {
                mapController.getView().startDrop(PieceType.SETTLEMENT, color, false);
            } else {
                mapController.getView().startDrop(PieceType.ROAD, color, false);
            }
        }
    //    Client.getInstance().startServerPoller();
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        if(super.getSecondVertexLocation() == null){
            int playerIndex = Client.getInstance().getClientModel().getClientPlayer().getPlayerIndex();
            HashMap<VertexLocation, VertexObject> settlements = Client.getInstance().getClientModel().getMap().getVertexObjects();
            for(VertexLocation loc: settlements.keySet()) {
                if (settlements.get(loc).getOwner() == playerIndex) {
                    if(!Client.getInstance().getClientModel().doesSettlementHaveConnectedRoad(loc)){
                        super.setSecondVertexLocation(loc);
                    }
                }
            }
        }
        return mapController.clientModel.canPlaceSetUpRoad(edgeLoc, super.getSecondVertexLocation());
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
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
        super.setSecondVertexLocation(vertLoc);
        int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.SETTLEMENT);

        BuildSettlementCommand buildSettlementCommand = new BuildSettlementCommand(vertObj, true);
        ClientFacade.getInstance().buildSettlement(buildSettlementCommand);
        CatanColor color = mapController.getClientModel().getClientPlayer().getColor();
        mapController.getView().startDrop(PieceType.ROAD, color, false);
    }
}
