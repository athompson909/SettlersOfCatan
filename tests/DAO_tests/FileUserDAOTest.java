package DAO_tests;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import server.User;
import server.plugins.data_access.FileUserDAO;

/**
 * Created by sierra on 12/3/16.
 */
public class FileUserDAOTest extends TestCase{

    private FileUserDAO fileUserDAO = new FileUserDAO();
    private Gson gsonTranslator = new Gson();

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private JSONObject user1JSON;
    private JSONObject user2JSON;
    private JSONObject user3JSON;
    private JSONObject user4JSON;


    @Test
    public void setUp() throws Exception {
        super.setUp();

        setUpUsers();

    }

    private void setUpUsers(){
        user1 = new User("Zapato", "zapato12", 10);
        user1JSON = new JSONObject(gsonTranslator.toJson(user1));
        user2 = new User("Sam", "sam", 0);
        user2JSON = new JSONObject(gsonTranslator.toJson(user2));
        user3 = new User("Poop", "poop", 3);
        user3JSON = new JSONObject(gsonTranslator.toJson(user3));
        user4 = new User("WHAT", "welp", 6);
        user4JSON = new JSONObject(gsonTranslator.toJson(user4));

    }

    @Test
    public void testWriteUser() throws Exception {
        System.out.println(">TESTING WRITEUSER!");

        fileUserDAO.writeUser(user1JSON);
        fileUserDAO.writeUser(user2JSON);
        fileUserDAO.writeUser(user3JSON);
        fileUserDAO.writeUser(user4JSON);
    }


    @Test
    public JSONArray testReadUsers() throws Exception {
        System.out.println(">TESTING READALLUSERS!");

        JSONArray readUsersResult = fileUserDAO.readAllUsers();

        assertTrue(readUsersResult.length() == 4);

        return readUsersResult;  //temp
    }



}
