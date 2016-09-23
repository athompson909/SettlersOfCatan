package shared.model.commandmanager;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class BaseCommand {
//should this be a class or an interface - do we need to implement all of these differently based on the type of command?
// do we need to pass the command as a parameter?
// do we need a clientExec? - we can send it to the facade in the controller
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
