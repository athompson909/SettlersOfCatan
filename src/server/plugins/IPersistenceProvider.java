package server.plugins;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IPersistenceProvider {

    /**
     * returns singleton
     */
    static IPersistenceProvider getInstance(){
        return null;
    }

    /**
     * Modifies an already existing game.
     */
    void writeGame(int gameID, String modelJSON, String gameInfoJSON);

    /**
     * Adds a new game.
     */
    void writeNewGame(int gameID, String modelJSON, String gameInfoJSON);

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     * @param gameID the ID of the game where this command was executed
     */
    void writeCommand(JSONObject commandJSON, int gameID);

    /**
     * Adds a new user.
     * @param userJSON JSON with the new user info.
     */
    void writeUser(JSONObject userJSON);

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    JSONArray readAllUsers();

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    JSONArray readAllGames();

    /**
     * Clears all the data.
     */
    void clearAllData();

    void clearCommands(int gameID);

}
