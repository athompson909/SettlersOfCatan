package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.SQLPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLGameDAO implements IGameDAO {

    private int gameCount = 0;

    /**
     * Modifies an already existing game.
     * @param
     */
    @Override
    public void writeGame(int gameID, String modelJSON, String gameInfoJSON) {

        try {
            Connection conn = SQLPlugin.startTransaction();

            modelJSON = SQLPlugin.formatForSQL(modelJSON);
            gameInfoJSON = SQLPlugin.formatForSQL(gameInfoJSON);

            Statement statement = conn.createStatement();
            String sql = "UPDATE games SET model = \'" + modelJSON + "\', gameInfo = \'" + gameInfoJSON + "\' WHERE gameID = " + gameID;
            statement.execute(sql);

            statement.close();
            SQLPlugin.endTransaction(conn, true);
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
            Connection conn = SQLPlugin.startTransaction();

//            modelJSON = SQLPlugin.formatForSQL(modelJSON);
//            gameInfoJSON = SQLPlugin.formatForSQL(gameInfoJSON);
            JSONObject modelJSONObj = new JSONObject(modelJSON);
            JSONObject gameInfoJSONObj = new JSONObject(gameInfoJSON);
//            JSONArray jsonArray = gameInfoJSONObj.getJSONArray("players");

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO games (gameID, model, gameInfo) " +
                    "VALUES (" + gameID + ", \'" + modelJSONObj + "\',\'" + gameInfoJSONObj + "\')";
            statement.execute(sql);
            gameCount++;

            statement.close();
            SQLPlugin.endTransaction(conn, true);
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
            Connection conn = SQLPlugin.startTransaction();

            String commandJSONStr = SQLPlugin.formatForSQL(commandJSON.toString());

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO commands (gameID, command) " +
                    "VALUES (" + gameID + ",\'" + commandJSONStr + "\')";
            statement.execute(sql);

            statement.close();
            SQLPlugin.endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * this is new Adam!!!
     *
     * @return
     */
    public JSONArray readAllCommands(){


        try {
            Connection conn = SQLPlugin.startTransaction();

            Statement statement = conn.createStatement();
            String commandsSql = "SELECT * FROM commands";
            ResultSet commandsResultSet = statement.executeQuery(commandsSql);

            HashMap<Integer, JSONArray> commandsMap = new HashMap<>();

            for(int i = 0; i < gameCount; ++i) commandsMap.put(i, new JSONArray());

            while(commandsResultSet.next()) {
                int key = commandsResultSet.getInt("gameID");
                commandsMap.get(key).put(new JSONObject(commandsResultSet.getString("command")));
            }

            statement.close();
            SQLPlugin.endTransaction(conn, true);

            JSONArray allCommands = new JSONArray();
            for(int key : commandsMap.keySet()) {
                allCommands.put(commandsMap.get(key));
            }

            return allCommands;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    @Override
    public JSONArray readAllGames() {

        try {
            Connection conn = SQLPlugin.startTransaction();

            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM games";
            ResultSet resultSet = statement.executeQuery(sql);

            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("gameID", resultSet.getInt("gameID"));
                jsonObj.put("model",  new JSONObject(resultSet.getString("model")));
                jsonObj.put("gameInfo", new JSONObject(resultSet.getString("gameInfo")));

                jsonArray.put(jsonObj);
                gameCount++;
            }



            statement.close();
            SQLPlugin.endTransaction(conn, true);

            return jsonArray;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void clearCommands(int gameID) {

    }
}
