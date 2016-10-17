package client.turntracker;

import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class WaitingState extends TurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Waiting for other Players", false);
    }

    @Override
    public State toEnum() {
        return State.WAITING;
    }
}
