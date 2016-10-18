package client.turntracker;

import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class RollingState extends TurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Roll Dice", false);
    }

    @Override
    public State toEnum() {
        return State.ROLLING;
    }
}
