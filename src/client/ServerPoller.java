package client;
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
 * Created by Mitchell on 9/15/2016.
 * Sierra renamed this class from ServerPuller to ServerPoller on 9/18/2016 :)
 */
public class ServerPoller {

    /**
     * The Timer object that fires an ActionListener (according to Java's documentation page)
     * that signals the Poller to send an update request to the server.
     */
    private Timer pollTimer;

    private ClientFacade clientFacade;

    /**
     * number of seconds to wait between requesting updates from the server
     */
    private int seconds = 2;

    /**
     * Constructor for ServerPoller - Takes either a ServerProxy or MockProxy object,
     * and saves a reference to that object so it can send it update requests.
     *
     * @param clientFacade the clientFacade specific to the current game
     *
     */
    public ServerPoller(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
        pollTimer = new Timer(true);//true tells the program to end this thread if it is the only one left so we cand exit the program
        pollTimer.schedule(new ServerPollerTask(), seconds*1000);
    }

    private class ServerPollerTask extends TimerTask{
        public void run() {
            System.out.println("fetch new model");
            fetchNewModel();
        }
    }


    /**
     * FetchNewModel() sends an update request to the saved proxy (currentProxy) via HTTP request.
     * This function is called every 2-3 seconds when pollTimer tells it to.
     */
    private void fetchNewModel() {
        clientFacade.gameModelVersion();
    }
}
