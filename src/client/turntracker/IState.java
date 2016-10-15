package client.turntracker;

import client.ClientUser;
import client.map.MapController;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;
import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/11/2016.
 */
public interface IState {
   /* public IState update(TurnTracker tracker){
        String status = tracker.getStatus();
        boolean myTurn = (tracker.getCurrentTurn() == ClientUser.getInstance().getIndex());
        if(status.equals("Rolling")){
            if(myTurn){
                return new RollingState();
            }else{
                return new WaitingState();
            }
        }else if(status.equals("Playing")){
            if(myTurn){
                return new PlayingState();
            }else{
                return new WaitingState();
            }
        }else if(status.equals("Robbing")){
            if(myTurn){
                return new RobbingState();
            }else{
                return new WaitingState();
            }
        }else if(status.equals("Discarding")){
            //todo
            //did they already discard?
            //do they need to discard?
            return new DiscardState();
        }else if(status.equals("FirstRound")){
            //todo -figure out what needs to be different about first and secondRound states
            if(myTurn){
                return new FirstRoundState();
            }else{
                return new WaitingState();
            }
        }else if(status.equals("SecondRound")){
            if(myTurn){
                return new SecondRoundState();
            }else{
                return new WaitingState();
            }
        }
        return null;
    }*/


    void updateStateButton(ITurnTrackerView view);

    void initFromModel(Map updatedMap);

    boolean canPlaceRoad(EdgeLocation edgeLoc);

    boolean canPlaceSettlement(VertexLocation vertLoc) ;

    void placeRoad(EdgeLocation edgeLoc);

    void placeSettlement(VertexLocation vertLoc);

    void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected);

    void cancelMove();

}
