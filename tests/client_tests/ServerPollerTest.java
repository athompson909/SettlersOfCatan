package client_tests;

import client.*;
import exceptions.ClientException;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;
import shared.model.ClientModel;

import java.util.Timer;

/**
 * Created by Alise on 9/27/2016.
 */
public class ServerPollerTest extends TestCase {
    private IServerProxy proxy = new ServerProxy();
    private ClientModel model = new ClientModel(0);
    private ServerPoller poller;

    private Timer timer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        proxy.setPort("8081");
        proxy.setHost("localhost");
        JSONObject loginJson = new JSONObject(LOGIN_STR);
        proxy.userLogin(loginJson);
        JSONObject jsonObject = new JSONObject(JOIN_STR);
        String jsonStr = proxy.gameJoin(jsonObject);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testServerPoller() throws ClientException {
        System.out.println("Testing ServerPoller: fetchNewModel()");
        String jsonNewModelStr = proxy.gameModelVersion();
        poller = new ServerPoller();
        //String str =poller.fetchNewModel();
        //assertEquals("{\"deck", str.substring(0, 6));
    }



    private final String LOGIN_STR = "{\n" +
            "  \"username\": \"Sam\",\n" +
            "  \"password\": \"sam\"\n" +
            "}";


    private final String JOIN_STR = "{\n" +
            "  \"id\": 0,\n" +
            "  \"color\": \"puce\"\n" +
            "}";

}
