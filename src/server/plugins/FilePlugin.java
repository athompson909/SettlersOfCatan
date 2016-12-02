package server.plugins;

import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.data_access.FileGameDAO;
import server.plugins.data_access.FileUserDAO;
import server.plugins.data_access.IGameDAO;
import server.plugins.data_access.IUserDAO;

import java.io.File;
import java.sql.Connection;

/**
 * Created by adamthompson on 11/29/16.
 */
public class FilePlugin implements IPersistenceProvider {

    private static File baseDirectory = new File("./json_files");
    private static File gamesDirectory = new File("./json_files/games");
    private static File usersDirectory = new File("./json_files/users");

    /**
     * IUserDAO to access users
     */
    private IUserDAO userDAO = new FileUserDAO();

    /**
     * IGameDAO to access games
     */
    private IGameDAO gameDAO = new FileGameDAO();

    /**
     * Constructor
     * creates the required directories if they don't exist already in the file structure
     */
    public FilePlugin() {

        if (!baseDirectory.exists()){
            //create the required file structure
            baseDirectory.mkdir();

            if (!gamesDirectory.exists()){
                gamesDirectory.mkdir();
            }
            if (!usersDirectory.exists()){
                usersDirectory.mkdir();
            }

            System.out.println(">FILEGAMEDAO: directories created");
        }

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
        //gameDAO.writeNewGame(gameID, modelJSON, gameInfoJSON);
        gameDAO.writeGame(gameID, modelJSON, gameInfoJSON);  //writeNewGame works the same as writeGame on the FileDAO

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
}
