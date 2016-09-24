package shared.model.commandmanager;

/**
 * Created by Mitchell on 9/15/2016.
 */
public interface BaseCommand {
    /**
     * Kicks off server Execution
     */
    public void serverExec(BaseCommand command);
}
