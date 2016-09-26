package shared.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONObject;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;

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

        String modelJSONString = newModelJSON.toString();

        //this fromJson() takes in a JSON string and the template class, and returns a complete ClientModel object
        newClientModel = gsonConverter.fromJson(modelJSONString, ClientModel.class);

        return newClientModel;
    }

    /**
     *
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
     *
     * @param execGameCmdsCommandObj
     * @return
     */
    public JSONObject execGameCmdsCmdToJSON(ExecuteGameCommandsCommand execGameCmdsCommandObj) {

        stringResult = gsonConverter.toJson(execGameCmdsCommandObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param fetchNewModelCmdObj
     * @return
     */
    public JSONObject fetchNewModelCmdToJSON(FetchNewModelCommand fetchNewModelCmdObj) {

        stringResult = gsonConverter.toJson(fetchNewModelCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
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
     * @param gameListCmdObj
     * @return
     */
    public JSONObject gameListCmdToJSON(GameListCommand gameListCmdObj) {

        stringResult = gsonConverter.toJson(gameListCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param gameResetCmdObj
     * @return
     */
    public JSONObject gameResetCmdToJSON(GameResetCommand gameResetCmdObj) {

        stringResult = gsonConverter.toJson(gameResetCmdObj);

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
     * @param getGameCmdsCmdObj
     * @return
     */
    public JSONObject getGameCmdsCmdToJSON(GetGameCommandsCommand getGameCmdsCmdObj) {

        stringResult = gsonConverter.toJson(getGameCmdsCmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
    }

    /**
     *
     * @param listAICmdObj
     * @return
     */
    public JSONObject listAICmdToJSON(ListAICommand listAICmdObj) {

        stringResult = gsonConverter.toJson(listAICmdObj);

        jsonObjectResult =  new JSONObject(stringResult);

        return jsonObjectResult;
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
     * @param endTurnCmd
     * @return
     */
    public JSONObject endTurnCmdToJSON(EndTurnCommand endTurnCmd) {

        stringResult = gsonConverter.toJson(endTurnCmd);

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

}
