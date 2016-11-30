/**
 * Created by adamthompson on 11/29/16.
 */
public class FilePlugin implements PersistenceProvider {

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    @Override
    public void writeGame(String gameJSON) {

    }

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    @Override
    public void writeNewGame(String gameJSON) {

    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    @Override
    public void writeCommand(String commandJSON) {

    }

    /**
     * Adds a new user.
     * @param userJSON JSON with the new user info.
     */
    @Override
    public void writeUser(String userJSON) {

    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public String readAllUsers() {
        return null;
    }

    /**
     * Reads all the games created.
     * @return a String with all the created games.
     */
    @Override
    public String readAllGames() {
        return null;
    }

    /**
     * Clears all the data.
     */
    @Override
    public void clearAllData() {

    }
}
