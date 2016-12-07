package shared.model.commandmanager;

import server.PersistenceManager;

import java.util.ArrayList;
import java.util.HashMap;
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

    //TEST
    private HashMap<Integer, BaseCommand> cmdsToReExecute = new HashMap<>();


    private static int commandLimit = 10;

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
     * Executes all the commands again WITHOUT talking to the PersistenceManager.
     * used to bring all the clientModels up to speed based on the extra commands
     * after all the games are saved to the GamesManager (readAllGames() is done).
     *
     *
     * @pre Used after Reload
     * @post model is up to date
     */
    public void reExecuteCommands(int gameID) {
        for(int i = 0; i < executedCommands.size(); i++){
            boolean reExecWorked = executedCommands.get(i).reExecute(gameID);
            if (!reExecWorked) {
                String failedCmdType = executedCommands.get(i).getClass().toString();
                    failedCmdType = failedCmdType.substring(40, failedCmdType.length());
                    System.out.println(">CMDMGR: reExecuteCmds(): cmd reExec failed: " + failedCmdType);
            }
        }
    }

    /**
     * Adds the command object to the list of commands that have been executed so far
     * @param command
     */
    public void addCommandtoList(BaseCommand command){
        executedCommands.add(command);
        System.out.println("CommandManager: new cmdlist size= " + executedCommands.size());
        //see if we reached the limit
        int gameID = command.getGameId();
     //   int gameID = executedCommands.get(0).getGameId();
        if (executedCommands.size() >= commandLimit){
            //rewrite model
            PersistenceManager.getInstance().writeGame(gameID);
            //clear commands
            PersistenceManager.getInstance().clearCommands(gameID);
            executedCommands.clear();
        }else {
            //save command for persistence
            PersistenceManager.getInstance().writeCommand(command, gameID);
        }
    }

    public static void setCommandLimit(int limit){
        commandLimit = limit;
    }



    //used for loading from files
    public List<BaseCommand> getExecutedCommands() {
        return executedCommands;
    }

    public void setExecutedCommands(List<BaseCommand> executedCommands) {
        this.executedCommands = executedCommands;
    }


//    //TEST
//    public HashMap<Integer, BaseCommand> getCmdsToReExecute() {
//        return cmdsToReExecute;
//    }
//
//    public void setCmdsToReExecute(HashMap<Integer, BaseCommand> cmdsToReExecute) {
//        this.cmdsToReExecute = cmdsToReExecute;
//    }
}
