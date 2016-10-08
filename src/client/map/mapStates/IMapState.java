package client.map.mapStates;

import client.data.RobPlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.map.Hex;
import shared.model.map.Port;

import java.util.Observable;

/**
 * Created by Alise on 10/8/2016.
 */
public interface IMapState {
    //Todo Note: I don't know if all these functions will be needed or not; add or delete as neededan

    public boolean canPlaceRoad(EdgeLocation edgeLoc);

    public boolean canPlaceSettlement(VertexLocation vertLoc) ;

    public boolean canPlaceCity(VertexLocation vertLoc);

    public boolean canPlaceRobber(HexLocation hexLoc);

    public void placeRoad(EdgeLocation edgeLoc);

    public void placeSettlement(VertexLocation vertLoc);

    public void placeCity(VertexLocation vertLoc);

    public void placeRobber(HexLocation hexLoc);

    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected);

    public void cancelMove();

    public void playSoldierCard();

    public void playRoadBuildingCard();

    public void robPlayer(RobPlayerInfo victim);
}
