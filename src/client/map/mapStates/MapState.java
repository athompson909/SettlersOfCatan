package client.map.mapStates;

import client.Client;
import client.ClientFacade;
import client.data.RobPlayerInfo;
import client.map.IMapController;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.Hex;
import shared.model.map.Map;
import shared.model.map.Port;
import shared.model.map.VertexObject;

import java.util.Observable;

/**
 * Created by Alise on 10/8/2016.
 */
public abstract class MapState  {
   //Todo Note: I don't know if all these functions will be needed or not; add or delete as neededan

    public MapController mapController;

    public MapState(MapController mapController){
        this.mapController = mapController;
    }

    public void initFromModel(Map updatedMap) {
        //Place the hexes, numbers, and robber.
        for (HexLocation key : updatedMap.getHexes().keySet()) {

            Hex currentHex = updatedMap.getHexes().get(key);
            mapController.getView().addHex(currentHex.getLocation(), currentHex.getResource());
            if (currentHex.getResource() != HexType.WATER) {
                if (currentHex.getResource() != HexType.DESERT) {
                    mapController.getView().addNumber(currentHex.getLocation(), currentHex.getNumber());
                }
            }
        }

        //Place Water Hexes
        drawWaterHexes();

        //Place the ports
        for (HexLocation key : updatedMap.getPorts().keySet()) {
            Port currentPort = updatedMap.getPorts().get(key);
            mapController.getView().addPort(new EdgeLocation(currentPort.getLocation(), currentPort.getEdgeDirection()), currentPort.getResource());
        }

        //Place the Roads
        for (EdgeLocation edgeLocation : updatedMap.getEdgeObjects().keySet()) {
            int owner = updatedMap.getEdgeObjects().get(edgeLocation).getOwner();
            mapController.getView().placeRoad(edgeLocation,  mapController.clientModel.getPlayers()[owner].getColor());
        }

        //Place Vertex Objects
        for (VertexLocation vertexLocation : updatedMap.getVertexObjects().keySet()) {
            VertexObject vertexObject = updatedMap.getVertexObjects().get(vertexLocation);
            CatanColor color = mapController.clientModel.getPlayers()[vertexObject.getOwner()].getColor()  ;
            if (vertexObject.getPieceType().equals(PieceType.SETTLEMENT)) {
                mapController.getView().placeSettlement(vertexLocation, color);
            } else {
                mapController.getView().placeCity(vertexLocation, color);
            }
        }


        mapController.getView().placeRobber(updatedMap.getRobber().getCurrentHexlocation());
    }

    public void drawWaterHexes(){
        createWaterHex(-3, 0);
        createWaterHex(-3, 1);
        createWaterHex(-3, 2);
        createWaterHex(-3, 3);
        createWaterHex(-2, 3);
        createWaterHex(-1, 3);
        createWaterHex(0, 3);
        createWaterHex(1, 2);
        createWaterHex(2, 1);
        createWaterHex(3, 0);
        createWaterHex(3, -1);
        createWaterHex(3, -2);
        createWaterHex(3, -3);
        createWaterHex(2, -3);
        createWaterHex(1, -3);
        createWaterHex(0, -3);
        createWaterHex(-1, -2);
        createWaterHex(-2, -1);
    }

    public void createWaterHex(int x, int y) {
        mapController.getView().addHex(new HexLocation(x, y), HexType.WATER);
    }


    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceRoad(currentPlayerId, edgeLoc);
       // return true;
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceSettlement(currentPlayerId, vertLoc);
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceCity(currentPlayerId, vertLoc);
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        return mapController.clientModel.canPlaceRobber(hexLoc);
    }

    public void placeRoad(EdgeLocation edgeLoc) {
        //This should send it to the server
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, currentPlayerId);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);

        FinishTurnCommand finishTurnCommand = new FinishTurnCommand(currentPlayerId);
        ClientFacade.getInstance().finishTurn(finishTurnCommand);
    }

    public void placeSettlement(VertexLocation vertLoc) {
        int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.SETTLEMENT);

        BuildSettlementCommand buildSettlementCommand = new BuildSettlementCommand(vertObj);
        ClientFacade.getInstance().buildSettlement(buildSettlementCommand);
    }

    public void placeCity(VertexLocation vertLoc) {
        int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.CITY);

        BuildCityCommand buildCityCommand = new BuildCityCommand(vertObj);
        ClientFacade.getInstance().buildCity(buildCityCommand);
    }

    public void placeRobber(HexLocation hexLoc) {
        System.out.println("MAP: PLACEROBBER");
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        //TODO: Robbing should not be hard coded with the 1...
        RobPlayerCommand robPlayerCommand = new RobPlayerCommand(currentPlayerId, hexLoc, 1);
        ClientFacade.getInstance().robPlayer(robPlayerCommand);
    }

    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(pieceType, color, true);
    }

    public void cancelMove() {

    }

    public void playSoldierCard() {
        System.out.println("MAP: PLAYER SOLDIER CARD!");
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        //getRobView().showModal(); //This gets the counters for how many cards possible players have.
        mapController.getView().startDrop(PieceType.ROBBER, color, true); //3rd variable is boolean, cancel allowed
    }

    public void playRoadBuildingCard() {
        System.out.println("MAP: PLAY ROAD BUILDING CARD.");

        //THIS IS TEMPORARY CODE TO MAKE PLAYER START WITH A ROAD
        EdgeLocation temp = new EdgeLocation(new HexLocation(0,2),EdgeDirection.North);

        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(PieceType.ROAD, color, true);
        mapController.getView().startDrop(PieceType.ROAD, color, true);

        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(temp, currentPlayerId);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);


    }

    public void robPlayer(RobPlayerInfo victim) {

    }

}
