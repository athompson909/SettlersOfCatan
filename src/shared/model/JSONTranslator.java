package shared.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.tools.internal.ws.processor.model.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.BuildCityManager;
import shared.model.map.BuildSettlementManager;
import shared.model.map.Hex;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
     * todo parameter in constructor is hardcoded
     */
    private ClientModel newClientModel = new ClientModel(0);

    /**
     * This Gson object can be reused as many times as you want
     * we're just using it to access its translating functions
     */
    private Gson gsonConverter = new Gson();

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
     * TranslateModel() can take in either a string or a JSON,
     * and uses Gson to convert it to normal objects to send to UpdateManager.
     * But first convert the incoming JSONObject to a String so Gson is happy!
     *
     * @throws JsonSyntaxException - this is Gson's exception
     * @param newModelJSON - this is the huge JSON string coming back directly from the server
     * @return newClientModel - the new ClientModel object created from the server's response JSON
     */
    public ClientModel modelFromJSON(JSONObject newModelJSON) throws JsonSyntaxException {

        //Break up ClientModel pieces and build a new ClientModel object manually:

        String modelJSONString = newModelJSON.toString();

        //TODO: the map is the last part to do!

//GET MAP
        /*
        JSONObject newCMMap = newModelJSON.getJSONObject("map");

        //GET RADIUS
        int newCMRadius = newCMMap.getInt("radius");
            //GET HEXES

        JSONArray newCMHexes = newCMMap.getJSONArray("hexes");
        // System.out.println("\t HEXES TEST= " + newCMTestHexes.length() + "  " + newCMTestHexes);
        ArrayList<Hex> parsedHexes = new ArrayList<Hex>();

        for (int h = 0; h < newCMHexes.length(); h++)
        {
            String tempHexString = newCMHexes.get(h).toString();
            System.out.println(">tempHexString = " + tempHexString);
            Hex testHex = gsonConverter.fromJson(tempHexString, Hex.class);
            System.out.println("\t testHex" + h + "= " + testHex.toString());

            parsedHexes.add(testHex);
        }
            //GET ROADS
            //GET CITIES
            //GET SETTLEMENTS
            //GET PORTS
            //GET ROBBER
            */

//GET RESOURCE BANK
        //GET RESOURCELIST
        JSONObject newResourceListJSON = newModelJSON.getJSONObject("bank");
        String newResListString = newResourceListJSON.toString();
            System.out.println(">newResListStr= " + newResListString);
        //the CMJSON has the ResourceList data under key "bank" and DevCardList data under key "deck"
        ResourceList newResourceList = gsonConverter.fromJson(newResListString, ResourceList.class);
            System.out.println(">newResourceList (for ResBank obj)= " + newResourceList);

        //GET DEVCARDLIST
        JSONObject newDevCardListJSON = newModelJSON.getJSONObject("deck");
        String newDevCardListString = newDevCardListJSON.toString();
            System.out.println(">newDevCardListStr= " + newDevCardListString);
        DevCardList newDevCardList = gsonConverter.fromJson(newDevCardListString, DevCardList.class);
            System.out.println(">newDevCardList (for ResBank obj)= " + newDevCardList);

        //Build ResourceBank out of ResourceList and DevCardList
        ResourceBank newResourceBank = new ResourceBank();
        newResourceBank.setResourceList(newResourceList);

        //ResourceBank is complete! Ready to add to new ClientModel obj.

//GET PLAYERS ARRAY
        //the ClientModel obj wants these in a Player[].
        JSONArray newPlayersJSONArr = newModelJSON.getJSONArray("players");
        Player[] newPlayersArray = new Player[4]; //there is a max of 4 players per game
        //for loop through newPlayersJSONArr, make a Player obj out of each one, and add it to the Player[]

        for (int p = 0; p < newPlayersJSONArr.length(); p++)
        {
            //realistically I don't think the model will ever need to be parsed without 4 players added,
            // but just to be sure/for testing purposes:
            if (newPlayersJSONArr.get(p) != null) {
                JSONObject currPlayerJSON = newPlayersJSONArr.getJSONObject(p);
                String currPlayerJSONSTr = currPlayerJSON.toString();
                System.out.println(">currPlayerJSON= " + currPlayerJSONSTr);
                Player newPlayer = gsonConverter.fromJson(currPlayerJSONSTr, Player.class);

                newPlayersArray[p] = newPlayer;
            }
        }
        //Player[] is complete! Ready to add to new ClientModel obj.

 //GET MESSAGEMANAGER out of CHAT and LOG
            //GET CHAT
        JSONObject newCMChatJSONObj = newModelJSON.getJSONObject("chat");
        MessageList newChatMsgList = parseMsgListFromJSON(newCMChatJSONObj);
            //GET LOG
        JSONObject newCMLogJSONObj = newModelJSON.getJSONObject("log");
        MessageList newLogMsgList = parseMsgListFromJSON(newCMLogJSONObj);

        //Put the new Chat and Log MsgListObjs into a new MessageManager object:
        MessageManager newMsgMgr = new MessageManager();
        newMsgMgr.setChat(newChatMsgList);
        newMsgMgr.setLog(newLogMsgList);

        //MessageManager is complete! Ready to add to the new ClientModel obj.

//GET TURNTRACKER
        JSONObject newTurnTrackerJSONObj = newModelJSON.getJSONObject("turnTracker");
        String newTTrackerJSONString = newTurnTrackerJSONObj.toString();
        TurnTracker newTurnTracker = gsonConverter.fromJson(newTTrackerJSONString, TurnTracker.class);
            System.out.println(">newTTrackerObj= " + newTurnTracker);

        //TurnTracker is complete! Ready to add to the new ClientModel obj.

//GET TRADE OFFER
        if (newModelJSON.has("tradeOffer")){
            JSONObject newTradeOfferJSONObj = newModelJSON.getJSONObject("tradeOffer");
            String newTradeOfferJSONString = newTradeOfferJSONObj.toString();
                System.out.println("newTradeOfferString= " + newTradeOfferJSONString);
            TradeOffer newTradeOffer = gsonConverter.fromJson(newTradeOfferJSONString, TradeOffer.class);
                System.out.println(">newTradeOfferObj= " + newTradeOffer);
            //TradeOffer is complete! Ready to add to the new ClientModel obj.
        }
        else{
            System.out.println(">No TradeOffer found in newClientModel JSON");
        }

//GET ADDITIONAL INTS/OTHER CLIENTMODEL DATA
        int newCMVersion = newModelJSON.getInt("version");
        int newCMWinner = newModelJSON.getInt("winner");
        //get gameNumber? what is this for again?

        //Not in JSON: ClientUpdateManager **

        return newClientModel;
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
            System.out.println("\t>newMsgLineObj= " + newMsgLine);
            newMsgLines.add(newMsgLine);
        }

        System.out.println("newMsgLines size= " + newMsgLines.size());
        //Now we have a complete ArrayList of MessageLines, so create a new MsgList obj for the MsgMgr:
        MessageList newMsgList = new MessageList();
        newMsgList.setLines(newMsgLines);

        return newMsgList;
    }



    /**
     * I don't think this is necessary
     * @param num
     * @return
     */
    public JSONObject modelVerNumToJSON(int num) {
        return null;
    }



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
           // System.out.println(">FORLOOP: currCOType= " + currCommandObjType);

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
            //System.out.println("\n allExecutedCommands new size= " + allExecutedCommands.size());
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


    //When you use a gameCreateCommand on the server, it sends back a JSONObject
    //with data about the game you just created.
    //The response data contains the same data as a GameListItem, but with an empty Player array.
    // I don't actually know where this data is going to be used
    public GameListItem gameCreateResponseFromJSON(JSONObject gameCreateResponse){
        //For now I'm saving the new game data as a TreeMap (although it might be better to have
        //some sort of encapsualating object to hold this new data, since it includes 3 bools and 1 string)

        TreeMap<String, String> gameCreateResponseData = new TreeMap<>();
        String gameCreateResponseStr = gameCreateResponse.toString();

        GameListItem newGameInfo = gsonConverter.fromJson(gameCreateResponseStr, GameListItem.class);

       // System.out.println("newGameListItem= " + newGameInfo.getTitle() + ", " + newGameInfo.getGameID());

        return newGameInfo;
    }

    /**
     * Translates the JSONArray response from the server when asked for a list of active games (/games/list)
     * Builds an ArrayList of GameListItems, which each hold an arrayList of Players in each game (GameListPlayerItems)
     * I set it up this way so it would be easy to parse from the JSON, and so it would (hopefully)
     * be easier to access/display in the Join Game view.
     *
     * @param gameCreateResponseJSON
     * @return
     */
    public ArrayList<GameListItem> gamesListResponseFromJSON(JSONArray gameCreateResponseJSON){

        ArrayList<GameListItem> allActiveGames = new ArrayList<>();

        for (int g = 0; g < gameCreateResponseJSON.length(); g++)
        {
            JSONObject currGameListItem = gameCreateResponseJSON.getJSONObject(g);
            String currGameListItemString = currGameListItem.toString();
          //  System.out.println(">currGLItemString = " + currGameListItemString);

            GameListItem newGLItem = gsonConverter.fromJson(currGameListItemString, GameListItem.class);

            allActiveGames.add(newGLItem);
        }

        return allActiveGames;
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
     * @param listAIResponseArr
     * @return an arraylist of Strings representing all available AIs (should only be one)
     */
    public ArrayList<String> listAIResponseFromJSON(JSONArray listAIResponseArr) {

        //just going to do this manually since I don't know how Gson does arrays of only strings

        //System.out.println(">ListAIResponseArr size= " + listAIResponseArr.length());

        //this is the only function I could find that changes an unkeyed JSONArray into usable objects:
        List<Object> allAITypesObjList = listAIResponseArr.toList();
        //I don't want to deal with something as vague as a list<object>, so change it into an arrayList<String>:

        ArrayList<String> allAITypes = new ArrayList<>();

        //iterate through the List<Object> and copy its strings into allAITypes
        for (int a = 0; a < allAITypesObjList.size(); a++)
        {
            String currAITypeString = allAITypesObjList.get(a).toString();
            allAITypes.add(currAITypeString);
        }

        System.out.println(">allAITypes size= " + allAITypes.size());

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
