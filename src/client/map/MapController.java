package client.map;

import client.ClientFacade;
import client.base.Controller;
import client.data.RobPlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.BuildRoadCommand;
import shared.model.map.Hex;
import shared.model.map.Map;
import shared.model.map.Port;
import shared.model.map.VertexObject;

import java.util.Observable;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

    private IRobView robView;

    private ClientModel clientModel;

    public MapController(IMapView view, IRobView robView) {
        super(view);
        System.out.println("Map Controller Constructor");
        setRobView(robView);
        
    }

    public IMapView getView() {
        return (IMapView) super.getView();
    }

    private IRobView getRobView() {
        return robView;
    }

    private void setRobView(IRobView robView) {
        this.robView = robView;
    }

    protected void initFromModel(Map updatedMap) {
        //Place the hexes, numbers, and robber.
        for (HexLocation key : updatedMap.getHexes().keySet()) {

            Hex currentHex = updatedMap.getHexes().get(key);
            getView().addHex(currentHex.getLocation(), currentHex.getResource());
            if (currentHex.getResource() != HexType.WATER) {
                if (currentHex.getResource() != HexType.DESERT) {
                    getView().addNumber(currentHex.getLocation(), currentHex.getNumber());
                } else {
                    getView().placeRobber(currentHex.getLocation());
                }
            }
        }

        //Place Water Hexes
        drawWaterHexes();

        //Place the ports
        for (HexLocation key : updatedMap.getPorts().keySet()) {
            Port currentPort = updatedMap.getPorts().get(key);
            getView().addPort(new EdgeLocation(currentPort.getLocation(), currentPort.getEdgeDirection()), currentPort.getResource());
        }

        //Place the Roads
        for (EdgeLocation edgeLocation : updatedMap.getEdgeObjects().keySet()) {
            if (updatedMap.getEdgeObjects().get(edgeLocation).getOwner() != -1) {
                getView().placeRoad(edgeLocation, CatanColor.ORANGE);
            }
        }

        //Place the Settlement
        for (VertexLocation vertexLocation : updatedMap.getVertexObjects().keySet()) {
            if (updatedMap.getVertexObjects().get(vertexLocation).getOwner() != -1) {
                getView().placeSettlement(vertexLocation, CatanColor.ORANGE);
            }
        }

/*
        EdgeLocation edgeLoc = new EdgeLocation(new HexLocation(1, 1), EdgeDirection.North);
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, 0);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);
*/
/*
        //TEMPORARY: Start out with a road
        VertexLocation verLoc = new VertexLocation(new HexLocation(1, 1), VertexDirection.NorthEast);
        placeSettlement(verLoc);
        EdgeLocation edgeLoc = new EdgeLocation(new HexLocation(1, 1), EdgeDirection.North);
        placeRoad(edgeLoc);

        VertexLocation verLocBlue = new VertexLocation(new HexLocation(-1, -1), VertexDirection.NorthEast);
        placeSettlement(verLocBlue);
        EdgeLocation edgeLocBlue = new EdgeLocation(new HexLocation(-1, -1), EdgeDirection.North);
        placeRoad(edgeLocBlue);
        */
    }

    private void drawWaterHexes(){
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

    private void createWaterHex(int x, int y) {
        getView().addHex(new HexLocation(x, y), HexType.WATER);
    }

    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        //return clientModel.canPlaceRoad(0, edgeLoc);
        return true;
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        //return clientModel.canPlaceSettlement(0, vertLoc);
        return true;
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        //return clientModel.canPlaceCity(0, vertLoc);
        return true;
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        //return clientModel.canPlaceRobber(hexLoc);
        return true;
    }

    public void placeRoad(EdgeLocation edgeLoc) {
        //This should send it to the server
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, 0);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);

        //ClientModel tempClientModel = new ClientModel(1);
        //tempClientModel.purchaseAndPlaceRoad(0, edgeLoc);


        //catanMap.buildRoadManager.placeRoad(0, edgeLoc);  //TODO: Should use the update stuff...
        //getView().placeRoad(edgeLoc, CatanColor.ORANGE);
    }

    public void placeSettlement(VertexLocation vertLoc) {
        //catanMap.buildSettlementManager.placeSettlement(0, vertLoc);  //TODO: Should use the update stuff...
        getView().placeSettlement(vertLoc, CatanColor.ORANGE);
        getView().placeCity(vertLoc, CatanColor.ORANGE);
        getView().placeSettlement(vertLoc, CatanColor.ORANGE);
    }

    public void placeCity(VertexLocation vertLoc) {
        //catanMap.buildCityManager.placeCity(0, vertLoc);  //TODO: Should use the update stuff...
        getView().placeCity(vertLoc, CatanColor.ORANGE);
    }

    public void placeRobber(HexLocation hexLoc) {
        //catanMap.placeRobber(hexLoc);
        getView().placeRobber(hexLoc);
        getRobView().showModal();
    }

    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        getView().startDrop(pieceType, CatanColor.ORANGE, true);
    }

    public void cancelMove() {

    }

    public void playSoldierCard() {
        System.out.println("MAP: PLAYER SOLDIER CARD!");
        //getRobView().showModal(); //This gets the counters for how many cards possible players have.
        getView().startDrop(PieceType.ROBBER, CatanColor.WHITE, true); //3rd variable is boolean, cancel allowed
    }

    public void playRoadBuildingCard() {
        System.out.println("MAP: PLAY ROAD BUILDING CARD");
        getView().startDrop(PieceType.ROAD, CatanColor.ORANGE, true);
        getView().startDrop(PieceType.ROAD, CatanColor.ORANGE, true);
    }

    public void robPlayer(RobPlayerInfo victim) {

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Map Controller: Update");
        clientModel = (ClientModel) o;
        initFromModel(clientModel.getMap());
    }


}

