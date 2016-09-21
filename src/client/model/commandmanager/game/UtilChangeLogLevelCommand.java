package client.model.commandmanager.game;
import client.model.LoggingLevel;
import client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class UtilChangeLogLevelCommand extends BaseCommand {
    /**
     * log level: SEVERE, WARNING, INFO, CONFIG, FINE, FINER, or FINEST
     */
    private LoggingLevel loggingLevel;

    /**
     * Creates UtilChangeLogLevelCommand to send to the client.ClientFacade. Sets data member.
     * @param loggingLevel
     */
    public UtilChangeLogLevelCommand(LoggingLevel loggingLevel){

    }

    /**
     * Sets the server's logging level
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
