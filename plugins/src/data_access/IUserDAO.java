package data_access;

/**
 * Created by adamthompson on 11/29/16.
 */
public interface IUserDAO {

    /**
     * Adds a new user.
     */
    void writeUser(String userJSON);

    /**
     * Reads all the users.
     * @return the list of users.
     */
    String readAllUsers();

}
