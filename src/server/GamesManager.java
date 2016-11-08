package server;

import java.util.HashMap;

/**
 *
 *  GamesManager keeps track of all the games that have been created on the server.
 *  The ServerFacade can access any Game it needs from its gameID.
 *
 *  This is a singleton!!
 *
 * Created by adamthompson on 11/4/16.
 */
public class GamesManager {

    /**
     * Hashmap of every game that has been created so far
     */
    private HashMap<Integer, Game> allGames = new HashMap<Integer, Game>();

    /**
     * Parts of the singleton pattern
     */
    private static GamesManager instance = new GamesManager();

    public static GamesManager getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private GamesManager() {

    }

    /**
     * Adds a newly created Game to the list of all games
     * and maps it to its gameID
     *
     * @param newGame - the new Game to add to the list of all games
     */
    public void addGame(Game newGame){

    }

    /**
     * Returns the desired game object to the serverFacade
     * @param gameID of desired game object
     * @return the game object
     */
    public Game getGame(int gameID) {
        return null;
    }

}
