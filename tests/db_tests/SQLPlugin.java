package db_tests;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLPlugin {
    /**
     * private constructor to make singleton
     */
    private SQLPlugin(){
    }

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    public void writeGame(String gameJSON) {

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
        return null;
    }

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    public String readAllGames() {
        return null;
    }

    /**
     * Clears all the data.
     */
    public void clearAllData() {

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

        if(commit)
            conn.commit();

        conn.close();
    }


    /**
     * creates a database if none exist or if the user asks it to be deleted and recreated
     */
    public void createDatabase() {
        // if db does not exist: {
        DBCreateHelper.createNewDatabase("catan.db");
        // then create the tables
        // }
    }
}
