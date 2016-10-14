package client.turntracker;

import client.ClientUser;
import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/14/2016.
 */
public class DiscardState extends IState {

    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Discard Cards", false);
    }
}
