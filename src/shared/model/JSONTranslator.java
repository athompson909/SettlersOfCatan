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


        return null;
    }

    /**
     *
     * @param execGameCmdsCommandObj
     * @return
     */
    public JSONObject execGameCmdsCmdToJSON(ExecuteGameCommandsCommand execGameCmdsCommandObj) {

        return null;
    }

    /**
     *
     * @param fetchNewModelCmdObj
     * @return
     */
    public JSONObject fetchNewModelCmdToJSON(FetchNewModelCommand fetchNewModelCmdObj) {

        return null;
    }

    /**
     *
     * @param gameCreateCmdObj
     * @return
     */
    public JSONObject gameCreateCmdToJSON(GameCreateCommand gameCreateCmdObj) {

        return null;
    }

    /**
     *
     * @param gameJoinCmdObj
     * @return
     */
    public JSONObject gameJoinCmdToJSON(GameJoinCommand gameJoinCmdObj) {

        return null;
    }

    /**
     *
     * @param gameListCmdObj
     * @return
     */
    public JSONObject gameListCmdToJSON(GameListCommand gameListCmdObj) {

        return null;
    }

    /**
     *
     * @param gameResetCmdObj
     * @return
     */
    public JSONObject gameResetCmdToJSON(GameResetCommand gameResetCmdObj) {

        return null;
    }

    /**
     *
     * @param gameSaveCmdObj
     * @return
     */
    public JSONObject gameSaveCmdToJSON(GameSaveCommand gameSaveCmdObj) {

        return null;
    }

    /**
     *
     * @param getGameCmdsCmdObj
     * @return
     */
    public JSONObject getGameCmdsCmdToJSON(GetGameCommandsCommand getGameCmdsCmdObj) {

        return null;
    }

    /**
     *
     * @param listAICmdObj
     * @return
     */
    public JSONObject listAICmdToJSON(ListAICommand listAICmdObj) {

        return null;
    }

    /**
     *
     * @param loginCmdObj
     * @return
     */
    public JSONObject loginCmdToJSON(LoginCommand loginCmdObj){

        return null;
    }

    /**
     *
     * @param registerCmdObj
     * @return
     */
    public JSONObject registerCmdToJSON(RegisterCommand registerCmdObj) {

        return null;
    }

    /**
     *  This will work for both Log messages and Chat messages
     *
     * @param sendChatCmdObj
     * @return
     */
    public JSONObject sendChatCmdToJSON(SendChatCommand sendChatCmdObj) {

        return null;
    }

    /**
     *
     * @param utilChangeLogLevelCmdObj
     * @return
     */
    public JSONObject utilChangeLogLevelCmdToJSON(UtilChangeLogLevelCommand utilChangeLogLevelCmdObj) {

        return null;
    }



    //GAME COMMANDS~~~~~~~~~~~~~~~~~~~~~~

    /**
     *
     * @param acceptTradeCmd
     * @return
     */
    public JSONObject acceptTradeCmdToJSON(AcceptTradeCommand acceptTradeCmd) {

        return null;
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

        return null;
    }

    /**
     *
     * @param endTurnCmd
     * @return
     */
    public JSONObject endTurnCmdToJSON(EndTurnCommand endTurnCmd) {

        return null;
    }

    /**
     *
     * @param maritimeTradeCmdObj
     * @return
     */
    public JSONObject maritimeTradeCmdToJSON(MaritimeTradeCommand maritimeTradeCmdObj) {

        return null;
    }

    /**
     *
     * @param offerTradeCmdObj
     * @return
     */
    public JSONObject offerTradeCmdToJSON(OfferTradeCommand offerTradeCmdObj) {

        return null;
    }

    /**
     *
     * @param playMonopolyCmdObj
     * @return
     */
    public JSONObject playMonopolyCmdToJSON(PlayMonopolyCommand playMonopolyCmdObj) {

        return null;
    }

    /**
     *
     * @param playMonumentCmdObj
     * @return
     */
    public JSONObject playMonumentCmdToJSON(PlayMonumentCommand playMonumentCmdObj) {

        return null;
    }

    /**
     *
     * @param playRoadBuilderCmdObj
     * @return
     */
    public JSONObject playRoadBuilderCmdToJSON(PlayRoadBuilderCommand playRoadBuilderCmdObj) {

        return null;
    }

    /**
     *
     * @param playSoldierCmdObj
     * @return
     */
    public JSONObject playSoldierCmdToJSON(PlaySoldierCommand playSoldierCmdObj) {

        return null;
    }

    /**
     *
     * @param playYearOfPlentyCmdObj
     * @return
     */
    public JSONObject playYearOfPlentyCmdToJSON(PlayYearOfPlentyCommand playYearOfPlentyCmdObj) {

        return null;
    }

    /**
     *
     * @param purchaseDevCardCmdObj
     * @return
     */
    public JSONObject purchaseDevDardCmdToJSON(PurchaseDevCardCommand purchaseDevCardCmdObj) {

        return null;
    }

    /**
     *
     * @param robPlayerCommandCmdObj
     * @return
     */
    public JSONObject robPlayerCmdToJSON(RobPlayerCommand robPlayerCommandCmdObj) {

        return null;
    }

    /**
     *
     * @param rollDiceCmdObj
     * @return
     */
    public JSONObject rollDiceCmdToJSON(RollDiceCommand rollDiceCmdObj) {

        return null;
    }

}
