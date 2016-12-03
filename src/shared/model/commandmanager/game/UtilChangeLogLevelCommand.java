package shared.model.commandmanager.game;
import org.json.JSONObject;
import shared.definitions.LoggingLevel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class UtilChangeLogLevelCommand extends BaseCommand {
    /**
     * log level: SEVERE, WARNING, INFO, CONFIG, FINE, FINER, or FINEST
     */
    private LoggingLevel logLevel;

    /**
     * Creates empty Command
     */
    public UtilChangeLogLevelCommand() {
    }

    /**
     * Creates UtilChangeLogLevelCommand. Sets data member.
     * @param logLevel
     */
    public UtilChangeLogLevelCommand(LoggingLevel logLevel){
        this.logLevel = logLevel;
    }

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONBoth();
    }


    /**
     * Sets the server's logging level
     */
    @Override
    public String serverExec() {

        return null;
    }

    @Override
    public void reExecute(int gameID){}

    //Getters

    public LoggingLevel getLogLevel() {
        return logLevel;
    }
}
