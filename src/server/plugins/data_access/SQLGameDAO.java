package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.SQLPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

        try {
            Connection conn = SQLPlugin.getInstance().startTransaction();

            Statement statement = conn.createStatement();
            String sql = "UPDATE games SET model = " + modelJSON + ", gameInfo = " + gameInfoJSON + " WHERE gameID = " + gameID;
            statement.execute(sql);

            statement.close();
            SQLPlugin.getInstance().endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds a new game.
     * Question... are we no longer passing in the title separately?
     * @param
     */
    @Override
    public void writeNewGame(int gameID, String modelJSON, String gameInfoJSON) {

        try {
            Connection conn = SQLPlugin.getInstance().startTransaction();

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO games (gameID, title, model, gameInfo) " +
                    "VALUES (" + gameID + ", null," + modelJSON + "," + gameInfoJSON + ")";
            statement.execute(sql);

            statement.close();
            SQLPlugin.getInstance().endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds a new command.
     * todo: what is the format of commandJSON?
     * @param commandJSON The type of command.
     * @param gameID the ID of the game where this command was executed
     */
    @Override
    public void writeCommand(JSONObject commandJSON, int gameID) {
        try {
            Connection conn = SQLPlugin.getInstance().startTransaction();

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO commands (gameID, command) " +
                    "VALUES (" + gameID + "," + commandJSON + ")";
            statement.execute(sql);

            statement.close();
            SQLPlugin.getInstance().endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    @Override
    public JSONArray readAllGames() {

        try {
            Connection conn = SQLPlugin.getInstance().startTransaction();

            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM games";
            ResultSet resultSet = statement.executeQuery(sql);

            String jsonResult = "[";
            while(resultSet.next()) {
                jsonResult += "{" +
                        "\"gameID\":" + resultSet.getInt("userID") +
                        ",\"title\":\"" + resultSet.getString("title") +
                        "\",\"model\":\"" + resultSet.getString("model") +
                        "\",\"gameInfo\":\"" + resultSet.getString("gameInfo") + "\"}";
            }
            jsonResult += "]";

            statement.close();
            SQLPlugin.getInstance().endTransaction(conn, true);

            return new JSONArray(jsonResult);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}