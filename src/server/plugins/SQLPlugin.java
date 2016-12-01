package server.plugins;

import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.data_access.IGameDAO;
import server.plugins.data_access.IUserDAO;
import server.plugins.data_access.SQLGameDAO;
import server.plugins.data_access.SQLUserDAO;
import server.plugins.database_related.DBCreateHelper;

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

    /**
     * Singleton
     */
    private static SQLPlugin instance = new SQLPlugin() {};

    /**
     * @return singleton sqlPlugin
     */
    public static IPersistenceProvider getInstance() {
        return instance;
    }

    /**
     * private constructor to make singleton
     */
    private SQLPlugin(){
    }

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    @Override
    public void writeGame(int gameID, String modelJSON, String gameInfoJSON) {
        gameDAO.writeGame(gameID, modelJSON, gameInfoJSON);
    }

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    @Override
    public void writeNewGame(int gameID, String modelJSON, String gameInfoJSON) {
        gameDAO.writeNewGame(gameID, modelJSON, gameInfoJSON);
    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    @Override
    public void writeCommand(JSONObject commandJSON) {
        gameDAO.writeCommand(commandJSON);
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
     * Clears all the data.
     */
    @Override
    public void clearAllData() {

    }

    @Override
    public void clearCommands(int gameID) {

    }

    /**
     * Starts transaction with database
     */
    public void startTransaction(){

    }

    /**
     * ends transaction with database
     * @param commit - whether or not to commit the transaction
     */
    public void endTransaction(boolean commit){

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
