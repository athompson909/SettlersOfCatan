package shared.model.commandmanager.moves;

import org.json.JSONObject;
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
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "Year_of_Plenty";

    /**
     * Creates empty Command
     */
    public PlayYearOfPlentyCommand() {
    }

    /**
     * Creates PlayYearOfPlentyCommand to send to client.ClientFacade. Sets data members
     * @param playerIndex
     * @param resource1
     * @param resource2
     */
    public PlayYearOfPlentyCommand(int playerIndex, ResourceType resource1, ResourceType resource2){
        this.playerIndex = playerIndex;
        this.resource1 = resource1;
        this.resource2 = resource2;
    }

    /**
     *Tells server to give player these two resources from the bank
     */
    @Override
    public JSONObject serverExec(BaseCommand command){

        return null;
    }

//Getters
    public String getType() {
        return type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public ResourceType getResource1() {
        return resource1;
    }

    public ResourceType getResource2() {
        return resource2;
    }
}
