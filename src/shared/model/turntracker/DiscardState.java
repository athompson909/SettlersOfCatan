package shared.model.turntracker;

import client.Client;
import client.turntracker.ITurnTrackerView;
import shared.definitions.State;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceList;

/**
 * Created by Alise on 10/17/2016.
 */
public class DiscardState extends TurnTrackerState {
    @Override
    public void updateStateButton(ITurnTrackerView view) {
        //todo - waiting state possible here
        Player currentPlayer = Client.getInstance().getClientModel().getCurrentPlayer();
        ResourceList resources = currentPlayer.getPlayerResourceList();
        if(currentPlayer.hasDiscarded() || resources.getCardCount() <=7) {
            view.updateGameState("Waiting for Other Players", false);
        }else{
            view.updateGameState("Discard Cards", false);
        }
    }

    @Override
    public State toEnum() {
        //todo should this possibly be waiting?
        return State.DISCARDING;
    }
}
