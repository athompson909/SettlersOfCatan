package data_access;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IGameDAO {

    void writeGame(String gameJSON);

    void writeNewGame(String gameJSON);

    void writeCommand(String commandJSON);

    String readAllGames();

}
