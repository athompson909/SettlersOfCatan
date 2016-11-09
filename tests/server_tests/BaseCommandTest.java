package server_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONArray;
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

    public void setUp() throws Exception {
        super.setUp();

        Server server = new Server("localhost", 8081);
        server.run();
        serverProxy.setHost("localhost");
        serverProxy.setPort("8081");

    }

    public void tearDown() throws Exception {
        super.tearDown();
        // stop server?
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

        LoginCommand loginCommand = new LoginCommand("Sam", "sam");

        String response = serverProxy.userLogin(jsonTranslator.loginCmdToJSON(loginCommand));
        System.out.println(response);

        JSONArray gamesListJSON = serverProxy.gamesList();
        System.out.println(gamesListJSON.toString());
    }


}
