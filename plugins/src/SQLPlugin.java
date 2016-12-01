import data_access.IGameDAO;
import data_access.IUserDAO;
import data_access.SQLGameDAO;
import data_access.SQLUserDAO;
import database_related.DBCreateHelper;

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


    /**
     * creates a database if none exist or if the user asks it to be deleted and recreated
     */
    public void createDatabase() {
        // if db does not exist: {
        DBCreateHelper.createNewDatabase("catan");
        // then create the tables
        // }
    }
}
