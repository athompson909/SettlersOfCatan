package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IGameDAO {

    /**
     * Modifies an already existing game.
     * @param
     */
    void writeGame(int gameID, String modelJSON, String gameInfoJSON);

    /**
     * Adds a new game.
     * @param
     */
    void writeNewGame(int gameID, String modelJSON, String gameInfoJSON);

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     * @param gameID the ID of the game where this command was executed
     */
    void writeCommand(JSONObject commandJSON, int gameID);

    /**
     * Reads all the games created.
     * @return a JSONArray with all the created games.
     */
    JSONArray readAllGames();

    /**
     * Reads all the comand files.
     * @return a JSONArray with each entry another JSONArray holding all the commands for one game.
     */
    JSONArray readAllCommands();

    /**
     * Clears all commands from the file/database for specified game.
     * @param gameID
     */
    void clearCommands(int gameID);
}
