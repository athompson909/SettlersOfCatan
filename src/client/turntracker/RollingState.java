package client.turntracker;

import shared.model.turntracker.TurnTracker;

/**
 * Created by Alise on 10/14/2016.
 */
public class RollingState extends IState {

    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Roll Dice", false);
    }
}
