package shared.model.commandmanager.game;

import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameCreateCommand extends BaseCommand {
    /**
     * name of game
     */
    private String name;

    /**
     * true if client wants tiles randomly placed, false if they want the default
     */
    private boolean randomTiles;

    /**
     * true if client wants numbers randomly placed, false if they want the default
     */
    private boolean randomNumbers;

    /**
     * true if client wants ports randomly placed, false if they want the default
     */
    private boolean randomPorts;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final transient String type = "createGame";

    /**
     * Creates GameCreateCommand to pass to the client.ClientFacade. Sets data members.
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     */
    public GameCreateCommand(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts ){
        this.name = name;
        this.randomTiles = randomTiles;
        this.randomNumbers = randomNumbers;
        this.randomPorts = randomPorts;
    }

    /**
     * Tells the server to create new game
     */
    @Override
    public JSONObject serverExec(BaseCommand command){

        return null;
    }

    //Getters
    public String getName() {
        return name;
    }

    public boolean isRandomTiles() {
        return randomTiles;
    }

    public boolean isRandomNumbers() {
        return randomNumbers;
    }

    public boolean isRandomPorts() {
        return randomPorts;
    }

    public String getType() {
        return type;
    }
}
