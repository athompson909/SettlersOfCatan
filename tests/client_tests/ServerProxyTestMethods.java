package client_tests;

import client.ServerProxy;
import exceptions.ClientException;
import org.json.JSONArray;
import org.json.JSONObject;

import static junit.framework.Assert.assertEquals;

/**
 * Created by adamthompson on 9/28/16.
 */
public class ServerProxyTestMethods {

    private ServerProxy serverProxy;

    public ServerProxyTestMethods(ServerProxy serverProxy) {
        this.serverProxy = serverProxy;
    }

    public void testHttpPost() throws ClientException {
        String urlStr = "http://localhost:8081/user/login";

        serverProxy.userRegister(new JSONObject(LOGIN_STR));

        assertEquals("Success", serverProxy.httpPost(urlStr, LOGIN_STR));
        assertEquals("http error: bad request", serverProxy.httpPost(urlStr, BAD_LOGIN_STR));
    }

    /**
     * don't use NEW_LOGIN_STR in this method
     */
    public void testUserLogin() throws ClientException {

        JSONObject loginJson = new JSONObject(LOGIN_STR);
        assertEquals("Success", serverProxy.userLogin(loginJson));
        testGameJoin(); // this has to be run from here otherwise login cookie will be null
        System.out.println("loginCookieStr: " + serverProxy.getLoginCookie());
        System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());

        JSONObject badLoginJson = new JSONObject(BAD_LOGIN_STR);
        assertEquals("http error 400: Bad Request", serverProxy.userLogin(badLoginJson));

    }

    public void testUserRegister() throws ClientException {

        JSONObject loginJson = new JSONObject(LOGIN_STR);
        System.out.println("register data (for already existing user): " + loginJson.toString());

        String response = serverProxy.userRegister(loginJson);
        System.out.println("response from server: " + response);
        String str = serverProxy.getRegisterCookie();
        System.out.println("registerCookie: " + str);
        //assertEquals("http error: bad request", serverProxy.userRegister(loginJson));


        // these have already been tested, obviously they will not pass if executed more than once for the same string
//        JSONObject newLoginJson = new JSONObject(NEW_LOGIN_STR); // todo: uncomment
//        System.out.println("register data (for new user): " + newLoginJson.toString());
//        response = serverProxy.userRegister(newLoginJson);
//        str = serverProxy.getRegisterCookie();
//        System.out.println("response from server: " + response);
//        System.out.println("registerCookie: " + str);
        //assertEquals("Success", serverProxy.userRegister(newLoginJson));
    }

    /**
     * prints the json object that should be returned by the server
     * for some reason with the demo server it doesn't print out any of the games that I create
     */
    public void testGamesList() throws ClientException {

        //compare variables in the two games lists
        JSONArray jsonArray = serverProxy.gamesList();
        String jsonStr = jsonArray.toString();
        System.out.print("response from server: " + jsonStr);
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
        System.out.println("game join data: " + jsonObject.toString());
        String jsonStr = serverProxy.gameJoin(jsonObject);
        assertEquals("Success", jsonStr);
        System.out.println("response from server: " + jsonStr);
        //System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());
    }

    public void testGameSave() throws ClientException {
        JSONObject jsonObject = new JSONObject(GAME_SAVE_STR);
        System.out.println("game save data: " + jsonObject.toString());
        String str = serverProxy.gameSave(jsonObject);
        System.out.println("response from server: " + str);
        assertEquals("Success", str);
    }

    public void testGameLoad() throws ClientException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"testgame1002-Adam\"}");
        System.out.println("game load data: " + jsonObject.toString());
        String str = serverProxy.gameLoad(jsonObject);
        System.out.println("response from server: " + str);
        assertEquals("Success", str);
    }

    /**
     * The version number of the model that the caller already has. It goes up by one for each command that is applied.
     * If you send this parameter, you will get a model back only if the current model is newer than the specified
     *  version number.
     * Otherwise, it returns the string "true" to notify the caller that it already has the current model state.
     * @throws ClientException
     */
    public void testGameModelVersion() throws ClientException {
        String model = serverProxy.gameModelVersion(0);
        System.out.println("version=0:\n" + model + "\n");
        model = serverProxy.gameModelVersion(1);
        System.out.println("version=1:\n" + model + "\n");
    }

    /**
     * make sure this returns the model in the state it had on the first roubd
     * todo: test
     * @throws ClientException
     */
    public void testGameReset() throws ClientException {
        JSONObject jsonObject = serverProxy.gameReset();
        System.out.println("response from server: " + jsonObject.toString() + "\n\n");
    }

    public void testGetGameCommands() throws ClientException {
        JSONArray jsonArray = serverProxy.getGameCommands();
        if(jsonArray != null)
            System.out.println("response from server: " + jsonArray.toString() + "\n\n");
        else System.out.println("NO GAME COMMANDS\n");
    }

    /**
     * todo: get a commands list... ask Sierra if she hardcoded one
     * wait to test this until to do above has been completed
     * @throws ClientException
     */
    public void testExecuteGameCommands() throws ClientException {
        JSONArray gameCommandsJson = new JSONArray(GAME_COMMANDS);
        System.out.println("execute game commands data: " + gameCommandsJson.toString());
        System.out.println("response from server: " + serverProxy.executeGameCommands(gameCommandsJson) + "\n\n");
    }

    public void testAddAI() throws ClientException {
        JSONObject jsonObject = new JSONObject(AI_REQUEST);
        System.out.println("add AI data: " + jsonObject.toString());
        System.out.println("response from server: " + serverProxy.addAI(jsonObject));
    }

    public void testListAI() throws ClientException {
        System.out.println("response from server: " + serverProxy.listAI().toString());
    }


    // ***** /moves: *****
    public void testSendChat() throws ClientException {
        JSONObject jsonObject = new JSONObject(SEND_CHAT);
        System.out.println("send chat data: " + jsonObject.toString());
        System.out.println("response from server: " + serverProxy.sendChat(jsonObject).toString());
    }


    // ***** /util: *****
    public void testChangeLogLevel() throws ClientException {
        JSONObject jsonObject = new JSONObject("{\"logLevel\":\"1\"}");
        System.out.println("test change log level data: " + jsonObject.toString());
        System.out.println("response from server: " + serverProxy.utilChangeLogLevel(jsonObject));
    }



    private final String LOGIN_STR = "{\n" +
            "  \"username\": \"adam\",\n" + //Sam
            "  \"password\": \"adam\"\n" + //sam
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
