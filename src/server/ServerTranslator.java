package server;

import client.data.GameInfo;
import com.google.gson.Gson;
import org.json.JSONObject;
import shared.model.ClientModel;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.ArrayList;

/**
 * ServerTranslator takes care of all operations related to translating object into JSON and vice-versa
 *
 *  It's a singleton, so any class in the Server package can use it whenever they want!
 *  a.k.a. basically a utils class.
 *
 * Created by adamthompson on 11/4/16.
 */
public class ServerTranslator {

    Gson gsonTranslator = new Gson();

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


//////////////// UTILS //////////////////

    /**
     * Helper function for contexts such as login, register, join, and addAI
     *
     * @return "Success" if true (I think), and null if false
     */
    public String booleanToString(boolean bool) {

        return (bool ? "Success" : null);
    }

    /**
     * Converts a string array to a string that follows json format
     *
     * @param listAIStr { "LARGEST_ARMY" }
     * @return listAIStr as json (but a string)
     */
    public String listAIToString(String[] listAIStr) {
        return "[\"LARGEST_ARMY\"]";
    }

    public String gameInfoToString(GameInfo gameInfo) {
        return null;
    }

    public String gameListToString(GameInfo[] gameList) {
        return MockResponses.GAMES_LIST;//for testing
    }

    public String clientModelToString(ClientModel clientModel) {
        return null;
    }


///////////////// TO JSON ///////////////////
    /**
     * Translates an arraylist of all Games objects into a JSON-formatted String ready to be sent
     * as part of an HTTPExchange response body. This is used when the Client (specifically during the
     * JoinGameView miniPoller process) calls games/list.
     *
     * @param gamesList
     * @return a String (JSON) representation of the list of all games
     */
    public String gamesListToJSON(ArrayList<Game> gamesList) {


        return "";
    }


    /**
     * Translates a ClientModel into a huge JSON-formatted String ready to be sent as part of a
     * HTTPExchange response body.
     *
     * @param model the ClientModel object that needs to be translated into JSON
     * @return a String (JSON) representation of the ClientModel object
     */
    public String modelToJSON(ClientModel model){

        // let's try doing it in pieces to avoid having to bring lots of nested data up front to higher classes
        //Pull out pieces of the clientModel, change them to JSON using Gson, and then add each converted piece
        // to the JSONobject using JSONArrays and JSONObject.put() or .append()

        /*
            - .append() adds items inside a JSONArray
            - .put() adds it just like normal
         */

        JSONObject modelJSON = new JSONObject();

//2 little INTs (winner, version)
        int tempVerNum = model.getVersion();
        int tempWinner = model.getWinner();
            modelJSON.put("version", tempVerNum);
            modelJSON.put("winner", tempWinner);

//RESOURCEBANK: DEVCARDLIST AND RESOURCELIST
        ResourceBank tempResBank = model.getResourceBank();
            DevCardList tempDCList = tempResBank.getDevCardList();
            ResourceList tempResList = tempResBank.getResourceList();
        JSONObject dcListJSON = new JSONObject(gsonTranslator.toJson(tempDCList));
        JSONObject resListJSON = new JSONObject(gsonTranslator.toJson(tempResList));
            modelJSON.put("deck", dcListJSON);
            modelJSON.put("bank", resListJSON);

//CHAT (chat) AND LOG (log)
        MessageManager tempMMgr = model.getMessageManager();
        MessageList tempChat = tempMMgr.getChat();
        MessageList tempLog = tempMMgr.getLog();
            JSONObject chatJSON = new JSONObject(gsonTranslator.toJson(tempChat));
            JSONObject logJSON = new JSONObject(gsonTranslator.toJson(tempLog));
        modelJSON.put("chat", chatJSON);
        modelJSON.put("log", logJSON);


//TURNTRACKER (turntracker)
        TurnTracker tempTT = model.getTurnTracker();
            JSONObject ttJSON = new JSONObject(gsonTranslator.toJson(tempTT));
        modelJSON.put("turnTracker", ttJSON);

//TRADEOFFER (tradeOffer)

        return null;
    }





//COMMAND OBJECTS FROM JSON

///////////////////////// GAME //////////////////////////////

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
     * Translates an GameListCommand coming from the Client as a JSON server request into a real GameListCommand object
     *
     * @param gameListCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GameListCommand gameListCommandFromJSON(JSONObject gameListCmdJSON){


        return null;
    }


