package server;

import client.data.GameInfo;
import client.data.PlayerInfo;
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
import shared.model.JSONTranslator;
import shared.model.TradeOffer;
import shared.model.commandmanager.BaseCommand;
import shared.model.map.*;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;
import shared.shared_utils.Converter;

import java.util.HashMap;

/**
 * ServerTranslator takes care of all operations related to translating object into JSON and vice-versa
 *
 *  It's a singleton, so any class in the Server package can use it whenever they want!
 *  a.k.a. basically a utils class.
 *
 * Created by adamthompson on 11/4/16.
 */
public class ServerTranslator {

    private Gson gsonTranslator = new Gson();
    private JSONTranslator jsonTranslator = new JSONTranslator(); //used just for loading games/stuff from files


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

        JSONArray playersJSONArr = new JSONArray();
        for (PlayerInfo player : gameInfo.getPlayers()){
            playersJSONArr.put(new JSONObject(gsonTranslator.toJson(player)));
        }

        //needs to have 4 spots, even if some are empty ("{}")
        while (playersJSONArr.length() < 4){
                playersJSONArr.put(emptyElement);
        }

        gameInfoJSON.put("players", playersJSONArr);

        return gameInfoJSON.toString();
    }

    public String clientModelToString(ClientModel clientModel) {
        return modelToJSON(clientModel);
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

        JSONObject modelJSON = new JSONObject();

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
        if (model.getTradeOffer() != null) {
            TradeOffer tempTO = model.getTradeOffer();
            JSONObject tOJSON = new JSONObject(gsonTranslator.toJson(tempTO));
                modelJSON.put("tradeOffer", tOJSON);
        }


//SERIALIZE MAP
        JSONObject mapJSON = serializeMap(model.getMap());
        modelJSON.put("map", mapJSON);

//SERIALIZE PLAYERS[] (players)
        Player[] tempPlayers = model.getPlayers();
        JSONArray tempPlayersArr = new JSONArray();

        for (int p = 0; p < tempPlayers.length; p++){
            Player currPlayer = tempPlayers[p];
            JSONObject currPlayerJSON = (currPlayer != null) ? new JSONObject(gsonTranslator.toJson(currPlayer)) : new JSONObject();
            tempPlayersArr.put(currPlayerJSON);
        }
        modelJSON.put("players", tempPlayersArr);


        //---------------------------------------------------
        return modelJSON.toString();
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

        for (int g = 0; g < gameInfos.length; g++){
            GameInfo currGameInfo = gameInfos[g];
            JSONObject currGIJSON = new JSONObject(gameInfoToJSON(currGameInfo));
            gameListJSONArr.put(currGIJSON);
        }

        return gameListJSONArr.toString();
    }

    /**
     *
     * @param command
     * @return
     */
    public String commandObjectToJSON(BaseCommand command) {
        String commandJSONString = gsonTranslator.toJson(command);

        return commandJSONString;
    }

    /**
     *
     * @param user
     * @return
     */
    public JSONObject userToJSON(User user){
        String userJSONString = gsonTranslator.toJson(user);

        JSONObject userJSON = new JSONObject(userJSONString);

        return userJSON;
    }
    //HELPER FUNCTIONS

    /**
     * helps with persistenceManager
     * takes a JSONArray of Users data and converts it to a Hashmap that the UsersManager can use to set its users registery
     * @param usersJSON
     * @return
     */
    public HashMap<Integer, User> usersFromJSON(JSONArray usersJSON){

        HashMap<Integer, User> usersMap = new HashMap<>();

        //get each item in the JSONArray and make a User object out of it
        for (int u = 0; u < usersJSON.length(); u++){
            JSONObject currUserJSON = usersJSON.getJSONObject(u);
            User currUser = gsonTranslator.fromJson(currUserJSON.toString(), User.class);
            int currUserID = currUser.getUserID();

            usersMap.put(currUserID, currUser);
        }

        return usersMap;
    }

    /**
     * helps with PersistenceManager
     * allGamesJSON is the huge JSONArr that has 1 entry representing each game.
     * each entry is a JSONObject that has a clientModel (JSON) under key "model", a gameInfo (JSON) under key "gameInfo", and a list of cmds under key "commands".
     * We use these JSON items to build actual Game objects that the GamesManager can use to populate its game registry.
     *
     * This also sets the GamesManager with the new Games AFTER the extra commands are executed on their respective game models.
     *
     * @param allGamesJSON
     * @return
     */
    public HashMap<Integer, Game> gamesFromJSON(JSONArray allGamesJSON) {

        HashMap<Integer, Game> gamesMap = new HashMap<>();

        //first grab each game entry in the JSONArray so we can pull out its CmdsList, ClientModel and GameInfo objects

        //new way: each gameEntry is a JSONObject.
        // inside the JSONObject are 4 keys->objs:
                    // "gameID" - > int
                    // "gameInfo" - > game info JSON
                    // "model" - > client Model JSON
                    // "commands" - > JSONarray of command JSONObjects   //NOT THIS ANYMORE. CMDS ARE READ IN SEPARATELY

        for (int g = 0; g < allGamesJSON.length(); g++) {

            JSONObject currGameEntry = allGamesJSON.getJSONObject(g); //this should contain 4 key/value pairs
            int currGameID = currGameEntry.getInt("gameID");
            JSONObject currGameGIJSON = currGameEntry.getJSONObject("gameInfo");
            JSONObject currGameCMJSON = currGameEntry.getJSONObject("model");

            //send these all through the JSONTranslator to get real objects
            ClientModel currGameClientModel = jsonTranslator.modelFromJSON(currGameCMJSON);
            GameInfo currGameInfo = jsonTranslator.gameInfoFromJSON(currGameGIJSON); //this is the translator for GIs

            Game currGame = new Game();
            currGame.setClientModel(currGameClientModel);
            currGame.setGameInfo(currGameInfo);

            gamesMap.put(currGameID, currGame);
            //model is now ready to be added to the GamesManager, but hasn't had its extra cmds re-executed yet.

        }

        if (gamesMap.size() == 0){
            System.out.println(">SERVERTRANSLATOR: gamesFromJSON(): there were no game files to read in...");
        }

        return gamesMap;
    }



