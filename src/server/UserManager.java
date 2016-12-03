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
     * Hashmap of every active user mapped by username
     */
    private HashMap<String, User> usersByUsername = new HashMap<String, User>();

    /**
     * this is the counter that increments each time a user is added, so everyone has a unique userID
     */
    private static int userID;
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
        userID = 0;
//       buildMockUsers();
    }

    /**
     * Adding a couple mock users for the gamehub's default games
     */
    private void buildMockUsers(){
        addUser("Adam", "adam");
        addUser("Steph", "steph");
        addUser("Mitch", "mitch");
        addUser("Sierra", "sierra");
        addUser("Alise", "alise");

        System.out.println(">USERMANAGER: mockUsers created, there are " + allUsers.size());
    }



    /**
     * Adds a newly registered User to the list of all games
     * and maps it to its gameID
     *
     */
    public boolean addUser(String username, String password){
        //Needs to check if username is UNIQUE
        //Add user to BOTH hashmaps
        if(!usersByUsername.containsKey(username)) {
            User user = new User(username, password, userID);
            allUsers.put(userID, user);
            usersByUsername.put(user.getUserName(), user);
            //save new user to database
            PersistenceManager.getInstance().writeUser(user);
            userID++;
            return true;
        }
        else {return false;} //Means that their username is already in our system
    }

    public User getUser(int userID) {
        if(allUsers.containsKey(userID)) {
            return allUsers.get(userID);
        }
        return null;
    }

    public User getUserByUsername(String username, String password) {
        if(isValidLogin(username, password)) {
            User user = usersByUsername.get(username);
            return user;
        }
        return null;
    }
    /**
     * checks whether or not the user login is valid
     *
     * @param username the username
     * @param password the password
     * @return true if valid
     */
    public boolean isValidLogin(String username, String password) {
        User user = usersByUsername.get(username);
        return (user != null && password.equals(user.getUserPassword()));
    }

    public void setAllUsers(HashMap allUsers){
        this.allUsers = allUsers;

        //create usersByUsername map
        for(Object o: allUsers.values()){
            User user = (User)o;
            usersByUsername.put(user.getUserName(), user);
        }
    }
}
