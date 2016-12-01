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
     */
    void writeCommand(JSONObject commandJSON);

    /**
     * Reads all the games created.
     * @return a JSONArray with all the created games.
     */
    JSONArray readAllGames();

}