//    //TEST
//    private HashMap<Integer, BaseCommand> mapCmdsToUserIDs(List<BaseCommand> cmdsList, GameInfo gameInfo){
//
//        HashMap<Integer, BaseCommand> mappedCmds = new HashMap<>();
//
//        //use the userIndex that each cmdObj knows to ask the gameInfo's PlayerInfo @ that index what its playerID is.
//        //thats the playerID we map to the cmdObj.
//
//        for (BaseCommand currCmd : cmdsList){
//            int currCmdPlayerIndex = currCmd.getPlayerIndex();
//              //this doesn't work because the fieldPlayerIndex only exists in the classes that inherit from BaseCommand.
//
//        }
//
//    }


    /**
     * Serializes the Map into a big JSONObject.
     * @param tempMap - pulled from the ClientModel
     * @return
     */
    private JSONObject serializeMap(Map tempMap){
        JSONObject mapJSON = new JSONObject();

            HashMap<HexLocation, Hex> tempHexesMap = tempMap.getHexes();
            HashMap<HexLocation, Port> tempPortsMap = tempMap.getPorts();
            Robber tempRobber = tempMap.getRobber();
            HashMap<VertexLocation, VertexObject> tempCitiesStlmtsMap = tempMap.getVertexObjects();
            HashMap<EdgeLocation, EdgeValue> tempRoadsMap = tempMap.getEdgeValues();
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
                //doing this all manually - I set some things to be transient to deserialize on the client so gson is all jacked up
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
            HexLocation tempRobberHL = tempRobber.getCurrentHexlocation();
            JSONObject tempRobberJSON = new JSONObject();
            tempRobberJSON.put("x", tempRobberHL.getX());
            tempRobberJSON.put("y", tempRobberHL.getY());
        mapJSON.put("robber", tempRobberJSON);

        return mapJSON;
    }

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
            int currPortRatio = 0;
            PortType currPortType = currPort.getResource();
            if (currPortType == PortType.THREE){
                currPortRatio = 3;
                //don't add in the resource type because it's generic
            }
            else {
                currPortRatio = 2;
                //so it has a real resource type
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


}
