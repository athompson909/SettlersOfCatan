package client.map.mapStates;

import client.Client;
import client.ClientFacade;
import client.data.RobPlayerInfo;
import client.map.IMapController;
import client.map.MapController;
import client.map.RobView;
import client.resources.ResourceBarElement;
import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.ClientModel;
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
     public MapController mapController;

    private VertexLocation firstVertexLocation;
    private VertexLocation secondVertexLocation;

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
        int currentPlayerIndex = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceRoad(currentPlayerIndex, edgeLoc);
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        int currentPlayerIndex = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceSettlement(currentPlayerIndex, vertLoc);
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        int currentPlayerIndex = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        return mapController.clientModel.canPlaceCity(currentPlayerIndex, vertLoc);
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        return mapController.clientModel.canPlaceRobber(hexLoc);
    }

    public void placeRoad(EdgeLocation edgeLoc) {
        int currentPlayerIndex = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, currentPlayerIndex, false);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);
    }

    public void placeSettlement(VertexLocation vertLoc) {
        int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.SETTLEMENT);

        BuildSettlementCommand buildSettlementCommand = new BuildSettlementCommand(vertObj, false);
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

    private HexLocation robberHex;

    public void placeRobber(HexLocation hexLoc) {
        System.out.println("MAP: PLACEROBBER");
        robberHex = hexLoc;

        RobPlayerInfo[] victims = mapController.clientModel.calculateRobPlayerInfo(hexLoc);

        if(victims.length > 0){
            mapController.getRobView().setPlayers(victims);
            mapController.getRobView().showModal(); //This shows the counters for how many cards possible players have.
        } else {
            //Don't rob anyone, so send a command with -1.
            int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
            PlaySoldierCommand playSoldierCommand = new PlaySoldierCommand(currentPlayerId, robberHex, -1);
            ClientFacade.getInstance().playSoldier(playSoldierCommand);
        }
    }

    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(pieceType, color, true);
    }

    public void cancelMove() {
        System.out.println("MAPSTATE: CANCELMOVE");
        //initFromModel(mapController.clientModel.getMap());
    }

    public void playSoldierCard() {
        System.out.println("MAP: PLAYER SOLDIER CARD!");
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(PieceType.ROBBER, color, true); //3rd variable is boolean, cancel allowed
    }

    public void playRoadBuildingCard() {
        System.out.println("MAP: PLAY ROAD BUILDING CARD.");
        mapController.setMapStateToRoadBuilding();

        startMove(PieceType.ROAD, true, false);

        startMove(PieceType.ROAD, true, false);
    }

    public void robPlayer(RobPlayerInfo victim) {
        System.out.println("MAP: ROB PLAYER!");

        //Why does the ROb PLayer command need a hex and victim? Where do I get the hex from?
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        PlaySoldierCommand playSoldierCommand = new PlaySoldierCommand(currentPlayerId, robberHex, victim.getPlayerIndex());
        ClientFacade.getInstance().playSoldier(playSoldierCommand);

    }

    public VertexLocation getFirstVertexLocation() {
        return firstVertexLocation;
    }

    public void setFirstVertexLocation(VertexLocation firstVertexLocation) {
        this.firstVertexLocation = firstVertexLocation;
    }

    public VertexLocation getSecondVertexLocation() {
        return secondVertexLocation;
    }

    public void setSecondVertexLocation(VertexLocation secondVertexLocation) {
        this.secondVertexLocation = secondVertexLocation;
    }


}
