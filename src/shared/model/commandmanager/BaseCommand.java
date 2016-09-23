package shared.model.commandmanager;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class BaseCommand {

    /**
     * sends the command to the clientFacade
     */
    public void clientExec(BaseCommand command){
        //need access to the Facade - get it through the Command Manager?
    }

    /**
     * Kicks off server Execution
     */
    public void serverExec(BaseCommand command){

    }
}
