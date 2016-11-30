/**
 * Created by adamthompson on 11/29/16.
 */
public interface PersistenceProvider {

    void writeGame(String gameJSON);

    void writeNewGame(String gameJSON);

    void writeCommand(String commandJSON);

    void writeUser(String userJSON);

    void readAllUsers();

    void readAllGames();

    void clearAllData();

}
