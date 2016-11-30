import data_access.FileGameDAO;
import data_access.FileUserDAO;
import data_access.IGameDAO;
import data_access.IUserDAO;

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
     * @param gameJSON JSON with the game info.
     */
    @Override
    public void writeGame(String gameJSON) {
        gameDAO.writeGame(gameJSON);
    }

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    @Override
    public void writeNewGame(String gameJSON) {
        gameDAO.writeNewGame(gameJSON);
    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    @Override
    public void writeCommand(String commandJSON) {
        gameDAO.writeCommand(commandJSON);
    }

    /**
     * Adds a new user.
     * @param userJSON JSON with the new user info.
     */
    @Override
    public void writeUser(String userJSON) {
        userDAO.writeUser(userJSON);
    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public String readAllUsers() {
        return userDAO.readAllUsers();
    }

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    @Override
    public String readAllGames() {
        return gameDAO.readAllGames();
    }

    /**
     * Clears all the data.
     */
    @Override
    public void clearAllData() {

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
}
