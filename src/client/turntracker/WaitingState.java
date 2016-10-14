package client.turntracker;

import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/14/2016.
 */
public class WaitingState extends IState {

    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Waiting for other players", false);
    }
}
