package db_tests;

import org.json.JSONObject;
import server.plugins.database_related.DBCreateHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by adamthompson on 11/29/16.
 *
 * THIS IS FOR TESTING PURPOSES ONLY (MUCH OF WHAT'S IN HERE HAS BEEN COPIED OVER TO THE ACTUAL PLUGINS)
 */
public class SQLPluginDemo {
    /**
     * private constructor to make singleton
     * (this isn't a singleton in db_tests package) ***
     */
    public SQLPluginDemo(){
    }

    /**
     * Modifies an already existing game.
     *
     * the game title doesn't get used in this one I don't think
     * @param gameJSON JSON with the game info.
     */
    public void writeGame(String gameJSON) {
        JSONObject newGameJSON = new JSONObject(gameJSON);
        int gameId = newGameJSON.getInt("gameId");

        String model = newGameJSON.getString("model");
        String gameInfo = newGameJSON.getString("gameInfo");

        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            String sql = "UPDATE games SET model = " + model + ", gameInfo = " + gameInfo + " WHERE gameID = " + gameId;
            statement.execute(sql);

            statement.close();
            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // does a SQL update
    }

    /**
     * Adds a new game.
     * This method currently always tells the connection to commit
     *
     *  I'm guessing the format of the string should be like this:
     *  {
     *   gameId: 0,
     *   title: "title",
     *   model: "BLOB",
     *   gameInfo: "BLOB"
     *  }
     *  !!PLEASE CORRECT ME IF I'M WRONG!! -Adam
     *
     * @param gameJSON JSON with the new game info.
     */
    public void writeNewGame(String gameJSON) {
        JSONObject newGameJSON = new JSONObject(gameJSON);
        int gameId = newGameJSON.getInt("gameId");
        String title = newGameJSON.getString("title");
        String model = newGameJSON.getString("model");
        String gameInfo = newGameJSON.getString("gameInfo");

        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO games (gameID, title, model, gameInfo) " +
                    "VALUES (" + gameId + "," + title + "," + model + "," + gameInfo + ")";
            statement.execute(sql);

            statement.close();
            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    public void writeCommand(String commandJSON) {
        JSONObject newGameJSON = new JSONObject(commandJSON);
        int gameId = newGameJSON.getInt("gameId");
        String command = newGameJSON.getString("command");

        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO commands (gameID, command) " +
                    "VALUES (" + gameId + "," + command + ")";
            statement.execute(sql);

            statement.close();
            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds a new user.
     * @param userJSON JSON with the new user info.
     */
    public void writeUser(String userJSON) {
        JSONObject newGameJSON = new JSONObject(userJSON);
        int userId = newGameJSON.getInt("userId");
        String username = newGameJSON.getString("username");
        String password = newGameJSON.getString("password");

        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO users (userId, username, password) " +
                    "VALUES (" + userId + "," + username + "," + password + ")";
            statement.execute(sql);

            statement.close();
            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    public String readAllUsers() {

        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sql);

            String jsonResult = "[";
            while(resultSet.next()) {
                jsonResult += "{" +
                        "\"userID\":" + resultSet.getInt("userID") +
                        ",\"username\":\"" + resultSet.getString("username") +
                        "\",\"password\":\"" + resultSet.getString("password") + "\"}";
            }
            jsonResult += "]";

            statement.close();
            endTransaction(conn, true);

            return jsonResult;
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
    public String readAllGames() {

        try {
            Connection conn = startTransaction();

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
            endTransaction(conn, true);

            return jsonResult;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Clears all the data.
     * it drops all the tables and recreates them (does not delete the database just clears all data from it)
     */
    public void clearAllData() {
        try {
            Connection conn = startTransaction();
            DBCreateHelper.createTables(conn);
            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Starts transaction with database
     */
    public Connection startTransaction() throws Exception {

        Connection conn = null;

        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:" + DBCreateHelper.getDbName();
        conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);

        return conn;
    }

    /**
     * ends transaction with database
     * @param commit - whether or not to commit the transaction
     */
    public void endTransaction(Connection conn, boolean commit) throws Exception {

        if(commit) conn.commit();

        conn.close();
    }


    /**
     * creates a database if none exist or if the user asks it to be deleted and recreated
     */
    public void createDatabase() {
        DBCreateHelper.createNewDatabase();
    }
}
