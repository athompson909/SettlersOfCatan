package shared.model.turntracker;

import client.ClientUser;
import client.turntracker.ITurnTrackerView;
import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public abstract class TurnTrackerState {
     public TurnTrackerState update(TurnTracker tracker){
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
    }

    public abstract void updateStateButton(ITurnTrackerView view);
    public abstract State toEnum();
}
