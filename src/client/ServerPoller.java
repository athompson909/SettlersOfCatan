package client;
import exceptions.ClientException;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ServerPoller runs on its own background thread, and every 2-3 seconds sends a request to the server
 * to download the current model state.
 * That new model state will return to the ServerPoller as JSON, which it will then send on to the JSON Translator
 * located within the client Facade, which will finally send the updated objects to the ClientUpdateManager to be
 * incorporated into the existing ClientModel.
 *
 * From the Phase 1 Specs sheet:
 *
 * "Design your Server Poller to use dependency injection so that it can be easily configured to use either
 * the “real” or “mock” Server Proxy implementation. This means that rather than calling “new” internally
 * to create a proxy object, the poller should instead have a constructor parameter or setter that
 * can be used to pass in the proxy it should use."
 *
 * *** once the user enters an actual game, have the server poller start polling the server ***
 *
 * Created by Mitchell on 9/15/2016.
 * Sierra renamed this class from ServerPuller to ServerPoller on 9/18/2016 :)
 */
public class ServerPoller {

    /**
     * The Timer object that fires an ActionListener (according to Java's documentation page)
     * that signals the Poller to send an update request to the server.
     */
    private Timer pollTimer;

    /**
     * number of seconds to wait between requesting updates from the server
     */
    private int seconds = 2;

    /**
     * for testing
     */
    private IServerProxy proxy;
    public void setProxy(IServerProxy proxy) {
        this.proxy = proxy;
    };

    /**
     * Constructor for ServerPoller - Takes either a ServerProxy or MockProxy object,
     * and saves a reference to that object so it can send it update requests.
     *
     *
     */
    public ServerPoller() {
        pollTimer = new Timer(true);//true tells the program to end this thread if it is the only one left so we cand exit the program
        pollTimer.scheduleAtFixedRate(new ServerPollerTask(), 1, seconds*1000);
    }

    private class ServerPollerTask extends TimerTask {
        public void run() {
            //System.out.println("fetching new model");
            try {
                if (proxy.runPoller()) {
//                    fetchNewModel();//**** IF THERE ARE ANY PROBLEMS WITH THE PULLER THROWING EXCEPTIONS, COMMENT THIS LINE OFF
                }
            }
            catch (ClientException e) {
                System.out.println("ClientException thrown in ServerProxy.ServerPollerTask.run()," +
                        "\nprobably because of the proxy.hasJoined() function" +
                        "\nor could be from fetchNewModel() if proxy.hasJoined() returned true");
                e.printStackTrace();
            }
        }
    }


    /**
     * fetchNewModel() sends an update request to the saved proxy (currentProxy) via HTTP request.
     * This function is called every 2-3 seconds when pollTimer tells it to.
     */
    public void fetchNewModel() throws ClientException {
        ClientFacade.getInstance().gameModelVersion();
    }
}
