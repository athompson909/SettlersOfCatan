package client;
import exceptions.ClientException;

import java.util.Date;
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

    //TESTING - for a way to end the ServerPoller loop from the GameFinishedView
    private Timer pollTimer;

    private boolean fetch = true;

    /**
     * Constructor for ServerPoller - Takes either a ServerProxy or MockProxy object,
     * and saves a reference to that object so it can send it update requests.
     */
    public ServerPoller() {
        int seconds = 2;
        pollTimer = new Timer(true);//true tells the program to end this thread if it is the only one left so we cand exit the program
        pollTimer.scheduleAtFixedRate(new ServerPollerTask(), 1, seconds*1000);
    }

    /**
     * executes every 2 seconds//
     */
    private class ServerPollerTask extends TimerTask {
        public void run() {

            try {
                System.out.println("ServerPoller: fetching new model: " + new Date().toString());
                if(fetch) {
                    fetchNewModel();
                }



            }
            catch (ClientException e) {
                e.printStackTrace();
            }

        }
    }


    //TESTING --------
    public Timer getPollTimer() {
        return pollTimer;
    }

    public void setPollTimer(Timer pollTimer) {
        this.pollTimer = pollTimer;
    }
    //---------

    /**
     * fetchNewModel() sends an update request to the saved proxy (currentProxy) via HTTP request.
     * This function is called every 2-3 seconds when pollTimer tells it to.
     *
     * ***comment off method body to stop the modals from closing (WARNING: also stops poller from updating model)
     */
    private void fetchNewModel() throws ClientException {
        ClientFacade.getInstance().gameModelVersion();
    }

    private void doFetchModel(boolean bool) {
        fetch = bool;
    }
}
