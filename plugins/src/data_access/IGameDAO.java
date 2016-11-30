package data_access;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IGameDAO {

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
     * Reads all the games created.
     * @return a String with all the created games.
     */
    String readAllGames();

}
