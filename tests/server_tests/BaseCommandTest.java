package server_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.shared_utils.MockResponses;
import server.Server;
import shared.model.JSONTranslator;
import shared.model.commandmanager.game.LoginCommand;
import shared.model.commandmanager.game.RegisterCommand;

/**
 * Created by adamthompson on 11/7/16.
 */
public class BaseCommandTest extends TestCase {


    private JSONTranslator jsonTranslator = new JSONTranslator();

    private ServerProxy serverProxy = new ServerProxy();

    private Server server = new Server("localhost", 8081);

    public void setUp() throws Exception {
        super.setUp();

        server.run();
        serverProxy.setHost("localhost");
        serverProxy.setPort("8081");

        LoginCommand loginCommand = new LoginCommand("Sam", "sam");

        String response = serverProxy.userLogin(jsonTranslator.loginCmdToJSON(loginCommand));
        System.out.println(response);

        String gameJoinResponse = serverProxy.gameJoin(new JSONObject(MockResponses.JOIN_REQUEST));
        System.out.println(gameJoinResponse);
        assertEquals("Success", gameJoinResponse);

    }

    public void tearDown() throws Exception {
        super.tearDown();
        server.stop();
    }

    public void testLogin() throws ClientException {

        LoginCommand loginCommand = new LoginCommand("Sam", "sam");

        String response = serverProxy.userLogin(jsonTranslator.loginCmdToJSON(loginCommand));
        System.out.println(response);
    }

    public void testRegister() throws ClientException {

        RegisterCommand registerCommand = new RegisterCommand("Bobby", "bobby");

        String response = serverProxy.userRegister(jsonTranslator.registerCmdToJSON(registerCommand));
        System.out.println(response);
    }

    public void testListAI() throws ClientException {

        JSONArray responseJSON = serverProxy.listAI();
        String responseStr = responseJSON.toString();
        System.out.println(responseStr);
        assertEquals("[\"LARGEST_ARMY\"]", responseStr);
    }

    public void testAll() throws ClientException {

//        Server server = new Server("localhost", 8081);
//        server.run();
//
//        LoginCommand loginCommand = new LoginCommand("Sam", "sam");
//
//        String response = serverProxy.userLogin(jsonTranslator.loginCmdToJSON(loginCommand));
//        System.out.println(response);

        JSONArray gamesListJSON = serverProxy.gamesList();
        System.out.println(gamesListJSON.toString());

//        String gameJoinResponse = serverProxy.gameJoin(new JSONObject(MockResponses.JOIN_REQUEST));
//        System.out.println(gameJoinResponse);
//        assertEquals("Success", gameJoinResponse);

        JSONObject gameCreateResponse = serverProxy.gameCreate(new JSONObject(MockResponses.GAME_CREATE_REQUEST));
        System.out.println(gameCreateResponse.toString());

        String gameModelVersionResponse = serverProxy.gameModelVersion();
        System.out.println(gameModelVersionResponse);

        String addAIResponse = serverProxy.addAI(new JSONObject(MockResponses.ADD_AI));
        System.out.println(addAIResponse);
        assertEquals("Success", addAIResponse);
    }


    public void testMoves() throws ClientException {

        JSONObject sendChatResponseJSON = serverProxy.sendChat(new JSONObject(MockResponses.SEND_CHAT_REQUEST_JSON));
        assertNotSame("HTTP 400 Error : Bad Request", sendChatResponseJSON.toString());
        System.out.println("send chat passed");

        JSONObject rollDiceResponseJSON = serverProxy.rollNumber(new JSONObject(MockResponses.ROLL_NUMBER_REQUEST_JSON));
        assertNotSame("HTTP 400 Error : Bad Request", rollDiceResponseJSON.toString());
        System.out.println("roll dice passed");

        JSONObject robPlayerResponseJSON = serverProxy.robPlayer(new JSONObject(MockResponses.ROB_PLAYER_REQUEST_JSON));
        assertNotSame("HTTP 400 Error : Bad Request", robPlayerResponseJSON.toString());
        System.out.println("rob player passed");

    }


}
