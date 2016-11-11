package shared.model.turntracker;

import client.turntracker.ITurnTrackerView;
import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class RobbingState extends TurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Robbing", false);
    }

    @Override
    public State toEnum() {
        return State.ROBBING;
    }
}
