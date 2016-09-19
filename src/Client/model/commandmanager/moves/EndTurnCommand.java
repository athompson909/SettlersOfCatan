package Client.model.commandmanager.moves;

import Client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class EndTurnCommand extends BaseCommand {

    /**
     * index of player finishing turn
     */
    int playerIndex;

    /**
     * Creates EndTurnCommand object to be sent to ClientFacade
     * for translation into JSON
     *
     * sets data member
     *
     * @param index
     */
    public EndTurnCommand(int index){

    }

    /**
     * Calls necessary model update methods to reflect changes
     *
     * Calls TurnTracker update methods to reflect end of turn
     * and cjange currentPlayer
     * Calls MessageManager update methods to reflect new Log
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
