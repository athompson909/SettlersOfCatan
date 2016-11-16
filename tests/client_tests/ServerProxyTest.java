package client_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.Server;

/**
 * Created by adamthompson on 9/22/16.
 *
 * this is to verify that interactions with the server work before integration
 *
 * this extends JUnit 3 TestCase (maybe change it to being JUnit 4 TestCase)
 */
public class ServerProxyTest extends TestCase {

    private ServerProxy serverProxy = new ServerProxy();

    private Server server;

    public void testAddAIFunc() throws ClientException {

        JSONObject loginJson = new JSONObject(LOGIN_STR);
        serverProxy.userLogin(loginJson);
        JSONObject jsonObject = new JSONObject(JOIN_STR);
        String jsonStr = serverProxy.gameJoin(jsonObject);
        serverProxy.gameReset();
    }


    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();

        serverProxy.setHost("localhost");
        serverProxy.setPort("8081");

        server = new Server(serverProxy.getHost(), Integer.parseInt(serverProxy.getPort()));
        server.run();

        System.out.println("Testing ServerProxy: userLogin()");


        JSONObject loginJson = new JSONObject(LOGIN_STR);
        serverProxy.userRegister(loginJson);
        assertEquals("Success", serverProxy.userLogin(loginJson));
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
        server.stop();
    }

    //todo: separate this all out


    /**
     * don't use NEW_LOGIN_STR in this method
     */
    @Test
    public void testUserLoginAndRegister() throws ClientException {
        System.out.println("Testing ServerProxy: userLogin()");


//        JSONObject loginJson = new JSONObject(LOGIN_STR);
//        serverProxy.userRegister(loginJson);
//        assertEquals("Success", serverProxy.userLogin(loginJson));

        JSONObject badLoginJson = new JSONObject(BAD_LOGIN_STR);
        assertEquals("{\"http error 404\":\"Not Found\"}", serverProxy.userLogin(badLoginJson));
    }

    /**
     * prints the json object that should be returned by the server
     * for some reason with the demo server it doesn't print out any of the games that I create
     */
    @Test
    public void testGamesList() throws ClientException {
        System.out.println("Testing ServerProxy: gamesList()");
        //compare variables in the two games lists
        JSONArray jsonArray = serverProxy.gamesList();
        String jsonArrayStr = jsonArray.toString();
        // verifies that the item returned is indeed a json array with a list of games
        assertEquals("[{\"players\":[", jsonArrayStr.substring(0, 13));
        assertEquals(']', jsonArrayStr.charAt(jsonArrayStr.length()-1));
    }

    @Test
    public void testGameCreate() throws ClientException {
        System.out.println("Testing ServerProxy: gameCreate()");

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
        System.out.println("Testing ServerProxy: gameJoin()");


//        JSONObject jsonObject = new JSONObject(GAME_CREATE_STR);
//        String responseStr = serverProxy.gameCreate(jsonObject).toString();
//        assertEquals("{\"players\":[{},{},{},{}],\"id\":", responseStr.substring(0, 30));

        JSONObject jsonObject2 = new JSONObject(JOIN_STR2);
        String jsonStr = serverProxy.gameJoin(jsonObject2);
        assertEquals("Success", jsonStr);
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
        System.out.println("Testing ServerProxy: gameModelVersion()");

        testGameJoin();

        String model = serverProxy.gameModelVersion();
        assertEquals("{\"bank\":{\"ore\":19,\"wood\"", model.substring(0, 24));
    }

    @Test
    public void testListAI() throws ClientException {
        System.out.println("Testing ServerProxy: listAI()");

        assertEquals("[\"LARGEST_ARMY\"]", serverProxy.listAI().toString());
    }

    @Test
    public void testFinishTurn() throws ClientException {
        System.out.println("Testing ServerProxy: finishTurn()");

        JSONObject request = new JSONObject(FINISH_TURN);
        JSONObject response = serverProxy.finishTurn(request);
        //assertEquals("{\"bank\":{", response.toString().substring(0, 9));
    }

    @Test
    public void testPlaySoldier() throws ClientException {
        System.out.println("Testing ServerProxy: playSoldier()");

        JSONObject request = new JSONObject(PLAY_SOLDIER);
        JSONObject response = serverProxy.playSoldier(request);
//        assertEquals("{\"bank\":{", response.toString().substring(0, 9));
    }

    @Test
    public void testPlayMonopoly() throws ClientException {
        System.out.println("Testing ServerProxy: playMonopoly()");

        JSONObject request = new JSONObject(MONOPOLY);
        JSONObject response = serverProxy.playMonopoly(request);
//        assertEquals("{\"bank\":{", response.toString().substring(0, 9));
    }

    @Test
    public void testBuildRoad() throws ClientException {
        System.out.println("Testing ServerProxy: buildRoad()");

        JSONObject request = new JSONObject(BUILD_ROAD);
        JSONObject response = serverProxy.buildRoad(request);
        assertEquals("{\"http error 400\":\"Bad Request\"}", response.toString());
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
            "  \"id\": 0,\n" +
            "  \"color\": \"puce\"\n" +
            "}";

    private final String JOIN_STR2 = "{\n" +
            "  \"id\": 1,\n" +
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
            "  \"playerIndex\": 0,\n" +
            "  \"content\": \"hello\"\n" +
            "}";

    private final String ROLL_NUMBER = "{\n" +
            "  \"type\": \"rollNumber\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"number\": 7\n" +
            "}";

    private final String ROB_PLAYER = "{\n" +
            "  \"type\": \"robPlayer\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"victimIndex\": 1,\n" +
            "  \"location\": {\n" +
            "    \"x\": 1,\n" +
            "    \"y\": 1\n" +
            "  }\n" +
            "}";

    private final String FINISH_TURN = "{\n" +
            "  \"type\": \"finishTurn\",\n" +
            "  \"playerIndex\": 0\n" +
            "}";

    private final String BUY_DEV_CARD = "{\n" +
            "  \"type\": \"buyDevCard\",\n" +
            "  \"playerIndex\": \"integer\"\n" +
            "}";

    private final String YEAR_OF_PLENTY = "{\n" +
            "  \"type\": \"Year_of_Plenty\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"resource1\": \"ore\",\n" +
            "  \"resource2\": \"wheat\"\n" +
            "}";

    private final String ROAD_BUILDING = "{\n" +
            "  \"type\": \"Road_Building\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"spot1\": {\n" +
            "    \"x\": 0,\n" +
            "    \"y\": -1,\n" +
            "    \"direction\": \"south\"\n" +
            "  },\n" +
            "  \"spot2\": {\n" +
            "    \"x\": -1,\n" +
            "    \"y\": 0,\n" +
            "    \"direction\": \"south\"\n" +
            "  }\n" +
            "}";

    private final String PLAY_SOLDIER = "{\n" +
            "  \"type\": \"Soldier\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"victimIndex\": 2,\n" +
            "  \"location\": {\n" +
            "    \"x\": 1,\n" +
            "    \"y\": 1\n" +
            "  }\n" +
            "}";

    private final String MONOPOLY = "{\n" +
            "  \"type\": \"Monopoly\",\n" +
            "  \"resource\": \"wood\",\n" +
            "  \"playerIndex\": 0\n" +
            "}";

    private final String MONUMENT = "{\n" +
            "  \"type\": \"Monument\",\n" +
            "  \"playerIndex\": 0\n" +
            "}";

    private final String BUILD_ROAD = "{\"" +
            "   type\":\"buildRoad\"," +
            "   \"playerIndex\":0," +
            "   \"roadLocation\":" +
            "   {" +
            "       \"x\":0," +
            "       \"y\":1," +
            "       \"direction\":\"NW\"" +
            "   }," +
            "   \"free\":true" +
            "}";

    private final String OFFER_TRADE = "{\n" +
            "  \"type\": \"offerTrade\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"offer\": {\n" +
            "    \"brick\": 0,\n" +
            "    \"ore\": 0,\n" +
            "    \"sheep\": 0,\n" +
            "    \"wheat\": 0,\n" +
            "    \"wood\": 1\n" +
            "  },\n" +
            "  \"receiver\": 2\n" +
            "}";

    private final String MARITIME_TRADE = "{\n" +
            "  \"type\": \"maritimeTrade\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"ratio\": 4,\n" +
            "  \"inputResource\": \"wood\",\n" +
            "  \"outputResource\": \"brick\"\n" +
            "}";

    private final String DISCARD_CARDS = "{\n" +
            "  \"type\": \"discardCards\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"discardedCards\": {\n" +
            "    \"brick\": 0,\n" +
            "    \"ore\": 0,\n" +
            "    \"sheep\": 1,\n" +
            "    \"wheat\": 0,\n" +
            "    \"wood\": 3\n" +
            "  }\n" +
            "}";

}
