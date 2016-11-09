package server;

import client.data.GameInfo;
import client.utils.MockResponses;
import org.json.JSONObject;
import shared.model.ClientModel;
import shared.model.JSONTranslator;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;

/**
 * Created by adamthompson on 11/4/16.
 */
public class MockServerFacade implements IServerFacade {

    //MOVES COMMANDS

    /**
     * Finishes the players turn, and changes to the next turn.
     * @param userID of the player ending their turn.
     */
    @Override
    public ClientModel finishTurn(int userID, int gameID, FinishTurnCommand finishTurnObj){
        return null;
    }

    /**
     * Send a chat message.
     * @param userID of the player sending the message.
     */
    @Override
    public ClientModel sendChat(int userID, int gameID, SendChatCommand sendChatObj){
        ClientModel clientModel = new JSONTranslator().modelFromJSON(new JSONObject(MockResponses.GAME_MODEL));
        clientModel.sendChat(sendChatObj.getPlayerIndex(), sendChatObj.getContent());
        return clientModel;
    }

    /**
     * Discarding cards from rolling a 7
     * @param userID of the player discarding.
     * @param discardObj cards the player has selected to discard.
     */
    @Override
    public ClientModel discardCards(int userID, int gameID, DiscardCommand discardObj){
        return null;
    }

    /**
     * Roll dice command. Players need to recieve resources according to the passed in number.
     * @param rollDiceObj randomly calculated number.
     */
    @Override
    public ClientModel rollNumber(int userID, int gameID, RollDiceCommand rollDiceObj){
        ClientModel clientModel = new JSONTranslator().modelFromJSON(new JSONObject(MockResponses.GAME_MODEL));
//        clientModel.receiveResourcesFromDiceRoll(rollDiceObj.getNumber());
        return clientModel;
    }

    /**
     * Rob a player.
     * @param userID who is robbing the victim.
     * @param gameID of specific game.
     */
    @Override
    public ClientModel robPlayer(int userID, int gameID, RobPlayerCommand robObj){
        ClientModel clientModel = new JSONTranslator().modelFromJSON(new JSONObject(MockResponses.GAME_MODEL));
        return clientModel;
    }

    /**
     * Buying a dev card.
     * @param userID of the player buying the card.
     */
    @Override
    public ClientModel purchaseDevCard(int userID, int gameID, PurchaseDevCardCommand purchDevCardObj){
        return null;
    }

    /**
     * Playing a solider dev card.
     * @param userID of the player using the soldier card.
     */
    @Override
    public ClientModel playSoldier(int userID, int gameID, PlaySoldierCommand soldierObj){
        return null;
    }

    /**
     * Player using a monument dev card.
     * @param userID of the player using the dev card.
     */
    @Override
    public ClientModel playMonument(int userID, int gameID, PlayMonumentCommand monumentObj) {
        return null;
    }

    /**
     * Player using a Year of plenty Dev Card
     * @param userID player using the card.
     */
    @Override
    public ClientModel playYearOfPlenty(int userID, int gameID, PlayYearOfPlentyCommand yearOfPlentyObj) {
        return null;
    }

    /**
     * Player using a road building dev card.
     * @param userID of player using the card.
     */
    @Override
    public ClientModel playRoadBuilding(int userID, int gameID, PlayRoadBuilderCommand roadBldgCardObj) {
        return null;
    }

    /**
     * Player using a monopoly dev card.
     * @param userID of the player using the card.
     */
    @Override
    public ClientModel playMonopoly(int userID, int gameID, PlayMonopolyCommand monopolyOBj) {
        return null;
    }

    /**
     * Player offering a trade.
     * @param userID of the player offering the trade.
     */
    @Override
    public ClientModel offerTrade(int userID, int gameID, OfferTradeCommand offerTradeObj){
        return null;
    }

    /**
     * Player choosing whether or not to accept a trade.
     * @param userID of the player choosing.
     */
    @Override
    public ClientModel acceptTrade(int userID, int gameID, AcceptTradeCommand acceptTradeObj) {
        return null;
    }

    /**
     * Maratime Trade Request
     * @param userID of the player trading.
     */
    @Override
    public ClientModel maritimeTrade(int userID, int gameID, MaritimeTradeCommand maritTradeObj){
        return null;
    }

    /**
     * Player building a new road.
     * @param userID EdgeValue, which contains the player ID and location of the road.
     */
    @Override
    public ClientModel buildRoad(int userID, int gameID, BuildRoadCommand buildRoadObj){
        return null;
    }

    /**
     * Player building a settlement.
     */
    @Override
    public ClientModel buildSettlement(int userID, int gameID, BuildSettlementCommand buildSettObj) {
        return null;
    }

    /**
     * Player building a city.
     */
    @Override
    public ClientModel buildCity(int userID, int gameID, BuildCityCommand buildCityObj){
        return null;
    }


    //USER AND GAME COMMANDS

    /**
     * User login.
     * @return true if login is successful.
     */
    @Override
    public boolean login(LoginCommand command){
        return true;
    }

    /**
     * User registering.
     * @return true if login is successful.
     */
    @Override
    public boolean register(RegisterCommand command){
        return true;
    }


    /**
     * List all of the games.
     * @return an array of the GameInfo objects used to display the list.
     */
    @Override
    public GameInfo[] list(int userId){
        return null;
    } //GET

    /**
     * Join a specific game.
     * @return true is succesful.
     */
    @Override
    public boolean join(int userId, GameJoinCommand command){
        return true;
    }

    /**
     * Create a new game.
     */
    @Override
    public GameInfo create(int userId, int gameId, GameCreateCommand command){
        return null;
    }

    /**
     * Get the model.
     * @return the model.
     */
    @Override
    public ClientModel model(int userId, int gameId, FetchNewModelCommand command){
        return new JSONTranslator().modelFromJSON(new JSONObject(MockResponses.GAME_MODEL));
    } //GET

    /**
     * Add an AI to the current game.
     * @return true if the AI
     */
    @Override
    public boolean addAI(int userId, int gameId){
        return true;
    }

    /**
     * List the AI
     * @return a string array of the names of the AI
     */
    @Override
    public String[] listAI(){
        return LIST_AI;
    }

    private final String[] LIST_AI = { "LARGEST_ARMY" };

    @Override
    public int getUserId() {
        return 0;
    }

    @Override
    public int getGameId() {
        return 3;
    }
}
