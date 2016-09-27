package client_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.*;

/**
 * Created by adamthompson on 9/22/16.
 *
 * this is to verify that interactions with the server work before integration
 */
public class ServerProxyTest extends TestCase {

    private ServerProxy serverProxy = new ServerProxy();


    @Test
    /**
     * this puts all the methods together making it possible for some of them to be tested
     * because some methods require other actions to have occured with the server before they can be executed
     */
    public void testServerProxy() throws ClientException {

        // ***** OPERATIONS ABOUT USERS: *****
        //  testing register
        testUserRegister();
        //  testing login
        JSONObject loginJson = new JSONObject(loginStr);
        String str = serverProxy.userLogin(loginJson);
        System.out.println(str);
        assertEquals("Success", str);

        // ***** GAME QUERIES/ACTIONS (PRE-JOINING): *****
        testGamesList();
        testGameCreate(); //todo: uncomment
        testGamesList();
        //  testing join
        testGameJoin(); // this has to be run// from here otherwise login cookie will be null
        System.out.println("loginCookieStr: " + serverProxy.getLoginCookie());
        System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());
        testGameSave();
        testGameLoad();
        testAddAI(); // http response is 400 when I run this, I'm turning on and off the server
        testListAI();

        // ***** OPERATIONS FOR THE GAME YOU'RE IN: *****
        testGameModelVersion();
        testGetGameCommands();
        //testExecuteGameCommands();

        testGameReset();
        // see if the model changes:
        //testGameModelVersion();


    }


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

        String response = serverProxy.userRegister(loginJson);
        System.out.println("userRegister(): " + response);

        String str = serverProxy.getRegisterCookie();
        System.out.println("registerCookie: " + str);
        //assertEquals("http error: bad request", serverProxy.userRegister(loginJson));

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
        System.out.print("gamesList(): \n" + jsonStr + "\n\n");
    }

    public void testGameCreate() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_CREATE_STR);
        System.out.println(serverProxy.gameCreate(jsonObject));
    }

    /**
     * can only be executed after testGameLogin() has been run (so we're going to run from inside there)
     * don't run this test case alone!
     */
    public void testGameJoin() throws ClientException {

        JSONObject jsonObject = new JSONObject(JOIN_STR);
        String jsonStr = serverProxy.gameJoin(jsonObject);
        assertEquals("Success", jsonStr);
        System.out.println("gameJoin(): " + jsonStr);
        //System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());
    }

    public void testGameSave() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_SAVE_STR);
        String str = serverProxy.gameSave(jsonObject);
        System.out.println("gameSave(): " + str);
        assertEquals("Success", str);
    }

    public void testGameLoad() throws ClientException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"testgame1002-Adam\"}");
        String str = serverProxy.gameLoad(jsonObject);
        System.out.println("gameLoad(): " + str);
        assertEquals("Success", str);
    }

    /**
     * @throws ClientException
     * The version number of the model that the caller already has. It goes up by one for each command that is applied.
     * If you send this parameter, you will get a model back only if the current model is newer than the specified
     *  version number.
     * Otherwise, it returns the string "true" to notify the caller that it already has the current model state.
     */
    public void testGameModelVersion() throws ClientException {
        String model = serverProxy.gameModelVersion(0);
        System.out.println("version=0:\n" + model + "\n\n");
        model = serverProxy.gameModelVersion(1);
        System.out.println("version=1:\n" + model + "\n\n");
    }

    /**
     * make sure this returns the model in the state it had on the first roubd
     * todo: test
     * @throws ClientException
     */
    public void testGameReset() throws ClientException {
        JSONObject jsonObject = serverProxy.gameReset();
        System.out.println("gameReset():\n" + jsonObject.toString() + "\n\n");
    }

    public void testGetGameCommands() throws ClientException {
        JSONObject jsonObject = serverProxy.getGameCommands();
        if(jsonObject != null)
            System.out.println("getGameCommands():\n" + jsonObject.toString() + "\n\n");
        else System.out.println("NO GAME COMMANDS\n");
    }

    /**
     * todo: get a commands list... ask Sierra if she hardcoded one
     * wait to test this until to do above has been completed
     * @throws ClientException
     */
    public void testExecuteGameCommands() throws ClientException {
        JSONObject gameCommandsJson = new JSONObject(GAME_COMMANDS);
        System.out.println("executeGameCommands():\n" + serverProxy.executeGameCommands(gameCommandsJson) + "\n\n");
    }

    public void testAddAI() throws ClientException {
        JSONObject jsonObject = new JSONObject(AI_REQUEST);
        System.out.println("addID(): " + serverProxy.addAI(jsonObject));
    }

    public void testListAI() throws ClientException {
        System.out.println("listAI(): " + serverProxy.listAI().toString());
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
            "  \"username\": \"adam2\",\n" +
            "  \"password\": \"adam2\"\n" +
            "}";

    private final String GAME_CREATE_STR = "{\n" +
            "  \"randomTiles\": \"true\",\n" +
            "  \"randomNumbers\": \"true\",\n" +
            "  \"randomPorts\": \"true\",\n" +
            "  \"name\": \"testgame1002-Adam\"\n" +
            "}";

    private final String JOIN_STR = "{\n" +
            "  \"id\": \"3\",\n" +
            "  \"color\": \"blue\"\n" +
            "}";

    private final String GAME_SAVE_STR = "{\n" +
            "  \"id\": \"3\",\n" +
            "  \"name\": \"testgame1002-Adam\"\n" +
            "}";

    private final String GAME_COMMANDS = "";

    private final String AI_REQUEST = "{\n" +
            "  \"AIType\": \"LARGEST_ARMY\"\n" +
            "}";
}
