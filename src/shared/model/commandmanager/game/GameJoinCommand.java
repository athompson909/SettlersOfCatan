package shared.model.commandmanager.game;
import shared.model.commandmanager.BaseCommand;
import shared.definitions.CatanColor;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameJoinCommand implements BaseCommand {
    /**
     * id of game the player wants to join
     * Sierra renamed it from gameID to id to follow the server command schema
     */
    private int id;

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
        this.id = gameID;
        this.color = color;
    }

    /**
     * Tells server to add user as new player of game with given id
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }

    //Getters

    public int getId() {
        return id;
    }

    public CatanColor getColor() {
        return color;
    }
}
