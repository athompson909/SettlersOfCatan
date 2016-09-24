package shared.model.commandmanager.game;
import shared.model.commandmanager.BaseCommand;

import java.io.File;
/**
 * Created by Alise on 9/18/2016.
 */
public class GameLoadCommand extends BaseCommand {
    /**
     * name of file to Load
     */
    private File filename;

    /**
     * Creates GameLoadCommand to send to the client.ClientFacade. Sets fileName.
     * @param fileName
     */
    public GameLoadCommand(File fileName){

    }

    /**
     * Tells server to load given file
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}