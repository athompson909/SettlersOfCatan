package server;
import java.util.Timer;     //not sure if this is the right Timer class

/**
 * ServerPoller runs on its own background thread, and every 2-3 seconds sends a request to the server
 * to download the current model state.
 * That new model state will return to the ServerPoller as JSON, which it will then send on to the JSON Translator
 * located within the tests.client Facade, which will finally send the updated objects to the ClientUpdateManager to be
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

    /**
     * This is the proxy defined in the constructor, that the ServerPoller will be sending
     * update requests to every couple seconds.
     */
    private IServerProxy currentProxy;



    /**
     * Constructor for ServerPoller - Takes either a ServerProxy or MockProxy object,
     * and saves a reference to that object so it can send it update requests.
     *
     * @param proxy - either a ServerProxy or MockProxy object: this is the proxy that ServerPoller
     *              will be asking for updates. Doing it this way to use dependency injection.
     */
    public void ServerPoller(IServerProxy proxy){
    }


    /**
     * FetchNewModel() sends an update request to the saved proxy (currentProxy) via HTTP request.
     * This function is called every 2-3 seconds when pollTimer tells it to.
     */
    private void fetchNewModel(){

    }

}
