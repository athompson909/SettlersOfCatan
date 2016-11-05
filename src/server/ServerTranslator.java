package server;

import org.json.JSONObject;
import shared.model.ClientModel;

import java.util.ArrayList;

/**
 * ServerTranslator takes care of all operations related to translating object into JSON and vice-versa
 *
 * It's a singleton, so any class in the Server package can use it whenever they want!
 *
 * Created by adamthompson on 11/4/16.
 */
public class ServerTranslator {

    //translate functions:
    // other server responses into JSON for the HTTP response objects
    // JSON to BaseCommand objects

    /**
     * Parts of the singleton pattern
     */
    private static ServerTranslator instance = new ServerTranslator();

    public static ServerTranslator getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private ServerTranslator() {

    }


    /**
     * Translates a ClientModel into a huge JSON-formatted String ready to be sent as part of a
     * HTTPExchange response body.
     *
     * @param model the ClientModel object that needs to be translated into JSON
     * @return a String (JSON) representation of the ClientModel object
     */
    public String modelToJSON(ClientModel model){


        return "";

    }

    /**
     * Translates an arraylist of all Games objects into a JSON-formatted String ready to be sent
     * as part of an HTTPExchange response body. This is used when the Client (specifically during the
     * JoinGameView miniPoller process) calls games/list.
     *
     * @param gamesList
     * @return a String (JSON) representation of the list of all games
     */
    public String gamesListToJSON(ArrayList<Game> gamesList){


        return "";
    }









}
