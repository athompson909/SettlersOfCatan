package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IGameDAO {

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    void writeGame(JSONArray gameJSON);

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    void writeNewGame(JSONArray gameJSON);

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
