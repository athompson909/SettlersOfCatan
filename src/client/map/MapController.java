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
import shared.model.map.*;
import client.map.mapStates.*;

import java.util.Observable;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

    private MapState mapState;

    private IRobView robView;

    public ClientModel clientModel;

    private FirstRoundMapState firstRoundMapState = new FirstRoundMapState(this);
    private SecondRoundMapState secondRoundMapState = new SecondRoundMapState(this);

    public MapController(IMapView view, IRobView robView) {
        super(view);
        System.out.println("Map Controller Constructor");
        setRobView(robView);
        //mapState = new FirstRoundMapState(this);
    }

    public IMapView getView() {
        return (IMapView) super.getView();
    }

    public IRobView getRobView() {
        return robView;
    }

    private void setRobView(IRobView robView) {
        this.robView = robView;
    }

    protected void initFromModel(Map updatedMap) {
        System.out.println("MAP CONTROLLER INIT FROM MODEL **************");
    }

    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return mapState.canPlaceRoad(edgeLoc);
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        return mapState.canPlaceSettlement(vertLoc);
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        return mapState.canPlaceCity(vertLoc);
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        return mapState.canPlaceRobber(hexLoc);
    }

    public void placeRoad(EdgeLocation edgeLoc) {
        mapState.placeRoad(edgeLoc);
    }

    public void placeSettlement(VertexLocation vertLoc) {
        mapState.placeSettlement(vertLoc);
    }

    public void placeCity(VertexLocation vertLoc) {
        mapState.placeCity(vertLoc);
    }

    public void placeRobber(HexLocation hexLoc) {
        mapState.placeRobber(hexLoc);
    }

    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        CatanColor color = clientModel.getCurrentPlayer().getColor();
        getView().startDrop(pieceType, color, true);
    }

    public void cancelMove() {
        mapState.cancelMove();
    }

    public void playSoldierCard() {
        mapState.playSoldierCard();
    }

    public void playRoadBuildingCard() {
        mapState.playRoadBuildingCard();
    }

    public void robPlayer(RobPlayerInfo victim) {
        mapState.robPlayer(victim);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Map Controller: Update");
        clientModel = (ClientModel) o;
        setState(Client.getInstance().getGameState());
        mapState.initFromModel(clientModel.getMap());
    }

    public void setState(State gameState) {
        if (gameState.equals(State.FIRSTROUND)) {
            mapState = firstRoundMapState;
        } else if (gameState.equals(State.SECONDROUND)) {
            mapState = secondRoundMapState;
        } else if (gameState.equals(State.DISCARDING)) {
            mapState = new DiscardingMapState(this);
        } else if (gameState.equals(State.PLAYING)) {
            mapState = new PlayingMapState(this);
        } else if (gameState.equals(State.WAITING)) {
            mapState = new WaitingMapState(this);
        } else if (gameState.equals(State.ROBBING)) {
            mapState = new RobbingMapState(this);
        } else if (gameState.equals(State.ROLLING)) {
            mapState = new RollingMapState(this);
        }
    }

    public void setMapStateToRoadBuilding(){
        mapState = new RoadBuildingMapState(this);
    }





}

