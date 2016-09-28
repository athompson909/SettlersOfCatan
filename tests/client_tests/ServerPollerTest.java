package client_tests;

import client.ClientFacade;
import client.IServerProxy;
import client.MockProxy;
import client.ServerPoller;
import junit.framework.TestCase;
import shared.model.ClientModel;

import java.util.*;

/**
 * Created by Alise on 9/27/2016.
 */
public class ServerPollerTest extends TestCase {
    private IServerProxy proxy = new MockProxy();
    private ClientModel model = new ClientModel(0);
    private ClientFacade clientFacade = new ClientFacade(proxy, model);
    private ServerPoller poller;

    private Timer timer;

    public void testServerPoller() {
        //System.out.println("\n\n\n\n\n***** TESTING SERVER POLLER: ******\n");
        poller = new ServerPoller(clientFacade);

        Date start = new Date(); // time right now
        long countDown = 1000000000, secondCount = 0;
        while (true) {
            countDown--;
            if (countDown == 0) {
                countDown = 1000000000;
                secondCount += new Date().getTime() - start.getTime();
                if (secondCount >= 10 * 10000) break;
            }
        }

        assertEquals(5, poller.getFetchCount());
        //System.out.println("Success");
    }

}

