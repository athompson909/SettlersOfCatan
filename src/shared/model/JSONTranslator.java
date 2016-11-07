package shared.model;

import client.data.GameInfo;
import client.data.PlayerInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.locations.*;
import shared.model.commandmanager.BaseCommand;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.*;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.sun.tools.internal.ws.processor.model.Message;

/**
 * JSONTranslator gets the new model from the server as a huge string, converts it to a JSONObject,
 * uses GSON to break it down into objects, then sends those new objects to the
 * ClientUpdateManager to be distributed to the existing ClientModel objects.
 *
 * There needs to be a function for every BaseCommand to be translated to JSON. JSON>BaseCommandObj isn't necessary,
 * we just need a way for the ClientFacade to change its BaseCommand objs to JSON readable by the server.
 *
 *
 * Created by Sierra on 9/22/16.
 */
public class JSONTranslator {

    /**
     * This is the temporary ClientModel that will be created from the JSON coming back from the server.
     * We will give this object to ClientUpdateManager after it has been fully parsed out.
     *
     */
    private ClientModel newClientModel;

    /**
     * This Gson object can be reused as many times as you want
     * we're just using it to access its translating functions
     */
    private com.google.gson.Gson gsonConverter = new Gson();

    /**
     * This is the string that always is returned by the GsonTranslator (in each function)
     */
    private String stringResult = null;

    /**
     * This is the converted form of stringResult that will be modified/returned by each function
     */
    private JSONObject jsonObjectResult = null;

    /**
     * Constructor
     */
    public void JSONTranslator(){}

    /**
     * TranslateModel() takes the new JSON clientModel from the server,
     * and pulls it apart into object that make up the ClientModel.
     * Using those smaller objects, it builds a complete ClientModel object at the end.
     * it would be really helpful to have a reference to the OLD clientModel here,
     * because when building the new ClientModel object, not every single object/data member in it
     * is going to be updated, just a few.
     * But I guess as long as the UpdateManager knows to only look at certain parts of the new ClientModel
     * to update its existing ClientModel, it doesn't matter what the extra parts of the new ClientModel hold.
     *
     *
     * @throws JsonSyntaxException - this is Gson's exception
     * @param newModelJSON - this is the huge JSON string coming back directly from the server
     * @return newClientModel - the new ClientModel object created from the server's response JSON
     */
    public ClientModel modelFromJSON(JSONObject newModelJSON) throws JsonSyntaxException {

        //Break up ClientModel pieces and build a new ClientModel object manually:
//GET MAP
        JSONObject newMapJSON = newModelJSON.getJSONObject("map");

   //GET RADIUS
        int newCMRadius = newMapJSON.getInt("radius");

//GET HEXES
        JSONArray newHexesJSONArr = newMapJSON.getJSONArray("hexes");
        HashMap<HexLocation, Hex> newHexesMap = parseHexesFromJSON(newHexesJSONArr);
        //HashMap<Hexes> complete! Ready to add to Map obj.

//GET PORTS
        JSONArray newPortsJSONArr = newMapJSON.getJSONArray("ports");
        HashMap<HexLocation, Port> newPortsMap = parsePortsFromJSON(newPortsJSONArr);
        //HashMap<Ports> complete! Ready to add to Map obj.

//GET ROADS
        JSONArray newRoadsJSONArr = newMapJSON.getJSONArray("roads");
        HashMap<EdgeLocation, EdgeValue> newRoadsMap = parseRoadsFromJSON(newRoadsJSONArr);
        //HashMap<Roads> complete! Ready to add to Map obj.

//GET SETTLEMENTS AND CITIES
        JSONArray newStlmtsJSONArr = newMapJSON.getJSONArray("settlements");
        JSONArray newCitiesJSONArr = newMapJSON.getJSONArray("cities");

        //THIS HASHMAP HOLDS BOTH CITIES *AND* SETTLEMENTS!! **********
        HashMap<VertexLocation, VertexObject> newCitiesStlmtsMap = parseCitiesAndStlmtsFromJSON(newStlmtsJSONArr, newCitiesJSONArr);

        //Settlements/Cities Hashmap<> complete! Ready to add to new Map obj.

//GET ROBBER
        //it's just a HexLocation, but the Robber obj type needs a reference to the Map...?
        //maybe add this Robber obj to the Map after the rest of it has been built up
        JSONObject newRobberJSON = newMapJSON.getJSONObject("robber");
        String newRobberJSONString = newRobberJSON.toString();
            //System.out.println("newRobberJSON= " + newRobberJSON);
        int rX = newRobberJSON.getInt("x");
        int rY = newRobberJSON.getInt("y");
        HexLocation newRobberHexLoc = new HexLocation(rX, rY);
        //try building the actual Robber object after building the Map object,
        // so you can pass a ref of the new Map to the new Robber?

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//BUILD NEW MAP OBJECT
        Map newCMMap = new Map(newHexesMap, newPortsMap, newCitiesStlmtsMap, newRoadsMap, newRobberHexLoc);
        newCMMap.setRadius(newCMRadius);

        Robber newRobber = new Robber(newCMMap);

        //Map object is complete (I think)! ready to add to new clientModel obj.
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//GET RESOURCE BANK
        JSONObject newResourceListJSON = newModelJSON.getJSONObject("bank");
        JSONObject newDevCardListJSON = newModelJSON.getJSONObject("deck");
        ResourceBank newCMResourceBank = parseResourceBankFromJSON(newResourceListJSON, newDevCardListJSON);
        //ResourceBank is complete! Ready to add to new ClientModel obj.

//GET PLAYERS ARRAY
        //the ClientModel obj wants these in a Player[].
        JSONArray newPlayersJSONArr = newModelJSON.getJSONArray("players");
        Player[] newPlayersArray = parsePlayersArrFromJSON(newPlayersJSONArr);
        //Player[] is complete! Ready to add to new ClientModel obj.

 //GET MESSAGEMANAGER out of CHAT and LOG
            //GET CHAT
        JSONObject newCMChatJSONObj = newModelJSON.getJSONObject("chat");
        MessageList newChatMsgList = parseMsgListFromJSON(newCMChatJSONObj);
            //GET LOG
        JSONObject newCMLogJSONObj = newModelJSON.getJSONObject("log");
        MessageList newLogMsgList = parseMsgListFromJSON(newCMLogJSONObj);

        //Put the new Chat and Log MsgListObjs into a new MessageManager object:
        MessageManager newCMMsgMgr = new MessageManager();
        newCMMsgMgr.setChat(newChatMsgList);
        newCMMsgMgr.setLog(newLogMsgList);
        //MessageManager is complete! Ready to add to the new ClientModel obj.

//GET TURNTRACKER
        JSONObject newTurnTrackerJSONObj = newModelJSON.getJSONObject("turnTracker");
        String newTTrackerJSONString = newTurnTrackerJSONObj.toString();
        TurnTracker newCMTurnTracker = gsonConverter.fromJson(newTTrackerJSONString, TurnTracker.class);
         //   System.out.println(">newTTrackerObj= " + newCMTurnTracker);
        //TurnTracker is complete! Ready to add to the new ClientModel obj.

//GET TRADE OFFER
        TradeOffer newCMTradeOffer = new TradeOffer();
        if (newModelJSON.has("tradeOffer")){
            JSONObject newTradeOfferJSONObj = newModelJSON.getJSONObject("tradeOffer");
            String newTradeOfferJSONString = newTradeOfferJSONObj.toString();
               // System.out.println("newTradeOfferString= " + newTradeOfferJSONString);
            newCMTradeOffer = gsonConverter.fromJson(newTradeOfferJSONString, TradeOffer.class);
            //    System.out.println(">newTradeOfferObj= " + newCMTradeOffer);
        }

        //TradeOffer is complete! Ready to add to the new ClientModel obj.


//GET ADDITIONAL INTS/OTHER CLIENTMODEL DATA
        int newCMVersion = newModelJSON.getInt("version");
        int newCMWinner = newModelJSON.getInt("winner");

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//BUILD NEW CLIENTMODEL OBJECT
        newClientModel = new ClientModel(0);
        newClientModel.setVersion(newCMVersion);
        newClientModel.setWinner(newCMWinner);
        newClientModel.setResourceBank(newCMResourceBank);
        newClientModel.setMessageManager(newCMMsgMgr);
        newClientModel.setTurnTracker(newCMTurnTracker);
        newClientModel.setChat(newChatMsgList);
        newClientModel.setLog(newLogMsgList);
        newClientModel.setTradeOffer(newCMTradeOffer);
        newClientModel.setPlayers(newPlayersArray);
        newClientModel.setMap(newCMMap);

        System.out.println("===== POLL FINISHED - MODEL TRANSLATION COMPLETE ====");

        return newClientModel;
    }

