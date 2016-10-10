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

import java.util.Observable;

//Game manger should be singleton that extends observable.
//Game manager holds a list of GameModels.
//Game Model holds all the information for a game, so pretty much everything being created in the JSON Translator.
//Some how, (need to clarify this), by having GameManager extend observable, all of its subcontrollers will then extend observable.

/*
 this.addObserver(map);
//JUST ALWAYS DO THESE TWO in this order:
setChanged(); //What does this do?
notifyObservers(); You could pass in the arg, or to make life easier, just pass in nothing, and have what ever is being updated go straight to the singleton.
*/

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

    private IRobView robView;

    //TODO: Erase this later, should connect to actual game model map.
   // private shared.model.map.Map catanMap = new shared.model.map.Map(false, false, false);

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
        System.out.println("Map Controller: initFromModel");

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

        //Place the ports
        for (HexLocation key : updatedMap.getPorts().keySet()) {
            Port currentPort = updatedMap.getPorts().get(key);
            getView().addPort(new EdgeLocation(currentPort.getLocation(), currentPort.getEdgeDirection()), currentPort.getResource());
        }

        EdgeLocation edgeLoc = new EdgeLocation(new HexLocation(1, 1), EdgeDirection.North);
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, 0);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);

        //Place the Roads
        for (EdgeLocation edgeLocation : updatedMap.getEdgeObjects().keySet()) {
            if (updatedMap.getEdgeObjects().get(edgeLocation).getOwner() != -1) {
                getView().placeRoad(edgeLocation, CatanColor.ORANGE);
            }
        }

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

    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        //return catanMap.buildRoadManager.canPlace(0, edgeLoc);
        return true;
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        //return catanMap.buildSettlementManager.canPlace(0, vertLoc);
        return true;
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        //return catanMap.buildCityManager.canPlaceCity(0, vertLoc);
        return true;
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        return true;
        //return catanMap.canPlaceRobber(hexLoc);
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
        ClientModel clientModel = (ClientModel) o;
        Map map = clientModel.getMap();
        initFromModel(map);
    }


}

