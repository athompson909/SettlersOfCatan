package server;

import java.util.HashMap;

/**
 *
 *  UserManager keeps track of all active users that have registered in Settlers of Catan.
 *  The ServerFacade can access any User it needs to by their (global) userID.
 *
 *  This is a singleton!!
 *
 * Created by adamthompson on 11/4/16.
 */
public class UserManager {

    /**
     * Hashmap of every active user across the program mapped to their global userID
     */
    private HashMap<Integer, User> allUsers = new HashMap<Integer, User>();

    /**
     * Parts of the singleton pattern
     */
    private static UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private UserManager() {

    }



}
