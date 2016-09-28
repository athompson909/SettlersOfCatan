package client_tests;

import client.ServerProxy;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by adamthompson on 9/22/16.
 *
 * this is to verify that interactions with the server work before integration
 */
public class ServerProxyTest extends TestCase {

    private ServerProxy serverProxy = new ServerProxy();

    /**
     * this puts all the methods together making it possible for some of them to be tested
     * because some methods require other actions to have occured with the server before they can be executed
     */
    @Test
    public void testServerProxy() throws ClientException {

        ServerProxyTestMethods methods = new ServerProxyTestMethods(serverProxy);

        // ***** OPERATIONS ABOUT USERS: *****
        System.out.println("for /user:\nTESTING REGISTER:\n");
        methods.testUserRegister();
        //  testing login
        System.out.println("\n\nTESTING LOGIN:\n");
        JSONObject loginJson = new JSONObject(LOGIN_STR);
        System.out.println("login data: " + loginJson.toString());
        String str = serverProxy.userLogin(loginJson);
        System.out.println("response from server: " + str);
        assertEquals("Success", str);

        // ***** GAME QUERIES/ACTIONS (PRE-JOINING): *****
        System.out.println("\n\n\nfor /games:\nTESTING GAMES LIST:\n");
        methods.testGamesList();
        System.out.println("\n\nTESTING GAME CREATE:\n");
        methods.testGameCreate(); //todo: uncomment
        System.out.println("\n\nTESTING GAME JOIN:\n");
        methods.testGameJoin(); // this has to be run// from here otherwise login cookie will be null
        System.out.println("loginCookieStr: " + serverProxy.getLoginCookie());
        System.out.println("joinCookieStr: " + serverProxy.getJoinCookie());
        System.out.println("\n\nTESTING GAME SAVE:\n");
        methods.testGameSave();
        System.out.println("\n\nTESTING GAME LOAD:\n");
        methods.testGameLoad();

        // ***** OPERATIONS FOR THE GAME YOU'RE IN: *****
        System.out.println("\n\n\nfor /game:\nTESTING GAME MODEL VERSION:\n");
        methods.testGameModelVersion();
        System.out.println("\n\nTESTING GET GAME COMMANDS:\n");
        methods.testGetGameCommands();
//        System.out.println("\n\nTESTING EXECUTE GAME COMMANDS:\n"); // this will be tested later on
//        testExecuteGameCommands();
        System.out.println("\n\nTESTING GAME RESET:\n");
        methods.testGameReset();
        System.out.println("\n\nTESTING GAME MODEL VERSION:\n");
        methods.testGameModelVersion();
        System.out.println("\n\nTESTING ADD AI:\n");
        methods.testAddAI(); // http response is 400 when I run this, I'm going to turn on and off the server
        System.out.println("\n\nTESTING LIST AI:\n");
        methods.testListAI();

//        // ***** ACTIONS YOU CAN TAKE MID GAME: *****
//        System.out.println("\n\n\nfor /moves:\nTESTING SEND CHAT:\n");
//        testSendChat();

        // ***** CHANGE HOW THE SERVER RUNS *****
        System.out.println("\n\n\nfor /util:\nTESTING CHANGE LOG LEVEL:\n");
        methods.testChangeLogLevel();

    }

    private final String LOGIN_STR = "{\n" +
            "  \"username\": \"adam\",\n" + //Sam
            "  \"password\": \"adam\"\n" + //sam
            "}";
}
