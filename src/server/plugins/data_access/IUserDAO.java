package server.plugins.data_access;

import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IUserDAO {

    /**
     * Adds a new user.
     */
    void writeUser(JSONObject userJSON);

    /**
     * Reads all the users.
     * @return the list of users.
     */
    String readAllUsers();

}