    //HELPER FUNCTIONS FOR MODEL TRANSLATOR ----------------------------------------------------

    /**
     * helper function for the translateModel process - just a big switch stmt basically
     */
    public HexType exchangeStringForHexType(String hexTypeString){
        switch (hexTypeString){
            case "wood":
                return HexType.WOOD;
            case "brick":
                return HexType.BRICK;
            case "sheep":
                return HexType.SHEEP;
            case "wheat":
                return HexType.WHEAT;
            case "ore":
                return HexType.ORE;
            default:
                return null;
        }
    }

    /**
     * helper function for the translateModel process - just a big switch stmt basically
     */
    public EdgeDirection exchangeStringForEdgeDirection(String edgeDirString){
        switch (edgeDirString){
            case "NW":
                return EdgeDirection.NorthWest;
            case "N":
                return EdgeDirection.North;
            case "NE":
                return EdgeDirection.NorthEast;
            case "SE":
                return EdgeDirection.SouthEast;
            case "S":
                return EdgeDirection.South;
            case "SW":
                return EdgeDirection.SouthWest;
            default:
                return null;
        }
    }

    /**
     * helper function for the translateModel process - just a big switch stmt basically
     */
    public VertexDirection exchangeStringForVertexDirection(String vertexDirString){
        switch (vertexDirString){
            case "W":
                return VertexDirection.West;
            case "NW":
                return VertexDirection.NorthWest;
            case "NE":
                return VertexDirection.NorthEast;
            case "E":
                return VertexDirection.East;
            case "SE":
                return VertexDirection.SouthEast;
            case "SW":
                return VertexDirection.SouthWest;
            default:
                return null;
        }
    }

    /**
     * helper function for the translateModel process - just a big switch stmt basically
     */
    public PortType exchangeStringForPortType(String portTypeString){
        switch (portTypeString){
            case "wood":
                return PortType.WOOD;
            case "brick":
                return PortType.BRICK;
            case "sheep":
                return PortType.SHEEP;
            case "wheat":
                return PortType.WHEAT;
            case "ore":
                return PortType.ORE;
            case "three":
                return PortType.THREE;
            default:
                return null;
        }
    }

