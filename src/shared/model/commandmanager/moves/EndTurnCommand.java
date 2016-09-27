package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;

/**
 * EndTurn doesn't have any API documentation or an entry on the swagger page, so I don't think it's a real command
 *  - Sierra
 *
 * Created by Alise on 9/18/2016.
 */
public class EndTurnCommand implements BaseCommand {

    /**
     * index of player finishing turn
     */
    int playerIndex;

    /**
     * Creates EndTurnCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * sets data member
     *
     * @param index
     */
    public EndTurnCommand(int index){
        this.playerIndex = index;
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
