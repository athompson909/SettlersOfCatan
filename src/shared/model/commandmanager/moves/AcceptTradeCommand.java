package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class AcceptTradeCommand extends BaseCommand {
//Data Members
    /**
     * Index of player who is being offered a trade
     */
    int playerIndex;
    /**
     * true if player wants to accept trade
     */
    boolean willAccept;

    /**
     * Creates AcceptTradeCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * Sets data members of object
     *
     * @param index
     * @param willAcc
     */
    public AcceptTradeCommand(int index, boolean willAcc){
        playerIndex = index;
        willAccept = willAcc;
    }

    /**
     * Calls all necessary model update functions
     *
     * Calls Player update methods to reflect a change of cards in hand
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {
        //do we need access to the update manager? should we pass that in as the parameter instead?

        //OR should we just do the server-side updates here so we don't bloat our update manager and so these command
        //objects aren't completely empty? - should we return something so we know whether or not it worked?
    }

    //Getters
    public int getPlayerIndex() {
        return playerIndex;
    }

    public boolean isWillAccept() {
        return willAccept;
    }
}
