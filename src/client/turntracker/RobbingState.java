package client.turntracker;

import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class RobbingState extends ITurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Robbing", false);
    }

    @Override
    public State toEnum() {
        return State.ROBBING;
    }
}