    /**
     * Iterates through the JSON array of Hexes and builds Hex objects to put in the Hashmap.
     * @param hexesJSON - the JSONArray containing the Hex info
     * @return
     */
    public HashMap<HexLocation, Hex> parseHexesFromJSON(JSONArray hexesJSON){

        HashMap<HexLocation, Hex> newHexesMap = new HashMap<>();
        for (int h = 0; h < hexesJSON.length(); h++)
        {
            //I'm doing it manually because 1) it freaks out about Hex's toString() for some reason
            // and 2) because I have to convert the hex's TYPE string to a HexType enum value.
            JSONObject currHexStringJSON = hexesJSON.getJSONObject(h);
            JSONObject currHexLocJSON = currHexStringJSON.getJSONObject("location");
            int hlX = currHexLocJSON.getInt("x");
            int hlY = currHexLocJSON.getInt("y");
            HexLocation newHexLoc = new HexLocation(hlX, hlY);

            Hex newHex;
            HexType currHexType;
            int currHexNum = 0;
            //it could be a desert/ocean hex - check if it contains a resource/number:
            if (currHexStringJSON.has("resource")) {
                //if it DOES have a resource, it also has a number, so it's a regular hex:
                String currHexTypeStr = currHexStringJSON.getString("resource");
                currHexType = exchangeStringForHexType(currHexTypeStr.toString());
                currHexNum = currHexStringJSON.getInt("number");
            }
            //I'm commenting this out for now. It looks like the server doesn't hold water hexes
            /*else if (currHexStringJSON.has("number")){
                //if it doesn't have a resource, but DOES have a number, it's a desert hex:
                currHexType = HexType.DESERT;
                currHexNum = currHexStringJSON.getInt("number");
            }*/
            else {
                //if it doesn't have a resource OR a number, it's an ocean hex:
                currHexType = HexType.DESERT;
                currHexNum = 0;
            }
            newHex = new Hex(newHexLoc, currHexType);
            newHex.setNumber(currHexNum);

            newHexesMap.put(newHex.getLocation(), newHex);
        }

        return newHexesMap;
    }

    /**
     * Iterates through the JSON objects for the Bank and Deck and builds a new ResourceBank object.
     * @param bankJSON - the JSONArray containing info for the ResourceList part of ResourceBank
     * @param deckJSON  - the JSONArray containing info for the DevCardList part of ResourceBank
     * @return
     */
    public ResourceBank parseResourceBankFromJSON(JSONObject bankJSON, JSONObject deckJSON){
        String newResListString = bankJSON.toString();
        //the CMJSON has the ResourceList data under key "bank" and DevCardList data under key "deck"
        ResourceList newResourceList = gsonConverter.fromJson(newResListString, ResourceList.class);

        String newDevCardListString = deckJSON.toString();
        DevCardList newDevCardList = gsonConverter.fromJson(newDevCardListString, DevCardList.class);

        //Build ResourceBank out of ResourceList and DevCardList
        ResourceBank newCMResourceBank = new ResourceBank();
        newCMResourceBank.setResourceList(newResourceList);
        newCMResourceBank.setDevCardList(newDevCardList);

        return newCMResourceBank;
    }

    /**
     * Iterates through the JSON array of Ports and builds Port objects to put in the Hashmap.
     * @param portsJSON - the JSONArray containing the Ports info
     * @return
     */
    public HashMap<HexLocation, Port> parsePortsFromJSON(JSONArray portsJSON){
        HashMap<HexLocation, Port> newPortsMap = new HashMap<>();
        //go parse all the ports data in the Ports JSON array:
        for (int p = 0; p < portsJSON.length(); p++) {

            JSONObject currPortStringJSON = portsJSON.getJSONObject(p);
            //get the port's edgeDirection:
            String newPortEdgeDirString = currPortStringJSON.getString("direction");
            EdgeDirection newPortEdgeDir = exchangeStringForEdgeDirection(newPortEdgeDirString);
            //get the port's location (as a HexLocation)
            JSONObject newPortHexLocJSON = currPortStringJSON.getJSONObject("location");
            int nPHLx = newPortHexLocJSON.getInt("x");
            int nPHLy = newPortHexLocJSON.getInt("y");
            HexLocation newPortHexLoc = new HexLocation(nPHLx, nPHLy);

            //determine the port's type:
            //if its ratio is 2, get the resource. if its ratio is 3, set resourcetype to THREE.
            PortType newPortType;
            int newPortRatio = currPortStringJSON.getInt("ratio");
            if (newPortRatio == 2) {
                String newPortTypeString = currPortStringJSON.getString("resource");
                newPortType = exchangeStringForPortType(newPortTypeString);
            }
            else {
                //newPortRatio must be 3, so it's a generic port.
                newPortType = PortType.THREE;
            }
            //Build the new Port obj out of the three parts:
            Port newPort = new Port(newPortType, newPortHexLoc, newPortEdgeDir);

            newPortsMap.put(newPort.getLocation(), newPort);
        }

        return newPortsMap;
    }

