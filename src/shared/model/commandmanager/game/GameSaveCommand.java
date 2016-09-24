package shared.model.commandmanager.game;
import shared.model.commandmanager.BaseCommand;

import java.io.File;
/**
 * Created by Alise on 9/18/2016.
 */
public class GameSaveCommand implements BaseCommand {
    /**
     * id of game to save
     */
    private int gameID;

    /**
     * name of file to create
     */
    private String fileName;

    /**
     * CreatesGameSaveCommand to send to the client.ClientFacade. Sets data members.
     * @param gameID
     * @param fileName
     */
    public GameSaveCommand(int gameID, String fileName){
        this.gameID = gameID;
        this.fileName = fileName;
    }

    /**
     * Tells server to save given game with to a file with the given name
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
