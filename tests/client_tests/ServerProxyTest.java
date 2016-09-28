package client_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by adamthompson on 9/22/16.
 *
 * this is to verify that interactions with the server work before integration
 *
 * this extends JUnit 3 TestCase (maybe change it to being JUnit 4 TestCase)
 */
public class ServerProxyTest extends TestCase {

    private ServerProxy serverProxy = new ServerProxy();


    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();

        JSONObject loginJson = new JSONObject(LOGIN_STR);
        serverProxy.userLogin(loginJson);
        JSONObject jsonObject = new JSONObject(JOIN_STR);
        String jsonStr = serverProxy.gameJoin(jsonObject);
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //todo: separate this all out


    /**
     * don't use NEW_LOGIN_STR in this method
     */
    @Test
    public void testUserLogin() throws ClientException {

        JSONObject loginJson = new JSONObject(LOGIN_STR);
        assertEquals("Success", serverProxy.userLogin(loginJson));
//        testGameJoin(); // this has to be run from here otherwise login cookie will be null

        JSONObject badLoginJson = new JSONObject(BAD_LOGIN_STR);
        assertEquals("http error 400: Bad Request", serverProxy.userLogin(badLoginJson));
    }

    @Test
    public void testUserRegister() throws ClientException {

        JSONObject loginJson = new JSONObject(LOGIN_STR);
        assertEquals("http error 400: Bad Request", serverProxy.userRegister(loginJson));

        // these have already been tested, obviously they will not pass if executed more than once for the same string
//        JSONObject newLoginJson = new JSONObject(NEW_LOGIN_STR); // todo: uncomment before official submit
//        assertEquals("Success", serverProxy.userRegister(newLoginJson));
    }

    /**
     * prints the json object that should be returned by the server
     * for some reason with the demo server it doesn't print out any of the games that I create
     */
    @Test
    public void testGamesList() throws ClientException {
        //compare variables in the two games lists
        JSONArray jsonArray = serverProxy.gamesList();
        String jsonArrayStr = jsonArray.toString();
        // verifies that the item returned is indeed a json array with a list of games
        assertEquals("[{\"players\":[{\"color\":\"", jsonArrayStr.substring(0, 23));
        assertEquals(']', jsonArrayStr.charAt(jsonArrayStr.length()-1));
    }

    @Test
    public void testGameCreate() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_CREATE_STR);
        String responseStr = serverProxy.gameCreate(jsonObject).toString();
        assertEquals("{\"players\":[{},{},{},{}],\"id\":", responseStr.substring(0, 30));
    }

    /**
     * can only be executed after testGameLogin() has been run (so we're going to run from inside there)
     * don't run this test case alone!
     */
    @Test
    public void testGameJoin() throws ClientException {

        JSONObject jsonObject = new JSONObject(JOIN_STR);
        String jsonStr = serverProxy.gameJoin(jsonObject);
        assertEquals("Success", jsonStr);
    }

    @Test
    public void testGameSave() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_SAVE_STR);
        String str = serverProxy.gameSave(jsonObject);
        assertEquals("Success", str);
    }

    @Test
    public void testGameLoad() throws ClientException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"testgame1002-Adam\"}");
        String str = serverProxy.gameLoad(jsonObject);
        assertEquals("Success", str);
    }

    /**
     * The version number of the model that the caller already has. It goes up by one for each command that is applied.
     * If you send this parameter, you will get a model back only if the current model is newer than the specified
     *  version number.
     * Otherwise, it returns the string "true" to notify the caller that it already has the current model state.
     * @throws ClientException
     */
    @Test
    public void testGameModelVersion() throws ClientException {
        String model = serverProxy.gameModelVersion(0);
        assertEquals("\"true\"", model);
        model = serverProxy.gameModelVersion(1);
        assertEquals("{\"deck\":{\"yearOfPlenty\":", model.substring(0, 24));
    }

    /**
     * make sure this returns the model in the state it had on the first roubd
     * todo: test
     * @throws ClientException
     */
    @Test
    public void testGameReset() throws ClientException {
        JSONObject jsonObject = serverProxy.gameReset();
        String jsonStr = jsonObject.toString();
        assertEquals("{\"bank\":{\"", jsonStr.substring(0, 10));
    }

    @Test
    public void testGetGameCommands() throws ClientException {
        JSONArray jsonArray = serverProxy.getGameCommands();
        assertEquals("[]", jsonArray.toString());
    }

    @Test
    public void testListAI() throws ClientException {
        assertEquals("[\"LARGEST_ARMY\"]", serverProxy.listAI().toString());
    }


    private final String LOGIN_STR = "{\n" +
            "  \"username\": \"Sam\",\n" +
            "  \"password\": \"sam\"\n" +
            "}";

    private final String BAD_LOGIN_STR = "{\n" +
            "  \"username\": \"xxxxxx\",\n" +
            "  \"password\": \"xxxxxx\"\n" +
            "}";

    private final String NEW_LOGIN_STR = "{\n" +
            "  \"username\": \"adam3\",\n" +
            "  \"password\": \"adam3\"\n" +
            "}";

    private final String GAME_CREATE_STR = "{\n" +
            "  \"randomTiles\": \"true\",\n" +
            "  \"randomNumbers\": \"true\",\n" +
            "  \"randomPorts\": \"true\",\n" +
            "  \"name\": \"testgame1002-Adam\"\n" +
            "}";

    private final String JOIN_STR = "{\n" +
            "  \"id\": 3,\n" +
            "  \"color\": \"blue\"\n" +
            "}";

    private final String GAME_SAVE_STR = "{\n" +
            "  \"id\": 3,\n" +
            "  \"name\": \"testgame1002-Adam\"\n" +
            "}";

    private final String GAME_COMMANDS = "[]";

    private final String AI_REQUEST = "{\n" +
            "  \"AIType\": \"LARGEST_ARMY\"\n" +
            "}";

    private final String SEND_CHAT = "{\n" +
            "  \"type\": \"sendChat\",\n" +
            "  \"playerIndex\": \"0\",\n" +
            "  \"content\": \"hello\"\n" +
            "}";
}
