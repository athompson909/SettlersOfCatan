package tests.client_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONObject;

/**
 * Created by adamthompson on 9/22/16.
 *
 * this is to verify that interactions with the server work before integration
 */
public class ServerProxyTest extends TestCase {

    private ServerProxy serverProxy = new ServerProxy();



    public void testHttpPost() throws ClientException {
        String urlStr = "http://localhost:8081/user/login";

        serverProxy.userRegister(new JSONObject(loginStr));

        assertEquals("Success", serverProxy.httpPost(urlStr, loginStr));
        assertEquals("http error: bad request", serverProxy.httpPost(urlStr, badLoginStr));
    }

    /**
     * don't use newLoginStr in this method
     */
    public void testUserLogin() throws ClientException {

        JSONObject loginJson = new JSONObject(loginStr);
        assertEquals("Success", serverProxy.userLogin(loginJson));
        testGameJoin(); // this has to be run from here otherwise login cookie will be null
        System.out.println("loginCookieStr: " + serverProxy.getLoginCookie());
        System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());

        JSONObject badLoginJson = new JSONObject(badLoginStr);
        assertEquals("http error: bad request", serverProxy.userLogin(badLoginJson));

    }

    public void testUserRegister() throws ClientException {

        JSONObject loginJson = new JSONObject(loginStr);

        String str = serverProxy.getRegisterCookie();

        assertEquals("http error: bad request", serverProxy.userRegister(loginJson));

        // these have already been tested, obviously they will not pass if executed more than once for the same string
        //JSONObject newLoginJson = new JSONObject(newLoginStr);
        //String str = serverProxy.getRegisterCookie();
        //assertEquals("Success", serverProxy.userRegister(newLoginJson));
    }

    /**
     * prints the json object that should be returned by the server
     * for some reason with the demo server it doesn't print out any of the games that I create
     */
    public void testGamesList() throws ClientException {

        //compare variables in the two games lists
        JSONObject jsonObject = serverProxy.gamesList();
        String jsonStr = jsonObject.toString();
        System.out.print(jsonStr);
    }

    /**
     * can only be executed after testGameLogin() has been run (so we're going to run from inside there)
     * don't run this test case alone!
     */
    public void testGameJoin() throws ClientException {

        JSONObject jsonObject = new JSONObject(JOIN_STR);
        String jsonStr = serverProxy.gameJoin(jsonObject);
        assertEquals("Success", jsonStr);
        System.out.println("json: " + jsonStr);
        //System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());
    }

    public void testGameCreate() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_CREATE_STR);
        System.out.println(serverProxy.gameCreate(jsonObject));
    }

    public void testGameSave() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_SAVE_STR);
        String str = serverProxy.gameSave(jsonObject);
        System.out.println(str);
        assertEquals("Success", str);
    }

    public void testGameLoad() throws ClientException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"testgame1002-Adam\"}");
        String str = serverProxy.gameLoad(jsonObject);
        System.out.println(str);
        assertEquals("Success", str);
    }

    /**
     * todo: test
     * @throws ClientException
     */
    public void testGameModelVersion() throws ClientException {
        JSONObject jsonObject = serverProxy.gameModelVersion(0); //figure out if there's a version number that could get a correct response for testing
        System.out.println("version=0:\n" + jsonObject.toString() + "\n\n");
        jsonObject = serverProxy.gameModelVersion(1);
        System.out.println("version=1:\n" + jsonObject.toString() + "\n\n");
    }

    /**
     * make sure this returns the model in the state it had on the first roubd
     * todo: test
     * @throws ClientException
     */
    public void testGameReset() throws ClientException {
        JSONObject jsonObject = serverProxy.gameReset();
        System.out.println(jsonObject.toString());
    }




    private String loginStr = "{\n" +
            "  \"username\": \"adam\",\n" + //Sam
            "  \"password\": \"adam\"\n" + //sam
            "}";

    private String badLoginStr = "{\n" +
            "  \"username\": \"xxxxxx\",\n" +
            "  \"password\": \"xxxxxx\"\n" +
            "}";

    private String newLoginStr = "{\n" +
            "  \"username\": \"adam2222\",\n" +
            "  \"password\": \"adam2222\"\n" +
            "}";

    private final String GAME_CREATE_STR = "{\n" +
            "  \"randomTiles\": \"true\",\n" +
            "  \"randomNumbers\": \"true\",\n" +
            "  \"randomPorts\": \"true\",\n" +
            "  \"name\": \"testgame1002-Adam\"\n" +
            "}";

    private final String JOIN_STR = "{\n" +
            "  \"id\": \"4\",\n" +
            "  \"color\": \"blue\"\n" +
            "}";

    private final String GAME_SAVE_STR = "{\n" +
            "  \"id\": \"4\",\n" +
            "  \"name\": \"testgame1002-Adam\"\n" +
            "}";
}
