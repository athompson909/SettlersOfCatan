package Client.model.commandmanager;

import Client.model.commandmanager.moves.RobPlayerCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class CommandManager {
    /**
     * Tracks commands of the Game - can be stored to save the state of the Game
     */
    List <BaseCommand> baseCommands = new ArrayList<BaseCommand>();

    /**
     * Sends the most recent command to the ClientFacade to be translated and executed
     */
    public void clientExec(BaseCommand command){

    }

    /**
     * Removes the last Command from the list
     */
    public void clientUndo(){

    }

    /**
     * Executes all the commands again
     * @pre Used after Reset
     * @post
     */
    private void serverExec(BaseCommand command) {

    }
}
