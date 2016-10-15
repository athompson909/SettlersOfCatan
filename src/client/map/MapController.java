package client.map;

import client.Client;
import client.ClientFacade;
import client.base.Controller;
import client.data.RobPlayerInfo;
import client.main.Catan;

import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.State;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.commandmanager.CommandManager;
import shared.model.commandmanager.moves.*;
import shared.model.map.*;
import sun.security.provider.certpath.Vertex;
import client.map.mapStates.*;
import java.util.Observable;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

    private MapState mapState;

    private IRobView robView;

    public ClientModel clientModel;

    public MapController(IMapView view, IRobView robView) {
        super(view);
        System.out.println("Map Controller Constructor");
        setRobView(robView);
        //mapState = new FirstRoundMapState(this);
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
        System.out.println("MAP CONTROLLER INIT FROM MODEL **************");
        //mapState.initFromModel(updatedMap);

        /*
        //Place the hexes, numbers, and robber.
        for (HexLocation key : updatedMap.getHexes().keySet()) {

            Hex currentHex = updatedMap.getHexes().get(key);
            getView().addHex(currentHex.getLocation(), currentHex.getResource());
            if (currentHex.getResource() != HexType.WATER) {
                if (currentHex.getResource() != HexType.DESERT) {
                    getView().addNumber(currentHex.getLocation(), currentHex.getNumber());
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
            int owner = updatedMap.getEdgeObjects().get(edgeLocation).getOwner();
            getView().placeRoad(edgeLocation,  clientModel.getPlayers()[owner].getColor());
        }

        //Place Vertex Objects
        for (VertexLocation vertexLocation : updatedMap.getVertexObjects().keySet()) {
            VertexObject vertexObject = updatedMap.getVertexObjects().get(vertexLocation);
            CatanColor color = clientModel.getPlayers()[vertexObject.getOwner()].getColor()  ;
            if (vertexObject.getPieceType().equals(PieceType.SETTLEMENT)) {
                getView().placeSettlement(vertexLocation, color);
            } else {
                getView().placeCity(vertexLocation, color);
            }
        }


        getView().placeRobber(updatedMap.getRobber().getCurrentHexlocation());

        //Place the Robber


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
        return mapState.canPlaceRoad(edgeLoc);

        //int currentPlayerId = clientModel.getCurrentPlayer().getPlayerIndex();
        //return clientModel.canPlaceRoad(currentPlayerId, edgeLoc);
        //return true;
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        return mapState.canPlaceSettlement(vertLoc);

       // int currentPlayerId = clientModel.getCurrentPlayer().getPlayerIndex();
       // return clientModel.canPlaceSettlement(currentPlayerId, vertLoc);
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        return mapState.canPlaceCity(vertLoc);
       // int currentPlayerId = clientModel.getCurrentPlayer().getPlayerIndex();
       // return clientModel.canPlaceCity(currentPlayerId, vertLoc);
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        return mapState.canPlaceRobber(hexLoc);

        //return clientModel.canPlaceRobber(hexLoc);
    }

    public void placeRoad(EdgeLocation edgeLoc) {
        mapState.placeRoad(edgeLoc);
        /*
        //This should send it to the server
        int currentPlayerId = clientModel.getCurrentPlayer().getPlayerIndex();
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(edgeLoc, currentPlayerId);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);

        FinishTurnCommand finishTurnCommand = new FinishTurnCommand(currentPlayerId);
        ClientFacade.getInstance().finishTurn(finishTurnCommand);
        */
    }

    public void placeSettlement(VertexLocation vertLoc) {
        mapState.placeSettlement(vertLoc);

        /*int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.SETTLEMENT);

        BuildSettlementCommand buildSettlementCommand = new BuildSettlementCommand(vertObj);
        ClientFacade.getInstance().buildSettlement(buildSettlementCommand);*/
    }

    public void placeCity(VertexLocation vertLoc) {
        mapState.placeCity(vertLoc);
       /* int currTurn = Client.getInstance().getClientModel().getTurnTracker().getCurrentTurn();
        VertexObject vertObj = new VertexObject(vertLoc);
        vertObj.setOwner(currTurn);
        vertObj.setPieceType(PieceType.CITY);

        BuildCityCommand buildCityCommand = new BuildCityCommand(vertObj);
        ClientFacade.getInstance().buildCity(buildCityCommand);*/
    }

    public void placeRobber(HexLocation hexLoc) {
        mapState.placeRobber(hexLoc);
       /* System.out.println("MAP: PLACEROBBER");
        int currentPlayerId = clientModel.getCurrentPlayer().getPlayerIndex();
        //TODO: Robbing should not be hard coded with the 1...
        RobPlayerCommand robPlayerCommand = new RobPlayerCommand(currentPlayerId, hexLoc, 1);
        ClientFacade.getInstance().robPlayer(robPlayerCommand);*/
    }

    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = clientModel.getCurrentPlayer().getColor();
        getView().startDrop(pieceType, color, true);
    }

    public void cancelMove() {

    }

    public void playSoldierCard() {
        System.out.println("MAP: PLAYER SOLDIER CARD!");
        CatanColor color = clientModel.getCurrentPlayer().getColor();
        //getRobView().showModal(); //This gets the counters for how many cards possible players have.
        getView().startDrop(PieceType.ROBBER, color, true); //3rd variable is boolean, cancel allowed
    }

    public void playRoadBuildingCard() {
        System.out.println("MAP: PLAY ROAD BUILDING CARD.");

        //THIS IS TEMPORARY CODE TO MAKE PLAYER START WITH A ROAD
        EdgeLocation temp = new EdgeLocation(new HexLocation(0,2),EdgeDirection.North);

        CatanColor color = clientModel.getCurrentPlayer().getColor();
        getView().startDrop(PieceType.ROAD, color, true);
        getView().startDrop(PieceType.ROAD, color, true);

        int currentPlayerId = clientModel.getCurrentPlayer().getPlayerIndex();
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(temp, currentPlayerId);
        ClientFacade.getInstance().buildRoad(buildRoadCommand);


    }

    public void robPlayer(RobPlayerInfo victim) {

    }

    public void setState(State gameState) {
        if(gameState.equals(State.FIRSTROUND)){
            mapState = new FirstRoundMapState(this);
        }
        else if(gameState.equals(State.SECONDROUND)) {
            mapState = new SecondRoundMapState(this);
        }
        else if(gameState.equals(State.DISCARDING)) {
            mapState = new DiscardingMapState(this);
        }
        else if(gameState.equals(State.PLAYING)) {
            mapState = new PlayingMapState(this);
        }
        else if(gameState.equals(State.WAITING)) {
            mapState = new WaitingMapState(this);
        }
        else if(gameState.equals(State.ROBBING)) {
            mapState = new RobbingMapState(this);
        }
        else if(gameState.equals(State.ROLLING)) {
            mapState = new RollingMapState(this);
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Map Controller: Update");
        clientModel = (ClientModel) o;
        setState(Client.getInstance().getGameState());
        mapState.initFromModel(clientModel.getMap());
        //state.initFromModel(clientModel.getMap());
    }


}

