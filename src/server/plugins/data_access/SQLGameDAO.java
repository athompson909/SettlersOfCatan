package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLGameDAO implements IGameDAO {

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    @Override
    public void writeGame(JSONArray gameJSON) {

    }

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    @Override
    public void writeNewGame(JSONArray gameJSON) {

    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    @Override
    public void writeCommand(JSONObject commandJSON) {

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