    /**
     * Translates an GameLoadCommand coming from the Client as a JSON server request into a real GameLoadCommand object
     *
     * @param gameLoadCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GameLoadCommand gameLoadCommandFromJSON(JSONObject gameLoadCmdJSON){


        return null;
    }

    /**
     * Translates an GameSaveCommand coming from the Client as a JSON server request into a real GameSaveCommand object
     *
     * @param gameSaveCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GameSaveCommand gameSaveCommandFromJSON(JSONObject gameSaveCmdJSON){


        return null;
    }

    /**
     * Translates a GetGameCmdsCommand coming from the Client as a JSON server request into a real GetGameCmdsCommand object
     *
     * @param getGameCmdsCommand
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public GetGameCommandsCommand getGameCmdsCommandFromJSON(JSONObject getGameCmdsCommand){


        return null;
    }

    /**
     * Translates a ListAICommand coming from the Client as a JSON server request into a real ListAICommand object
     *
     * @param listAICommandJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public ListAICommand listAICommandFromJSON(JSONObject listAICommandJSON){


        return null;
    }

    /**
     * Translates a ListAICommand coming from the Client as a JSON server request into a real ListAICommand object
     *
     * @param loginCommandJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public LoginCommand loginCommandFromJSON(JSONObject loginCommandJSON){


        return null;
    }

    /**
     * Translates a RegisterCommand coming from the Client as a JSON server request into a real RegisterCommand object
     *
     * @param registerCommandJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public RegisterCommand registerCommandFromJSON(JSONObject registerCommandJSON){


        return null;
    }

    /**
     * Translates a SendChatCommand coming from the Client as a JSON server request into a real SendChatCommand object
     *
     * @param sendChatCommandJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public SendChatCommand sendChatCommandFromJSON(JSONObject sendChatCommandJSON){


        return null;
    }

    /**
     * Translates a UtilChangeLogLevelCommand coming from the Client as a JSON server request into a real UtilChangeLogLevelCommand object
     *
     * @param utilChangeLogLevelJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public UtilChangeLogLevelCommand utilChangeLogLevelFromJSON(JSONObject utilChangeLogLevelJSON){


        return null;
    }

////////////////////////// MOVES //////////////////////////////

    /**
     * Translates a AcceptTradeCommand coming from the Client as a JSON server request into a real AcceptTradeCommand object
     *
     * @param acceptTradeCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public AcceptTradeCommand acceptTradeCommandFromJSON(JSONObject acceptTradeCmdJSON){


        return null;
    }

    /**
     * Translates a BuildCityCommand coming from the Client as a JSON server request into a real BuildCityCommand object
     *
     * @param buildCityCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public BuildCityCommand buildCityCommandFromJSON(JSONObject buildCityCmdJSON){


        return null;
    }


    /**
     * Translates a BuildRoadCommand coming from the Client as a JSON server request into a real BuildRoadCommand object
     *
     * @param buildRoadCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public BuildRoadCommand buildRoadCommandFromJSON(JSONObject buildRoadCmdJSON){


        return null;
    }

    /**
     * Translates a BuildSettlementCommand coming from the Client as a JSON server request into a real BuildSettlementCommand object
     *
     * @param buildSettlementCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public BuildSettlementCommand buildSettlementCommandFromJSON(JSONObject buildSettlementCmdJSON){


        return null;
    }

    /**
     * Translates a DiscardCommand coming from the Client as a JSON server request into a real DiscardCommand object
     *
     * @param discardCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public DiscardCommand discardCommandFromJSON(JSONObject discardCmdJSON){


        return null;
    }

    /**
     * Translates a FinishTurnCommand coming from the Client as a JSON server request into a real FinishTurnCommand object
     *
     * @param finishTurnCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public FinishTurnCommand finishTurnCommandFromJSON(JSONObject finishTurnCmdJSON){


        return null;
    }

    /**
     * Translates a MaritimeTradeCommand coming from the Client as a JSON server request into a real MaritimeTradeCommand object
     *
     * @param maritimeTradeCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public MaritimeTradeCommand maritimeTradeCommandFromJSON(JSONObject maritimeTradeCmdJSON) {


        return null;
    }

    /**
     * Translates a OfferTradeCommand coming from the Client as a JSON server request into a real OfferTradeCommand object
     *
     * @param offerTradeCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public OfferTradeCommand offerTradeCommandFromJSON(JSONObject offerTradeCmdJSON){


        return null;
    }

    /**
     * Translates a PlayMonopolyCommand coming from the Client as a JSON server request into a real PlayMonopolyCommand object
     *
     * @param playMonopolyCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public PlayMonopolyCommand playMonopolyCommandFromJSON(JSONObject playMonopolyCmdJSON){


        return null;
    }

    /**
     * Translates a PlayMonumentCommand coming from the Client as a JSON server request into a real PlayMonumentCommand object
     *
     * @param playMonumentCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public PlayMonumentCommand playMonumentCommandFromJSON(JSONObject playMonumentCmdJSON){


        return null;
    }

    /**
     * Translates a PlayRoadBuilderCommand coming from the Client as a JSON server request into a real PlayRoadBuilderCommand object
     *
     * @param playRoadBuilderCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public PlayRoadBuilderCommand playRoadBuilderCommandFromJSON(JSONObject playRoadBuilderCmdJSON){


        return null;
    }

    /**
     * Translates a PurchaseDevCardCommand coming from the Client as a JSON server request into a real PurchaseDevCardCommand object
     *
     * @param purchaseDevCardCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public PurchaseDevCardCommand purchaseDevCardCommandFromJSON(JSONObject purchaseDevCardCmdJSON){


        return null;
    }

    /**
     * Translates a RobPlayerCommand coming from the Client as a JSON server request into a real RobPlayerCommand object
     *
     * @param robPlayerCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public RobPlayerCommand robPlayerCommandFromJSON(JSONObject robPlayerCmdJSON){


        return null;
    }

    /**
     * Translates a RollDiceCommand coming from the Client as a JSON server request into a real RollDiceCommand object
     *
     * @param rollDiceCmdJSON
     * @return a Command object (inheriting from BaseCommand) built from the data in the JSON request
     */
    public RollDiceCommand rollDiceCommandFromJSON(JSONObject rollDiceCmdJSON){


        return null;
    }



}
