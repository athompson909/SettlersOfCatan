package server.plugins;

import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.data_access.IGameDAO;
import server.plugins.data_access.IUserDAO;
import server.plugins.data_access.SQLGameDAO;
import server.plugins.data_access.SQLUserDAO;
import server.plugins.database_related.DBCreateHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLPlugin implements IPersistenceProvider {
    /**
     * IUserDAO to access users
     */
    private IUserDAO userDAO = new SQLUserDAO();

    /**
     * IGameDAO to access games
     */
    private IGameDAO gameDAO = new SQLGameDAO();

    public SQLPlugin() {
        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);//30 second query timeout
            statement.execute(DBCreateHelper.CREATE_TABLE_IF_NOT_EXISTS_GAMES_STATEMENT);
            statement.execute(DBCreateHelper.CREATE_TABLE_IF_NOT_EXISTS_USERS_STATEMENT);
            statement.execute(DBCreateHelper.CREATE_TABLE_IF_NOT_EXISTS_COMMANDS_STATEMENT);

            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Modifies an already existing game.
     * @param
     */
    @Override
    public void writeGame(int gameID, String modelJSON, String gameInfoJSON) {
        gameDAO.writeGame(gameID, modelJSON, gameInfoJSON);
    }

    /**
     * Adds a new game.
     * @param
     */
    @Override
    public void writeNewGame(int gameID, String modelJSON, String gameInfoJSON) {
        gameDAO.writeNewGame(gameID, modelJSON, gameInfoJSON);
    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     *
     */
    @Override
    public void writeCommand(JSONObject commandJSON, int gameID) {
        gameDAO.writeCommand(commandJSON, gameID);
    }


    /**
     * Adds a new user.
     * @param userJSON JSON with the new user info.
     */
    @Override
    public void writeUser(JSONObject userJSON) {
        userDAO.writeUser(userJSON);
    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public JSONArray readAllUsers() {
        return userDAO.readAllUsers();
    }

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    @Override
    public JSONArray readAllGames() {
        return gameDAO.readAllGames();
    }


    /**
     * Reads all the command files.
     * @return a JSONArray with all the commands grouped by game.
     */
    @Override
    public JSONArray readAllCommands() {
        return gameDAO.readAllCommands();
    }

    /**
     * Clears all the data.
     * drops all the tables and recreates them (does not delete the database, just empties it)
     */
    @Override
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

    @Override
    public void clearCommands(int gameID) {
        try {
            Connection conn = startTransaction();

            Statement statement = conn.createStatement();
            statement.execute("drop table if exists commands");
            statement.execute(DBCreateHelper.CREATE_TABLE_COMMANDS_STATEMENT);

            endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }    /**
     * Starts transaction with database
     */
    public static Connection startTransaction() throws Exception {

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
    public static void endTransaction(Connection conn, boolean commit) throws Exception {

        if(commit) conn.commit();

        conn.close();
    }

    /**
     * creates a database if none exist or if the user asks it to be deleted and recreated
     */
    public void createDatabase() {

        DBCreateHelper.createNewDatabase();
    }


    /**
     * formats json strings appropriately so that they can be parsed correctly into the sql database
     * @param str a string following json format
     * @return a string that can be inserted into sql without any problems
     */
    public static String formatForSQL(String str) {
        List<Integer> indexes = new ArrayList<>();
        int index = str.indexOf('\'');
        while (index >= 0) {
            indexes.add(index);
            index = str.indexOf('\'', index + 1);
        }

        int offset = 0;
        for (int i : indexes) {
            str = str.substring(0, i+offset) + "'" + str.substring(i+offset, str.length());
            offset++;
        }
        return str;
    }
}
