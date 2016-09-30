package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;
import shared.definitions.ResourceType;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayMonopolyCommand implements BaseCommand {

    /**
     * Index of player playing Monopoly
     */
    private int playerIndex;
    /**
     * Resource player is calling monopoly on
     */
    private ResourceType resource;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "Monopoly";


    /**
     * Creates PlayMonopolyCommand object to be sent to client.ClientFacade
     * for translating into JSON
     *
     * sets data members
     *
     * @param index
     * @param res
     */
    public PlayMonopolyCommand(int index, ResourceType res){
        playerIndex = index;
        resource = res;
    }

    /**
     * Calls all necessary model update methods
     *
     * Calls Player update methods to reflect changes in new and old devCard lists
     * Calls Player update methods to reflect changes in (potentially) each player's hand
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }

//Getters
    public String getType() {
        return type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public ResourceType getResource() {
        return resource;
    }
}
