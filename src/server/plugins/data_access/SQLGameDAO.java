package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLGameDAO implements IGameDAO {

    /**
     * Modifies an already existing game.
     * @param
     */
    @Override
    public void writeGame(int gameID, String modelJSON, String gameInfoJSON) {

    }

    /**
     * Adds a new game.
     * @param
     */
    @Override
    public void writeNewGame(int gameID, String modelJSON, String gameInfoJSON) {

    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     * @param gameID the ID of the game where this command was executed
     */
    @Override
    public void writeCommand(JSONObject commandJSON, int gameID) {

    }

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    @Override
    public JSONArray readAllGames() {
        return null;
    }
}