    /**
     * Iterates through the JSON array of EdgeValues and builds Road objects to put in the Hashmap.
     * @param roadsJSON - the JSONArray containing the Roads info
     * @return
     */
    public HashMap<EdgeLocation, EdgeValue> parseRoadsFromJSON(JSONArray roadsJSON){
        HashMap<EdgeLocation, EdgeValue> newRoadsMap = new HashMap<>();
        //go parse all the data in the newRoads array:
        for (int r = 0; r < roadsJSON.length(); r++) {

            JSONObject currRoadJSON = roadsJSON.getJSONObject(r);
            //get the HexLocation object out of the currRoadJSON:
            JSONObject currRoadLocJSON = currRoadJSON.getJSONObject("location");
            int rHLx = currRoadLocJSON.getInt("x");
            int rHLy = currRoadLocJSON.getInt("y");
            HexLocation newRoadHexLoc = new HexLocation(rHLx, rHLy);
            //get the EdgeDirection object out of the currRoadJSON:
            EdgeDirection newRoadEdgeDir = exchangeStringForEdgeDirection(currRoadLocJSON.getString("direction"));
            //build the newRoad's EdgeLocation obj out of HexLoc and EdgeDir:
            EdgeLocation newRoadEdgeLoc = new EdgeLocation(newRoadHexLoc, newRoadEdgeDir);
            //get the road's owner:
            int newRoadOwnerIndex = currRoadJSON.getInt("owner");
            //now build a complete EdgeValue obj to represent the new road:
            EdgeValue newRoad = new EdgeValue(newRoadEdgeLoc.getNormalizedLocation());
            newRoad.setOwner(newRoadOwnerIndex);

            newRoadsMap.put(newRoad.getEdgeLocation(), newRoad);
        }

        return newRoadsMap;
    }

    /**
     * Iterates through the JSON array of Players and builds Player objects to put in the Player[].
     * @param playersArrJSON - the JSONArray containing the Players info
     * @return
     */
    public Player[] parsePlayersArrFromJSON(JSONArray playersArrJSON){
        Player[] newPlayersArray = new Player[playersArrJSON.length()]; //there is a max of 4 players per game
        //for loop through newPlayersJSONArr, make a Player obj out of each one, and add it to the Player[]

        for (int p = 0; p < playersArrJSON.length(); p++)
        {
            // check if the players coming in are valid
            if (!playersArrJSON.get(p).equals(null) && playersArrJSON.get(p) != null) {
                JSONObject currPlayerJSON = playersArrJSON.getJSONObject(p);
                String currPlayerJSONStr = currPlayerJSON.toString();
                Player newPlayer = gsonConverter.fromJson(currPlayerJSONStr, Player.class);

                newPlayersArray[p] = newPlayer;
            }
        }

        return newPlayersArray;
    }

    /**
     * Iterates through the JSON array of VertexObjects and builds City/Settlement objects to put in the Hashmap.
     * @param stlmtsJSON - the JSONArray containing the Settlement info
     * @param citiesJSON - the JSONArray containing the Cities info
     * @return
     */
    public HashMap<VertexLocation, VertexObject> parseCitiesAndStlmtsFromJSON(JSONArray stlmtsJSON, JSONArray citiesJSON){

        HashMap<VertexLocation, VertexObject> newCitiesStlmtsMap = new HashMap<>();

        //SETTLEMENTS
        //go parse all the data in the newStlmts array:
        for (int s = 0; s < stlmtsJSON.length(); s++){

            JSONObject currStlmtJSON = stlmtsJSON.getJSONObject(s);
            //get the HexLocation object out of the currStlmtJSON:
            JSONObject currStlmtLocJSON = currStlmtJSON.getJSONObject("location");
            int sHLx = currStlmtLocJSON.getInt("x");
            int sHLy = currStlmtLocJSON.getInt("y");
            HexLocation newStlmtHexLoc = new HexLocation(sHLx, sHLy);
            //get the VertexDirection out of the currStlmtLocJSON:
            VertexDirection newStlmtVtxDir = exchangeStringForVertexDirection(currStlmtLocJSON.getString("direction"));
            //build a VertexLocation obj out of HexLoc and VertexDir:
            VertexLocation newStlmtVtxLoc = new VertexLocation(newStlmtHexLoc, newStlmtVtxDir);
            //get the stlmt owner:
            int newStlmtOwnerIndex = currStlmtJSON.getInt("owner");
            //now build a complete VertexObject to represent the new settlement:
            VertexObject newSettlement = new VertexObject(newStlmtVtxLoc.getNormalizedLocation());
            newSettlement.setOwner(newStlmtOwnerIndex);
            newSettlement.setPieceType(PieceType.SETTLEMENT);  //these are all of Settlement pieceType!

            newCitiesStlmtsMap.put(newSettlement.getVertexLocation().getNormalizedLocation(), newSettlement);
        }

        //CITIES
        for (int c = 0; c < citiesJSON.length(); c++){

            JSONObject currCityJSON = citiesJSON.getJSONObject(c);
            //get the HexLocation object out of the currCityJSON:
            JSONObject currCityLocJSON = currCityJSON.getJSONObject("location");
            int cHLx = currCityLocJSON.getInt("x");
            int cHLy = currCityLocJSON.getInt("y");
            HexLocation newCityHexLoc = new HexLocation(cHLx, cHLy);
            //get the VertexDirection out of the currCityLocJSON:
            VertexDirection newCityVtxDir = exchangeStringForVertexDirection(currCityLocJSON.getString("direction"));
            //build a VertexLocation obj out of HexLoc and VertexDir:
            VertexLocation newCityVtxLoc = new VertexLocation(newCityHexLoc, newCityVtxDir);
            //get the city owner:
            int newCityOwnerIndex = currCityJSON.getInt("owner");
            //now build a complete VertexObject to represent the new city:
            VertexObject newCity = new VertexObject(newCityVtxLoc.getNormalizedLocation());
            newCity.setOwner(newCityOwnerIndex);
            newCity.setPieceType(PieceType.CITY);  //these are all of City pieceType!

            newCitiesStlmtsMap.put(newCity.getVertexLocation().getNormalizedLocation(), newCity);
        }

        return newCitiesStlmtsMap;
    }

