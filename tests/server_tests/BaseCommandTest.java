package server_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import server.Server;
import shared.model.JSONTranslator;
import shared.model.commandmanager.game.LoginCommand;
import shared.model.commandmanager.game.RegisterCommand;

/**
 * Created by adamthompson on 11/7/16.
 */
public class BaseCommandTest extends TestCase {

    public void testLogin() throws ClientException {

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

    public void testRegister() throws ClientException {

        Server server = new Server("localhost", 8081);
        server.run();

        RegisterCommand registerCommand = new RegisterCommand("Bobby", "bobby");
        JSONTranslator jsonTranslator = new JSONTranslator();
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setHost("localhost");
        serverProxy.setPort("8081");

        String response = serverProxy.userRegister(jsonTranslator.registerCmdToJSON(registerCommand));
        System.out.println(response);

    }
}
