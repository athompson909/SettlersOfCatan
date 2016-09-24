package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayMonumentCommand implements BaseCommand {

    /**
     * Index of player playing monument card
     */
    int playerIndex;

    /**
     * Creates PlauMonumentCard object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * sets data member
     *
     * @param index
     */
    public PlayMonumentCommand(int index){
        playerIndex = index;
    }

    /**
     * Calls all necessary model updates methods
     *
     * Calls TurnTracker update methods to reflect new victory points
     * Calls Player update methods to reflect changes in old and new devCard lists
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
