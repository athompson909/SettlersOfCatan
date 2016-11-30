/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLPlugin implements PersistenceProvider {

    /**
     * Modifies an already existing game.
     * @param gameJSON JSON with the game info.
     */
    @Override
    public void writeGame(String gameJSON) {

    }

    @Override
    public void writeNewGame(String gameJSON) {

    }

    @Override
    public void writeCommand(String commandJSON) {

    }

    /**
     * Adds a new user.
     */
    @Override
    public void writeUser(String userJSON) {

    }

    @Override
    public String readAllUsers() {
        return null;
    }

    @Override
    public String readAllGames() {
        return null;
    }

    @Override
    public void clearAllData() {

    }
}
