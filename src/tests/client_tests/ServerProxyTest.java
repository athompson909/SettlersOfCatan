package tests.client_tests;

import server.ServerProxy;
import junit.framework.TestCase;
import org.json.JSONObject;

/**
 * Created by adamthompson on 9/22/16.
 *
 * this is to verify that interactions with the server work before integration
 */
public class ServerProxyTest extends TestCase {

    private ServerProxy serverProxy = new ServerProxy();

    private String loginStr = "{\n" +
            "  \"username\": \"adam\",\n" +
            "  \"password\": \"adam\"\n" +
            "}";

    private String badLoginStr = "{\n" +
            "  \"username\": \"xxxxxx\",\n" +
            "  \"password\": \"xxxxxx\"\n" +
            "}";

    private String newLoginStr = "{\n" +
            "  \"username\": \"adam2222\",\n" +
            "  \"password\": \"adam2222\"\n" +
            "}";



    public void testHttpPost() {
        String urlStr = "http://localhost:8081/user/login";

        assertEquals("Success", serverProxy.httpPost(urlStr, loginStr));
        assertEquals("http error: bad request", serverProxy.httpPost(urlStr, badLoginStr));
    }

    /**
     * don't use newLoginStr in this method
     */
    public void testUserLogin() {

        JSONObject loginJson = new JSONObject(loginStr);

        assertEquals("Success", serverProxy.userLogin(loginJson));

    }

    public void testUserRegister() {

        JSONObject loginJson = new JSONObject(loginStr);

        assertEquals("http error: bad request", serverProxy.userRegister(loginJson));

        // these have already been tested, obviously they will not pass if executed more than once for the same string
        //JSONObject newLoginJson = new JSONObject(newLoginStr);
        //assertEquals("Success", serverProxy.userRegister(newLoginJson));
    }

    /**
     * prints the json object that should be returned by the server
     */
    public void testGamesList() {

        //compare variables in the two games lists
        JSONObject jsonObject = serverProxy.gamesList();
        String jsonStr = jsonObject.toString();
        System.out.print(jsonStr);
    }

}
