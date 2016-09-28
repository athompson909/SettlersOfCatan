package client_tests;

import client.ClientFacade;
import client.IServerProxy;
import client.MockProxy;
import client.ServerPoller;
import junit.framework.TestCase;
import shared.model.ClientModel;

import java.util.Timer;

/**
 * Created by Alise on 9/27/2016.
 */
public class ServerPollerTest extends TestCase {
    private IServerProxy proxy = new MockProxy();
    private ClientModel model = new ClientModel(0);
    private ClientFacade clientFacade = new ClientFacade(proxy, model);
    private ServerPoller poller = new ServerPoller(clientFacade);

    private Timer timer = new Timer();

    public void testPoller() {
    }
}
