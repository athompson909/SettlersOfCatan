package client.turntracker;

import client.ClientUser;
import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/11/2016.
 */
public abstract class IState {
    public IState update(TurnTracker tracker){
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
}
