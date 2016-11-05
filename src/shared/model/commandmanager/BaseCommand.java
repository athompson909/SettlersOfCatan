package shared.model.commandmanager;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

/**
 * Created by Mitchell on 9/15/2016.
 */
public abstract class BaseCommand implements HttpHandler{
    /**
     * Handles commands received from the server and returns a response
     * @param exchange
     */
    @Override
    public void handle(HttpExchange exchange){
        //Todo
        //translate and set parameters
        //get cookie
        //call serverExec and return JSON to the client
    }

    /**
     * Kicks off server Execution
     */
    public abstract JSONObject serverExec(BaseCommand command);
}
