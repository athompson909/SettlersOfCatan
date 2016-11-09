package server;

import shared.locations.HexLocation;
import shared.model.map.Port;

import java.util.HashMap;

/**
 * Created by adamthompson on 11/4/16.
 */
public class User {

    /**
     * Name of the user used to login.
     */
    private String userName;

    /**
     * Password user used to login.
     */
    private String userPassword;

    /**
     * ID of the user, created when the user logs in.
     */
    private int userID;

    /**
     * HashMap of all the games the user is in.
     * The Key is the index of the game, and the value is their index value in that game.
     */
    private HashMap<Integer, Integer> addedGames = new HashMap<>();

    public User(String username, String password, int userid) {
        this.userName = username;
        this.userPassword = password;
        this.userID = userid;
    }

    //GETTERS
    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUserID() {
        return userID;
    }

    public HashMap<Integer, Integer> getAddedGames() {
        return addedGames;
    }


    //SETTERS
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
