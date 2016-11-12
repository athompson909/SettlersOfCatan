package server;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.shared_utils.Converter;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ClientModel;
import shared.model.TradeOffer;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.*;
import shared.model.messagemanager.*;
import shared.model.player.Player;
import shared.model.resourcebank.*;
import shared.model.turntracker.TurnTracker;
import shared.shared_utils.Converter;
import shared.shared_utils.MockJSONs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    private ServerTranslator() {}


//////////////// UTILS //////////////////

    /**
     * Helper function for contexts such as login, register, join, and addAI
     *
     * @return "Success" if true, and null if false
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

    /**
     * used in the games/list command - converts one GameInfo object to a JSON string
     * @param gameInfo
     * @return
     */
    public String gameInfoToJSON(GameInfo gameInfo) {

        JSONObject gameInfoJSON = new JSONObject();
        JSONObject emptyElement = new JSONObject();

        gameInfoJSON.put("title", gameInfo.getTitle());
        gameInfoJSON.put("id", gameInfo.getId());

        JSONArray playersJSONArr = new JSONArray(); //needs to have 4 spots, even if some are empty ("{}")
        for (PlayerInfo player : gameInfo.getPlayers()){
            playersJSONArr.put(gsonTranslator.toJson(player));
        }

        while (playersJSONArr.length() < 4){
            //add empty brackets until you get to 4
//            if (playersJSONArr.length() <= 3){
//                playersJSONArr.put("{},"); //comma
//            }
                playersJSONArr.put(emptyElement);
        }

        gameInfoJSON.put("players", playersJSONArr);

        return gameInfoJSON.toString();
    }

    public String gameListToString(GameInfo[] gameList) {
        return MockJSONs.GAMES_LIST;//for testing
    }

    public String clientModelToString(ClientModel clientModel) {
        return MockJSONs.GAME_MODEL;//todo: change
    }