    /**
     * helper function for ModelFromJSON - builds the Chat/Log MessageList objects
     */
    public MessageList parseMsgListFromJSON(JSONObject msgListJSON) {
        JSONArray newCMMsgListArr = msgListJSON.getJSONArray("lines");
        String newCMMsgListArrStr = newCMMsgListArr.toString();

        List<MessageLine> newMsgLines = new ArrayList<>();

        //for loop to get all log MessageLines inside newCMMsgListArr, save each one to newMsgLines:
        for (int c = 0; c < newCMMsgListArr.length(); c++)
        {
            JSONObject currMsgLine = newCMMsgListArr.getJSONObject(c);
            String currMsgLineStr = currMsgLine.toString();
            MessageLine newMsgLine = gsonConverter.fromJson(currMsgLineStr, MessageLine.class);
            newMsgLines.add(newMsgLine);
        }

        //Now we have a complete ArrayList of MessageLines, so create a new MsgList obj for the MsgMgr:
        MessageList newMsgList = new MessageList();
        newMsgList.setLines(newMsgLines);

        return newMsgList;
    }

    //------------------------------------------------------------------------------------------------------------

//COMMAND OBJECT TRANSLATORS ==================================================

    //GAME COMMANDS~~~~~~~~~~~~~~~~~~~~~~

