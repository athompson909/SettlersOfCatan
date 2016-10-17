package client.turntracker;

import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class DiscardState extends ITurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        //todo - waiting state possible here
        view.updateGameState("Discard Cards", false);
    }

    @Override
    public State toEnum() {
        //todo should this possibly be waiting?
        return State.DISCARDING;
    }
}
