package server_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import server.Server;
import shared.model.JSONTranslator;
import shared.model.commandmanager.game.LoginCommand;

/**
 * Created by adamthompson on 11/7/16.
 */
public class BaseCommandTest extends TestCase {

    public void testHandle() throws ClientException {

        Server server = new Server("localhost", 8081);
        server.run();

        LoginCommand loginCommand = new LoginCommand("Sam", "sam");
        JSONTranslator jsonTranslator = new JSONTranslator();
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setHost("localhost");
        serverProxy.setPort("8081");

        String response = serverProxy.userLogin(jsonTranslator.loginCmdToJSON(loginCommand));
        System.out.println(response);
    }
}
