package shared.model.commandmanager.game;
import shared.model.commandmanager.BaseCommand;
import shared.definitions.CatanColor;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameJoinCommand extends BaseCommand {
    /**
     * id of game player wants to join
     */
    private int gameID;

    /**
     * color of player
     */
    private CatanColor color;

    /**
     * Creates GameJoinCommand to send to the client.ClientFacade
     * @param gameID
     * @param color
     */
    public GameJoinCommand(int gameID, CatanColor color){

    }

    /**
     * Tells server to add user as new player of game with given id
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
