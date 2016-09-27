package shared.model.commandmanager.game;
import shared.definitions.LoggingLevel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class UtilChangeLogLevelCommand implements BaseCommand {
    /**
     * log level: SEVERE, WARNING, INFO, CONFIG, FINE, FINER, or FINEST
     */
    private LoggingLevel logLevel;

    /**
     * Creates UtilChangeLogLevelCommand to send to the client.ClientFacade. Sets data member.
     * @param logLevel
     */
    public UtilChangeLogLevelCommand(LoggingLevel logLevel){
        this.logLevel = logLevel;
    }

    /**
     * Sets the server's logging level
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }

    //Getters

    public LoggingLevel getLogLevel() {
        return logLevel;
    }
}
