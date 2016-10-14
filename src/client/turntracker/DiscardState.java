package client.turntracker;

import client.ClientUser;
import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/14/2016.
 */
public class DiscardState extends IState {
    @Override
    public IState update(TurnTracker tracker){
        IState state = super.update(tracker);
        if(state instanceof DiscardState) {
            return state;
        }else {//state changed so reset boolean to false
            ClientUser.getInstance().setNeedToDiscard(true);
            return state;
        }
    }

    @Override
    public void updateStateButton(ITurnTrackerView view) {
        if(ClientUser.getInstance().getNeedToDiscard()){
            view.updateGameState("Discard Cards", false);
        }else {
            view.updateGameState("Waiting for Other Players", false);
        }
    }
}
