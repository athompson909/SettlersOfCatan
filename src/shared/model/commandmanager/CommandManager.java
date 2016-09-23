package shared.model.commandmanager;

import client.ClientFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandManager contains a list of all the BaseCommand objects that have been executed,
 * as well as functions to send the commands to the tests.client.ClientFacade to be executed by the server,
 * and to undo the most recent command by the player.
 *
 * Created by Mitchell on 9/15/2016.
 */
public class CommandManager {
    /**
     * Tracks commands of the Game - can be stored to save the state of the Game
     */
    List <BaseCommand> baseCommands = new ArrayList<BaseCommand>();

    ClientFacade clientFacade;

    public CommandManager(ClientFacade facade) {
        clientFacade = facade;
    }

    /**
     * Sends the most recent command to the tests.client.ClientFacade to be translated and executed
     */
    public void clientExec(BaseCommand command){
        //command.clientExec(command);
        //clientFacade.translate(command);//- create translate for the facade to use the translator?
    }

    /**
     * Removes the last Command from the list
     */
    public void clientUndo(){
        if(!baseCommands.isEmpty()){
            baseCommands.remove(baseCommands.size() - 1);
        }
    }

    /**
     * Executes all the commands again
     * @pre Used after Reset
     * @post
     */
    private void serverExec(BaseCommand command) {

    }
}
