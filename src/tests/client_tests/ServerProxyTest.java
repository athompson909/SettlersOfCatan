package tests.client_tests;

import client.ServerProxy;
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
            "  \"username\": \"xxx\",\n" +
            "  \"password\": \"xxx\"\n" +
            "}";

    public void testHttpPost() {
        String urlStr = "http://localhost:8081/user/login";

        assertEquals("Success", serverProxy.httpPost(urlStr, loginStr));
        assertEquals("http error", serverProxy.httpPost(urlStr, badLoginStr));
    }

    public void testUserLogin() {

        JSONObject loginJson = new JSONObject(loginStr);

        assertEquals("Success", serverProxy.userLogin(loginJson));

    }

}