    /**
     *
     * @param addAICommandObj
     * @return
     */
    public JSONObject addAICmdToJSON(AddAICommand addAICommandObj) {

        stringResult = gsonConverter.toJson(addAICommandObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     * This is how the server Executes all commands on its own model -
     * Here we are translating the CommandManager's list of all exec'd commands so far
     * ***from CommandObject into JSON*** to post to the server.
     *
     * @param allExecutedCommands - this is the CommandManager's record of all the executed commands so far
     * @return JSONArray representing allExecutedCommands as JSON so the server can read it
     */
    public JSONArray commandsListToJSON(List<BaseCommand> allExecutedCommands) {

        stringResult = gsonConverter.toJson(allExecutedCommands);

        JSONArray jsonArrayResult =  new JSONArray(stringResult);

        return jsonArrayResult;
    }


    /**
     * This translates the server's JSON response after /game/Commands is called.
     * It comes back as a JSONArray of lots of different CommandObjs,
     * so we need a switch statement to tell gson which type of CommandObj to create,
     * and return a list of fully built CommandObjs.
     *
     * @param jsonCommandsList the JSONArray representing all executed commands so far, returned by the server
     * @return an arraylist of Command objects built from the jsonCommandsList JSON
     */
    public List<BaseCommand> commandsListFromJSON(JSONArray jsonCommandsList) {

        //STEPS
        //iterate through all encoded CommandObjs inside the JSONArray,
        //create a temp JSONObject for each one to examine it,
        //grab its TYPE field, put it through the switch stmt,
        //then build/save a CommandObj for it depending on what the switch stmt said.

        ArrayList<BaseCommand> allExecutedCommands = new ArrayList<>();

        for (int c = 0; c < jsonCommandsList.length(); c++)
        {
            //isolate one encoded command object:
            JSONObject currCommandObj = jsonCommandsList.getJSONObject(c);
            String currCommandObjString = currCommandObj.toString();
            //extract its command TYPE field:
            String currCommandObjType = currCommandObj.getString("type");

            //run that through a switch statement to determine which Command Obj to build for it:

            //these cases are in the order from the swagger page fyi
            switch (currCommandObjType) {
                case "sendChat":
                    SendChatCommand newSendChatCmd = gsonConverter.fromJson(currCommandObjString, SendChatCommand.class);
                    allExecutedCommands.add(newSendChatCmd);
                    break;
                case "rollNumber":
                    RollDiceCommand rollDiceCmd = gsonConverter.fromJson(currCommandObjString, RollDiceCommand.class);
                    allExecutedCommands.add(rollDiceCmd);
                    break;
                case "robPlayer":
                    RobPlayerCommand robPlayerCmd = gsonConverter.fromJson(currCommandObjString, RobPlayerCommand.class);
                    allExecutedCommands.add(robPlayerCmd);
                    break;
                case "finishTurn":
                    FinishTurnCommand finishTurnCmd = gsonConverter.fromJson(currCommandObjString, FinishTurnCommand.class);
                    allExecutedCommands.add(finishTurnCmd);
                    break;
                case "buyDevCard":
                    PurchaseDevCardCommand purchaseDevCardCmd = gsonConverter.fromJson(currCommandObjString, PurchaseDevCardCommand.class);
                    allExecutedCommands.add(purchaseDevCardCmd);
                    break;
                case "Year_Of_Plenty":
                    PlayYearOfPlentyCommand playYearOfPlentyCommand = gsonConverter.fromJson(currCommandObjString, PlayYearOfPlentyCommand.class);
                    allExecutedCommands.add(playYearOfPlentyCommand);
                    break;
                case "Road_Building":
                    PlayRoadBuilderCommand playRoadBuildingCommand = gsonConverter.fromJson(currCommandObjString, PlayRoadBuilderCommand.class);
                    allExecutedCommands.add(playRoadBuildingCommand);
                    break;
                case "Soldier":
                    PlaySoldierCommand playSoldierCommand = gsonConverter.fromJson(currCommandObjString, PlaySoldierCommand.class);
                    allExecutedCommands.add(playSoldierCommand);
                    break;
                case "Monopoly":
                    PlayMonopolyCommand playMonopolyCommand = gsonConverter.fromJson(currCommandObjString, PlayMonopolyCommand.class);
                    allExecutedCommands.add(playMonopolyCommand);
                    break;
                case "Monument":
                    PlayMonumentCommand playMonumentCommand = gsonConverter.fromJson(currCommandObjString, PlayMonumentCommand.class);
                    allExecutedCommands.add(playMonumentCommand);
                    break;
                case "buildRoad":
                    BuildRoadCommand buildRoadCommand = gsonConverter.fromJson(currCommandObjString, BuildRoadCommand.class);
                    allExecutedCommands.add(buildRoadCommand);
                    break;
                case "buildSettlement":
                    BuildSettlementCommand buildStlmtCommand = gsonConverter.fromJson(currCommandObjString, BuildSettlementCommand.class);
                    allExecutedCommands.add(buildStlmtCommand);
                    break;
                case "buildCity":
                    BuildCityCommand buildCityCommand = gsonConverter.fromJson(currCommandObjString, BuildCityCommand.class);
                    allExecutedCommands.add(buildCityCommand);
                    break;
                case "offerTrade":
                    OfferTradeCommand offerTradeCommand = gsonConverter.fromJson(currCommandObjString, OfferTradeCommand.class);
                    allExecutedCommands.add(offerTradeCommand);
                    break;
                case "acceptTrade":
                    AcceptTradeCommand acceptTradeCommand = gsonConverter.fromJson(currCommandObjString, AcceptTradeCommand.class);
                    allExecutedCommands.add(acceptTradeCommand);
                    break;
                case "maritimeTrade":
                    MaritimeTradeCommand maritimeTradeCommand = gsonConverter.fromJson(currCommandObjString, MaritimeTradeCommand.class);
                    allExecutedCommands.add(maritimeTradeCommand);
                    break;
                case "discardCards":
                    DiscardCommand discardCommand = gsonConverter.fromJson(currCommandObjString, DiscardCommand.class);
                    allExecutedCommands.add(discardCommand);
                    break;
            }
        }

        return allExecutedCommands;
    }

    /**
     *
     * @param gameCreateCmdObj
     * @return
     */
    public JSONObject gameCreateCmdToJSON(GameCreateCommand gameCreateCmdObj) {

        stringResult = gsonConverter.toJson(gameCreateCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }


    /**
     * When you use a gameCreateCommand on the server, it sends back a JSONObject
     * with data about the game you just created.
     * The response data contains the same data as a GameListItem, but with an empty Player array.
    */
     public GameInfo gameCreateResponseFromJSON(JSONObject gameCreateResponseJSON){

        String gameCreateResponseStr = gameCreateResponseJSON.toString();

        /*
        GameInfo newGameInfo = gsonConverter.fromJson(gameCreateResponseStr, GameInfo.class);
        System.out.println(">>>JSONTRANSLATOR: gameCreateRespFromJSON: newGameInfo = " + newGameInfo.toString());
        System.out.println(">>>newGameInfoPlayersArr size= " + newGameInfo.getPlayers().size());
        */

        //need to make sure that the players array is not counting empty players as spots

        //may do parse manually. trying here:
        GameInfo newGameInfo = new GameInfo();
        //    System.out.println(">>>JSONTRANSLATOR: gameCreateRespFromJSON: gameCreateRespStr= " + gameCreateResponseStr);
        newGameInfo.setTitle(gameCreateResponseJSON.getString("title"));
        newGameInfo.setId(gameCreateResponseJSON.getInt("id"));

        ArrayList<PlayerInfo> tempPIArrayList = new ArrayList<>();
        //we're checking on login that they don't have any blank names, null strings, etc.
        //so we can use the player Name to check if they're a real player or not.
        //only add players to the ArrayList if their name is NOT a null/empty string.
        //this should ensure that each GameInfo object's list of playerInfos only contains real players and not the default null PIs.
        JSONArray newGamePlayerInfosJSONArr = gameCreateResponseJSON.getJSONArray("players");

       // System.out.println("\t ******* picking out real playerInfos:");
        //go through array result from JSON, and add each REAL player to the ArrayList
        for (int p = 0; p < newGamePlayerInfosJSONArr.length(); p++) {
            JSONObject currPlayerInfoJSON = newGamePlayerInfosJSONArr.getJSONObject(p);
            String currPlayerInfoStr = currPlayerInfoJSON.toString();
           //     System.out.println("\t\tCurrPlayerInfo= " + currPlayerInfoStr);

            //check if it's a real player or just blank
            if(!currPlayerInfoStr.equals("{}")){
                //it's ok to make PlayerInfo object, since it's not blank
                PlayerInfo newPlayerInfo = gsonConverter.fromJson(currPlayerInfoStr, PlayerInfo.class);
                tempPIArrayList.add(newPlayerInfo);

             //   System.out.println("\t Player was good, added player: " + newPlayerInfo);
            }
            else{
                //it was blank, so don't add it to the arraylist
             //   System.out.println("\t Player was null, skipping add");
            }
        }

        newGameInfo.setPlayers(tempPIArrayList);

      //  System.out.println("\t *******>FINAL GAMEINFO: " + newGameInfo + ", playersArr size= " + newGameInfo.getPlayers().size());

        return newGameInfo;
    }

    /**
     * Translates the JSONArray response from the server when asked for a list of active games (/games/list)
     * Builds an ArrayList of GameListItems, which each hold an arrayList of Players in each game (GameListPlayerItems)
     * I set it up this way so it would be easy to parse from the JSON, and so it would (hopefully)
     * be easier to access/display in the Join Game view.
     *
     * @param gameCreateResponseJSON
     * @return an array of GameInfo objects
     */
    public GameInfo[] gamesListResponseFromJSON(JSONArray gameCreateResponseJSON){

        ArrayList<GameInfo> allActiveGames = new ArrayList<>();

        for (int g = 0; g < gameCreateResponseJSON.length(); g++)
        {
            JSONObject currGameListItem = gameCreateResponseJSON.getJSONObject(g);
            String currGameListItemString = currGameListItem.toString();
            //GameInfo newGameInfo = gsonConverter.fromJson(currGameListItemString, GameInfo.class);

         //   System.out.println(">>>JSONTRANSLATOR: gamesListRespFromJSON: game " + g + "= " + currGameListItemString);

            //we need to do this manually to remove all null/empty players:
            GameInfo newGameInfo = new GameInfo();

            newGameInfo.setTitle(currGameListItem.getString("title"));
            newGameInfo.setId(currGameListItem.getInt("id"));

            ArrayList<PlayerInfo> tempPIArrayList = new ArrayList<>();
            //we're checking on login that they don't have any blank names, null strings, etc.
            //so we can use the player Name to check if they're a real player or not.
            //only add players to the ArrayList if their name is NOT a null/empty string.
            //this should ensure that each GameInfo object's list of playerInfos only contains real players and not the default null PIs.
            JSONArray currGamePlayerInfosJSONArr = currGameListItem.getJSONArray("players");

           // System.out.println("\t ******* picking out real playerInfos:");
            //go through array result from JSON, and add each REAL player to the ArrayList
            for (int p = 0; p < currGamePlayerInfosJSONArr.length(); p++) {
                JSONObject currPlayerInfoJSON = currGamePlayerInfosJSONArr.getJSONObject(p);
                String currPlayerInfoStr = currPlayerInfoJSON.toString();
             //   System.out.println("\t\tCurrPlayerInfo= " + currPlayerInfoStr);

                //check if it's a real player or just blank
                if(!currPlayerInfoStr.equals("{}")){
                    //it's ok to make PlayerInfo object, since it's not blank
                    PlayerInfo newPlayerInfo = gsonConverter.fromJson(currPlayerInfoStr, PlayerInfo.class);
                    tempPIArrayList.add(newPlayerInfo);

               //     System.out.println("\t Player was good, added player: " + newPlayerInfo);
                }
                else{
                    //it was blank, so don't add it to the arraylist
               //     System.out.println("\t Player was null, skipping add");
                }
            }

            newGameInfo.setPlayers(tempPIArrayList);

          //  System.out.println("\t *******>FINAL GAMEINFO: " + newGameInfo + ", playersArr size= " + newGameInfo.getPlayers().size());


            allActiveGames.add(newGameInfo);
        }

        //makes an array of GameInfos for the JoinGameView, based on the size of allActiveGames:
        GameInfo[] allGameInfos = allActiveGames.toArray(new GameInfo[allActiveGames.size()]);

        return allGameInfos;
    }

    /**
     *
     * @param gameJoinCmdObj
     * @return
     */
    public JSONObject gameJoinCmdToJSON(GameJoinCommand gameJoinCmdObj) {

        stringResult = gsonConverter.toJson(gameJoinCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }
    /**
     *
     * @param gameSaveCmdObj
     * @return
     */
    public JSONObject gameSaveCmdToJSON(GameSaveCommand gameSaveCmdObj) {

        stringResult = gsonConverter.toJson(gameSaveCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param gameLoadCmdObj
     * @return
     */
    public JSONObject gameLoadCmdToJSON(GameLoadCommand gameLoadCmdObj) {

        stringResult = gsonConverter.toJson(gameLoadCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * List AI is just a URL command, it doesn't actually need to send and JSON to the server
     * but the server returns a JSON string array of all available AIs (probably only LARGEST_ARMY)
     * that needs to be translated.
     *
     * Sierra changed this to return a String[] instead of an ArrayList<String>
     *     because PlayerWaitingView
     *
     * @param listAIResponseArr
     * @return a String[] representing all available AIs (should only be one in there)
     */
    public String[] listAIResponseFromJSON(JSONArray listAIResponseArr) {

        //just going to do this manually since I don't know how Gson does arrays of only strings

        //this is the only function I could find that changes an unkeyed JSONArray into usable objects:
        List<Object> allAITypesObjList = listAIResponseArr.toList();
        //I don't want to deal with something as vague as a list<object>, so change it into an arrayList<String>:

        //ArrayList<String> allAITypes = new ArrayList<>();
        String[] allAITypes = new String[50];  //there is no way there will ever be more than 50 ai types

        //iterate through the List<Object> and copy its strings into allAITypes
        for (int a = 0; a < allAITypesObjList.size(); a++)
        {
            String currAITypeString = allAITypesObjList.get(a).toString();
            //allAITypes.add(currAITypeString);
            allAITypes[a] = currAITypeString;
        }

        return allAITypes;
    }

    /**
     *
     * @param loginCmdObj
     * @return
     */
    public JSONObject loginCmdToJSON(LoginCommand loginCmdObj){

        stringResult = gsonConverter.toJson(loginCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param registerCmdObj
     * @return
     */
    public JSONObject registerCmdToJSON(RegisterCommand registerCmdObj) {

        stringResult = gsonConverter.toJson(registerCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *  This will work for both Log messages and Chat messages
     *
     * @param sendChatCmdObj
     * @return
     */
    public JSONObject sendChatCmdToJSON(SendChatCommand sendChatCmdObj) {

        stringResult = gsonConverter.toJson(sendChatCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param utilChangeLogLevelCmdObj
     * @return
     */
    public JSONObject utilChangeLogLevelCmdToJSON(UtilChangeLogLevelCommand utilChangeLogLevelCmdObj) {

        stringResult = gsonConverter.toJson(utilChangeLogLevelCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }



    //GAME COMMANDS~~~~~~~~~~~~~~~~~~~~~~

    /**
     *
     * @param acceptTradeCmd
     * @return
     */
    public JSONObject acceptTradeCmdToJSON(AcceptTradeCommand acceptTradeCmd) {

        stringResult = gsonConverter.toJson(acceptTradeCmd);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param buildCityCmd
     * @return
     */
    public JSONObject buildCityCmdToJSON(BuildCityCommand buildCityCmd) {

        stringResult = gsonConverter.toJson(buildCityCmd);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param buildRoadCmd
     * @return
     */
    public JSONObject buildRoadCmdToJSON(BuildRoadCommand buildRoadCmd) {

        stringResult = gsonConverter.toJson(buildRoadCmd);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param buildSettlementCmd
     * @return
     */
    public JSONObject buildSettlementCmdToJSON(BuildSettlementCommand buildSettlementCmd) {

        stringResult = gsonConverter.toJson(buildSettlementCmd);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param discardCmdObj
     * @return
     */
    public JSONObject discardCmdToJSON(DiscardCommand discardCmdObj) {

        stringResult = gsonConverter.toJson(discardCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param finishTurnCmd
     * @return
     */
    public JSONObject finishTurnCmdToJSON(FinishTurnCommand finishTurnCmd) {

        stringResult = gsonConverter.toJson(finishTurnCmd);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param maritimeTradeCmdObj
     * @return
     */
    public JSONObject maritimeTradeCmdToJSON(MaritimeTradeCommand maritimeTradeCmdObj) {

        stringResult = gsonConverter.toJson(maritimeTradeCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param offerTradeCmdObj
     * @return
     */
    public JSONObject offerTradeCmdToJSON(OfferTradeCommand offerTradeCmdObj) {

        stringResult = gsonConverter.toJson(offerTradeCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param playMonopolyCmdObj
     * @return
     */
    public JSONObject playMonopolyCmdToJSON(PlayMonopolyCommand playMonopolyCmdObj) {

        stringResult = gsonConverter.toJson(playMonopolyCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param playMonumentCmdObj
     * @return
     */
    public JSONObject playMonumentCmdToJSON(PlayMonumentCommand playMonumentCmdObj) {

        stringResult = gsonConverter.toJson(playMonumentCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param playRoadBuilderCmdObj
     * @return
     */
    public JSONObject playRoadBuilderCmdToJSON(PlayRoadBuilderCommand playRoadBuilderCmdObj) {

        stringResult = gsonConverter.toJson(playRoadBuilderCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param playSoldierCmdObj
     * @return
     */
    public JSONObject playSoldierCmdToJSON(PlaySoldierCommand playSoldierCmdObj) {

        stringResult = gsonConverter.toJson(playSoldierCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param playYearOfPlentyCmdObj
     * @return
     */
    public JSONObject playYearOfPlentyCmdToJSON(PlayYearOfPlentyCommand playYearOfPlentyCmdObj) {

        stringResult = gsonConverter.toJson(playYearOfPlentyCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param purchaseDevCardCmdObj
     * @return
     */
    public JSONObject purchaseDevDardCmdToJSON(PurchaseDevCardCommand purchaseDevCardCmdObj) {

        stringResult = gsonConverter.toJson(purchaseDevCardCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param robPlayerCommandCmdObj
     * @return
     */
    public JSONObject robPlayerCmdToJSON(RobPlayerCommand robPlayerCommandCmdObj) {

        stringResult = gsonConverter.toJson(robPlayerCommandCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param rollDiceCmdObj
     * @return
     */
    public JSONObject rollDiceCmdToJSON(RollDiceCommand rollDiceCmdObj) {

        stringResult = gsonConverter.toJson(rollDiceCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }





    //-----------------------------------------------
    //The following commands are sent to server ONLY as URLS - so no JSON translation necessary:


    /**
     *
     * @param gameListCmdObj
     * @return
     */
    /*
    public JSONObject gameListCmdToJSON(GameListCommand gameListCmdObj) {

        stringResult = gsonConverter.toJson(gameListCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }
    */

    /**
     *
     * @param gameResetCmdObj
     * @return
     */
    /*
    public JSONObject gameResetCmdToJSON(GameResetCommand gameResetCmdObj) {

        stringResult = gsonConverter.toJson(gameResetCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }
    */

    /**
     *
     * @param getGameCmdsCmdObj
     * @return
     */
    /*
    public JSONArray getGameCmdsCmdToJSON(GetGameCommandsCommand getGameCmdsCmdObj) {

        stringResult = gsonConverter.toJson(getGameCmdsCmdObj);

        JSONArray jsonArrayResult =  new JSONArray(stringResult);

        return jsonArrayResult;
    }
    */


    /**
     *
     * @param fetchNewModelCmdObj
     * @return
     */
    /*
    public JSONObject fetchNewModelCmdToJSON(FetchNewModelCommand fetchNewModelCmdObj) {

        stringResult = gsonConverter.toJson(fetchNewModelCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    } */


}
