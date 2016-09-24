package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.definitions.ResourceType;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayYearOfPlentyCommand extends BaseCommand {
    /**
     * int 0-3 of player using card
     */
    private int playerIndex;

    /**
     * first resource player wants
     */
    private ResourceType resource1;

    /**
     * second resource player wants
     */
    private ResourceType resource2;

    /**
     * Creates PlayYearOfPlentyCommand to send to client.ClientFacade. Sets data members
     * @param playerIndex
     * @param resource1
     * @param resource2
     */
    public PlayYearOfPlentyCommand(int playerIndex, ResourceType resource1, ResourceType resource2){

    }

    /**
     *Tells server to give player these two resources from the bank
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
