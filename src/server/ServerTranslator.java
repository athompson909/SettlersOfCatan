package server;

import client.data.GameInfo;
import org.json.JSONObject;
import shared.model.ClientModel;
import shared.model.commandmanager.game.*;

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
    //translate each possible command from JSON into a commandObject

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


    /**
     * Translates an AddAICommand JSON request coming from the Client into a real AddAICommand object
     *
     * @param addAICmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public AddAICommand addAICommandFromJSON(JSONObject addAICmdJSON){


        return null;
    }

    /**
     * Translates an ExecuteGameCommandsCommand coming from the Client as a JSON server request into a real ExecuteGameCommandsCommand object
     *
     * @param execGameCmdsJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public ExecuteGameCommandsCommand executeGameCmdsCommandFromJSON(JSONObject execGameCmdsJSON){


        return null;
    }

    /**
     * Translates an GameCreateCommand coming from the Client as a JSON server request into a real GameCreateCommand object
     *
     * @param gameCreateCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GameCreateCommand gameCreateCommandFromJSON(JSONObject gameCreateCmdJSON){


        return null;
    }

    /**
     * Translates an GameJoinCommand coming from the Client as a JSON server request into a real GameJoinCommand object
     *
     * @param gameJoinCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GameJoinCommand gameJoinCommandFromJSON(JSONObject gameJoinCmdJSON){


        return null;
    }

    /**
     * Translates an GameJoinCommand coming from the Client as a JSON server request into a real GameJoinCommand object
     *
     * @param gameListCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GameListCommand gameListCommandFromJSON(JSONObject gameListCmdJSON){


        return null;
    }

    /**
     * this will be for contexts such as login, register, join, and addAI
     *
     * @return "Success" if true (I think)
     */
    public String booleanToString(boolean bool) {
        return null;
    }


    /**
     * converts a string array to a string that follows json format
     *
     * @param listAIStr { "LARGEST_ARMY" }
     * @return listAIStr as json (but a string)
     */
    public String listAIToString(String[] listAIStr) {
        return null;
    }

    public String gameInfoToString(GameInfo gameInfo) {
        return null;
    }

    public String gameListToString(GameInfo[] gameList) {
        return null;
    }

    public String clientModelToString(ClientModel clientModel) {
        return null;
    }




}
