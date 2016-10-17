package client.turntracker;

import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class FirstRoundState extends ITurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Finish Turn", true);
    }

    @Override
    public State toEnum() {
        return State.FIRSTROUND;
    }
}
