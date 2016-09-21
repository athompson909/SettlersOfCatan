package client.model.commandmanager.game;
import client.model.commandmanager.BaseCommand;
import client.model.player.PlayerColor;

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
    private PlayerColor color;

    /**
     * Creates GameJoinCommand to send to the client.ClientFacade
     * @param gameID
     * @param color
     */
    public GameJoinCommand(int gameID, PlayerColor color){

    }

    /**
     * Tells server to add user as new player of game with given id
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}