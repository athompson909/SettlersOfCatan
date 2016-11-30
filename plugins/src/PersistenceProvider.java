/**
 * Created by adamthompson on 11/29/16.
 */
public interface PersistenceProvider {

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    void writeGame(String gameJSON);

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    void writeNewGame(String gameJSON);

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    void writeCommand(String commandJSON);

    /**
     * Adds a new user.
     * @param userJSON JSON with the new user info.
     */
    void writeUser(String userJSON);

    /**
     *
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    String readAllUsers();

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    String readAllGames();

    /**
     * Clears all the data.
     */
    void clearAllData();

}
