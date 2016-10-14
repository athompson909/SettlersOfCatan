package client.turntracker;

import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/14/2016.
 */
public class PlayingState extends IState {

    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Finish Turn", true);
    }
}
