package shared.model.commandmanager.game;

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
     * true if tests.client wants tiles randomly placed, false if they want the default
     */
    private boolean randomTiles;

    /**
     * true if tests.client wants numbers randomly placed, false if they want the default
     */
    private boolean randomNumbers;

    /**
     * true if tests.client wants ports randomly placed, false if they want the default
     */
    private boolean randomPorts;

    /**
     * Creates GameCreateCommand to pass to the tests.client.ClientFacade. Sets data members.
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     */
    public GameCreateCommand(String name,boolean randomTiles, boolean randomNumbers, boolean randomPorts ){

    }

    /**
     * Tells the server to create new game
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
