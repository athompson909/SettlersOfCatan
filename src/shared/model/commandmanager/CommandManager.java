package shared.model.commandmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandManager contains a list of all the BaseCommand objects that have been executed,
 * as well as functions to send the commands to the client.ClientFacade to be executed by the server,
 * and to undo the most recent command by the player.
 *
 * Created by Mitchell on 9/15/2016.
 */
public class CommandManager {
    /**
     * Tracks commands of the Game - can be stored to save the state of the Game
     */
    List <BaseCommand> executedCommands = new ArrayList<BaseCommand>();


    public CommandManager() {

    }

    /**
     * Removes the last Command from the list
     */
    public void clientUndo(){
        if(!executedCommands.isEmpty()){
            executedCommands.remove(executedCommands.size() - 1);
        }
    }

    /*
    Commands that are recorded in the GameHistory:
    BuildRoad
    BuildSettlement
    BuildCity
    RollDice
    EndTurn
    Robber (moving and robbing another player )
     */


    /**
     * Executes all the commands again
     * @pre Used after Reset
     * @post
     */
    private void serverExec(BaseCommand command) {

    }

    /**
     * Adds the command object to the list of commands that have been executed so far
     * [Sierra] I don't think we actually need to use this, just get the history/log from the model each time.
     * @param command
     */
    public void addCommandtoList(BaseCommand command){
        executedCommands.add(command);
        System.out.println("CommandManager: new cmdlist size= " + executedCommands.size());
    }
}