///////////////// TO JSON ///////////////////

    /**
     * Translates a ClientModel into a huge JSON-formatted String ready to be sent as part of a
     * HTTPExchange response body.
     *
     * @param model the ClientModel object that needs to be translated into JSON
     * @return a String (JSON) representation of the ClientModel object
     */
    public String modelToJSON(ClientModel model){

        /*
            - .append() adds items to the JSONObject inside an encapsulating JSONArray
            - .put() adds it to the JSONObject just like normal
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
        if (model.getTradeOffer() != null){
            TradeOffer tempTO = model.getTradeOffer();
            JSONObject tOJSON = new JSONObject(gsonTranslator.toJson(tempTO));
            modelJSON.put("tradeOffer", tOJSON);
        }

//MAP start --------------------------------------------------
        Map tempMap = model.getMap();
        HashMap<HexLocation, Hex> tempHexesMap = tempMap.getHexes();
        HashMap<HexLocation, Port> tempPortsMap = tempMap.getPorts();
        Robber tempRobber = tempMap.getRobber();
        HashMap<VertexLocation, VertexObject> tempCitiesStlmtsMap = tempMap.getVertexObjects();
        HashMap<EdgeLocation, EdgeValue> tempRoadsMap = tempMap.getEdgeValues();
            JSONObject mapJSON = new JSONObject();
        int tempRadius = tempMap.getRadius();
            mapJSON.put("radius", tempRadius);

     //SERIALIZE HEXES (hexes)
        JSONArray tempHexesArr = serializeHexes(tempHexesMap);
        mapJSON.put("hexes", tempHexesArr);

     //SERIALIZE PORTS (ports)
        JSONArray tempPortsArr = serializePorts(tempPortsMap);
        mapJSON.put("ports", tempPortsArr);


     //SERIALIZE ROADS (roads)
        JSONArray tempRoadsArr = new JSONArray();
        for (EdgeLocation key3 : tempRoadsMap.keySet()){
            EdgeValue currRoadEV = tempRoadsMap.get(key3);
            JSONObject currRoadJSON = new JSONObject(gsonTranslator.toJson(currRoadEV));

                tempRoadsArr.put(currRoadJSON);
        }
        mapJSON.put("roads", tempRoadsArr);

     //SERIALIZE CITIES AND SETTLEMENTS (cities, settlements)
        JSONArray tempStlmtsArr = new JSONArray();
        JSONArray tempCitiesArr = new JSONArray();
        for (VertexLocation key4 : tempCitiesStlmtsMap.keySet()){
            //doing this all manually - I set some things to be transient to deserialize ok on the clientside so it's all jacked up
            VertexObject currCSVO = tempCitiesStlmtsMap.get(key4);
            JSONObject currCSJSON = new JSONObject();
                currCSJSON.put("owner", currCSVO.getOwner());
            VertexLocation currCSVL = currCSVO.getVertexLocation();
            JSONObject currCSVLJSON = new JSONObject(gsonTranslator.toJson(currCSVL));
                currCSJSON.put("location", currCSVLJSON);

            if (currCSVO.getPieceType() == PieceType.CITY){
                tempCitiesArr.put(currCSJSON);
            }
            else if (currCSVO.getPieceType() == PieceType.SETTLEMENT){
                tempStlmtsArr.put(currCSJSON);
            }
        }
        mapJSON.put("settlements", tempStlmtsArr);
        mapJSON.put("cities", tempCitiesArr);


     //SERIALIZE ROBBER (robber)
        //he only has a coordinate location (hexlocation)
        HexLocation tempRobberHL = tempRobber.getCurrentHexlocation();
        JSONObject tempRobberJSON = new JSONObject();
            tempRobberJSON.put("x", tempRobberHL.getX());
            tempRobberJSON.put("y", tempRobberHL.getY());
        mapJSON.put("robber", tempRobberJSON);
//MAP done-----------------------------------------------------------
        modelJSON.put("map", mapJSON);

//SERIALIZE PLAYERS[] (players)
        Player[] tempPlayers = model.getPlayers();
        JSONArray tempPlayersArr = new JSONArray();

        for (int p = 0; p < tempPlayers.length; p++){
            Player currPlayer = tempPlayers[p];
            JSONObject currPlayerJSON = new JSONObject(gsonTranslator.toJson(currPlayer));
            tempPlayersArr.put(currPlayerJSON);
        }
        modelJSON.put("players", tempPlayersArr);


        //---------------------------------------------------
        return modelJSON.toString();
    }

    //HELPER FUNCTIONS

    /**
     * Serializes the list of Ports into a JSONArray.
     * @param portsMap - pulled from the ClientModel's Map
     * @return
     */
    private JSONArray serializePorts(HashMap<HexLocation, Port> portsMap){
        JSONArray portsJSONArr = new JSONArray();
        for (HexLocation key2 : portsMap.keySet()){
            Port currPort = portsMap.get(key2);
            //if portType != THREE, is ratio is 2
            //if portType == THREE, it will not have a resource AND its ratio is 3
            JSONObject currPortJSON = new JSONObject();
            int currPortRatio = 0; //temp
            PortType currPortType = currPort.getResource();
            if (currPortType == PortType.THREE){
                currPortRatio = 3;
                //don't add in the resource type because it's generic
            }
            else {
                currPortRatio = 2;
                //so it has a real resource type  - formatted into lowercase
                currPortJSON.put("resource", currPortType.toString().toLowerCase());
            }
            currPortJSON.put("ratio", currPortRatio);
            HexLocation currPortHL = currPort.getLocation();
            JSONObject currPortLocJSON = new JSONObject();
            currPortLocJSON.put("x", currPortHL.getX());
            currPortLocJSON.put("y", currPortHL.getY());
            currPortJSON.put("location", currPortLocJSON);
            EdgeDirection currPortED = currPort.getEdgeDirection();
            currPortJSON.put("direction", Converter.edgeDirToLetter(currPortED));

            //JSONObject currPortJSON = new JSONObject(gsonTranslator.toJson(currPort));
            portsJSONArr.put(currPortJSON);
        }

        return portsJSONArr;
    }

    /**
     * Serializes the list of Hexes into a JSONArray.
     * @param hexesMap  - pulled from the ClientModel's Map
     * @return
     */
    private JSONArray serializeHexes(HashMap<HexLocation, Hex> hexesMap){
        JSONArray hexesJSONArr = new JSONArray();

        for (HexLocation key1 : hexesMap.keySet()){
            Hex currHex = hexesMap.get(key1);
            //if it has a resource AND number, it's a regular land hex.
            //if it has a number but no resource, it's a desert hex.
            //if it has no number OR resource, it's a water hex.
            JSONObject currHexJSON = new JSONObject(gsonTranslator.toJson(currHex));
            hexesJSONArr.put(currHexJSON);
        }

        return hexesJSONArr;
    }

    /**
     * Converts the current list of games on the server into JSON.
     * This is used in the GameListView/GameHub minipoller process.
     *
     * @param gameInfos the list of all Games created on the server
     * @return a String (JSON) representation of the list of all games
     */
    public String gamesListToJSON(GameInfo[] gameInfos){

        /*
        GameManager (singleton) has a list of all Games.
        Each Game has a GameInfo object.
        Each GameInfo object has a list of <= 4 PlayerInfo objects.
         */

        JSONArray gameListJSONArr = new JSONArray();

        //maybe I should make this function go grab the list of gameInfos automatically...
        //during the test though GamesManager is empty, so I can't do that
         //   HashMap<Integer, Game> gamesMap = GamesManager.getInstance().getAllGames();

        for (int g = 0; g < gameInfos.length; g++){
            GameInfo currGameInfo = gameInfos[g];
            JSONObject currGIJSON = new JSONObject(gameInfoToJSON(currGameInfo));  //use the new bracket style
            //JSONObject currGIJSON = new JSONObject(gsonTranslator.toJson(currGameInfo));
            gameListJSONArr.put(currGIJSON);
        }

        return gameListJSONArr.toString();
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
