package client.model.commandmanager.moves;

import client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class RollDiceCommand extends BaseCommand {
    /**
     * number rolled
     */
    private int number;
    /**
     * Creates RollDiceCommand to send to client.ClientFacade. Sets data members
     * @param number
     */
    public RollDiceCommand(int number){

    }

    /**
     * Tells server the dice were rolled and to distribute/require discarding of resources
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
