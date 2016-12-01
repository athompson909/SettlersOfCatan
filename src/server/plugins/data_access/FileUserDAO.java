package server.plugins.data_access;

import org.json.JSONObject;

/**
 * Created by adamthompson on 11/29/16.
 */
public class FileUserDAO implements IUserDAO {

    /**
     * Adds a new user.
     */
    @Override
    public void writeUser(JSONObject userJSON) {

    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public String readAllUsers() {
        return null;
    }
}
