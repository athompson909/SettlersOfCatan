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
     * Hashmap of every
     */
    private HashMap<Integer, User> allGames = new HashMap<Integer, User>();

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


}
