package shared.model.turntracker;

import client.turntracker.ITurnTrackerView;
import shared.definitions.State;

/**
 * Created by Alise on 10/17/2016.
 */
public class SecondRoundState extends TurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        view.updateGameState("Finish Turn", true);
    }

    @Override
    public State toEnum() {
        return State.SECONDROUND;
    }
}
