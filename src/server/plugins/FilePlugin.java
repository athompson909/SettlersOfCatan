package server.plugins;

import server.plugins.data_access.IUserDAO;
import server.plugins.data_access.IGameDAO;
import server.plugins.data_access.FileUserDAO;
import server.plugins.data_access.FileGameDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;

/**
 * Created by adamthompson on 11/29/16.
 */
public class FilePlugin implements IPersistenceProvider {
    /**
     * IUserDAO to access users
     */
    private IUserDAO userDAO = new FileUserDAO();

    /**
     * IGameDAO to access games
     */
    private IGameDAO gameDAO = new FileGameDAO();

    /**
     * Singleton
     */
    private static FilePlugin instance = new FilePlugin() {};

    /**
     * @return singleton filePlugin
     */
    public static IPersistenceProvider getInstance() {
        return instance;
    }

    /**
     * private constructor to make singleton
     */
    private FilePlugin(){
    }

    /**
     * Modifies an already existing game.
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
     * @param gameID the ID of the game where this command was executed
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
    public Connection startTransaction(){

        return null;
    }

    /**
     * ends transaction with database
     * @param commit - whether or not to commit the transaction
     */
    public void endTransaction(Connection temp, boolean commit){

    }
}