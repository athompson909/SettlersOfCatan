package server;

import client.data.GameInfo;
import shared.model.ClientModel;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;

/**
 * Created by adamthompson on 11/4/16.
 */
public class ServerFacade implements IServerFacade {

    public boolean validateParams(int userID, int gameID) {
        Game game = GamesManager.getInstance().getGame(gameID);
        User user = UserManager.getInstance().getUser(userID);
        if(game != null && user != null) {
            return true;
        }
        return false;
    }

    //MOVES COMMANDS

    /**
     * Finishes the players turn, and changes to the next turn.
     * @param userID of the player ending their turn.
     */
    public ClientModel finishTurn(int userID, int gameID, FinishTurnCommand finishTurnObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = finishTurnObj.getPlayerIndex();

            ClientModel model = game.finishTurn(playerIndex);
            return model;
        }
        return null;
    }

    /**
     * Send a chat message.
     * @param userID of the player sending the message.
     */
    public ClientModel sendChat(int userID, int gameID, SendChatCommand sendChatObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = sendChatObj.getPlayerIndex();
            String message = sendChatObj.getContent();

            ClientModel model = game.sendChat(playerIndex, message);
            return model;
        }
        return null;
    }

    /**
     * Discarding cards from rolling a 7
     * @param userID of the player discarding.
     * @param discardObj cards the player has selected to discard.
     */
    public ClientModel discardCards(int userID, int gameID, DiscardCommand discardObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = discardObj.getPlayerIndex();
            ResourceList resList = discardObj.getDiscardedCards();

            ClientModel model = game.discardCards(playerIndex, resList);
            return model;
        }
        return null;
    }

    /**
     * Roll dice command. Players need to recieve resources according to the passed in number.
     * @param rollDiceObj randomly calculated number.
     */
    public ClientModel rollNumber(int userID, int gameID, RollDiceCommand rollDiceObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int diceRoll = rollDiceObj.getNumber();

            ClientModel model = game.rollNumber(diceRoll);
            return model;
        }
        return null;
    }

    /**
     * Rob a player.
     * @param userID who is robbing the victim.
     * @param gameID of specific game.
     */
    public ClientModel robPlayer(int userID, int gameID, RobPlayerCommand robObj){
        if(validateParams(userID, gameID)) {

            Game game =
        }
        return null;
    }

    /**
     * Buying a dev card.
     * @param userID of the player buying the card.
     */
    public ClientModel purchaseDevCard(int userID, int gameID, PurchaseDevCardCommand purchDevCardObj){
        return null;
    }

    /**
     * Playing a solider dev card.
     * @param userID of the player using the soldier card.
     */
    public ClientModel playSoldier(int userID, int gameID, PlaySoldierCommand soldierObj){
        return null;
    }

    /**
     * Player using a monument dev card.
     * @param userID of the player using the dev card.
     */
    public ClientModel playMonument(int userID, int gameID, PlayMonumentCommand monumentObj) {
        return null;
    }

    /**
     * Player using a Year of plenty Dev Card
     * @param userID player using the card.
     */
    public ClientModel playYearOfPlenty(int userID, int gameID, PlayYearOfPlentyCommand yearOfPlentyObj) {
        return null;
    }

    /**
     * Player using a road building dev card.
     * @param userID of player using the card.
     */
    public ClientModel playRoadBuilding(int userID, int gameID, PlayRoadBuilderCommand roadBldgCardObj) {
        return null;
    }

    /**
     * Player using a monopoly dev card.
     * @param userID of the player using the card.
     */
    public ClientModel playMonopoly(int userID, int gameID, PlayMonopolyCommand monopolyOBj) {
        return null;
    }

    /**
     * Player offering a trade.
     * @param userID of the player offering the trade.
     */
    public ClientModel offerTrade(int userID, int gameID, OfferTradeCommand offerTradeObj){
        return null;
    }

    /**
     * Player choosing whether or not to accept a trade.
     * @param userID of the player choosing.
     */
    public ClientModel acceptTrade(int userID, int gameID, AcceptTradeCommand acceptTradeObj) {
        return null;
    }

    /**
     * Maratime Trade Request
     * @param userID of the player trading.
     */
    public ClientModel maritimeTrade(int userID, int gameID, MaritimeTradeCommand maritTradeObj){
        return null;
    }

    /**
     * Player building a new road.
     * @param userID EdgeValue, which contains the player ID and location of the road.
     */
    public ClientModel buildRoad(int userID, int gameID, BuildRoadCommand buildRoadObj){
        return null;
    }

    /**
     * Player building a settlement.
     */
    public ClientModel buildSettlement(int userID, int gameID, BuildSettlementCommand buildSettObj) {
        return null;
    }

    /**
     * Player building a city.
     */
    public ClientModel buildCity(int userID, int gameID, BuildCityCommand buildCityObj){
        return null;
    }


    //USER AND GAME COMMANDS

    /**
     * User login.
     * @param command - LoginCommand containing needed login info
     * @return true if login is successful.
     */
    public boolean login(LoginCommand command){
        //todo - we decided this would ask for the user and then check if the user is logged in
        //SAVE THE USERID FROM THE MODEL HERE TO SEND BACK FOR ADAM
        //return (UserManager.getInstance().isValidLogin(username, password));
        return false;
    }

    /**
     * User registering.
     * @param command - RegisterCommand containing needed register info
     * @return true if login is successful.
     */
    public boolean register(RegisterCommand command){
        return false;
    }


    /**
     * List all of the games.
     * @return an array of the GameInfo objects used to display the list.
     */
    public GameInfo[] list(int userId){
        return null;
    } //GET

    /**
     * Join a specific game.
     * @param command
     * @return true is succesful.
     */
    public boolean join(int userId, GameJoinCommand command){
        return false;
    }

    /**
     * Create a new game.
     */
    public GameInfo create(int userId, GameCreateCommand command){
        return null;
    }

    /**
     * Get the model.
     * @return the model.
     */
    public ClientModel model(int userId, int gameId, FetchNewModelCommand command){
        return null;
    } //GET

    /**
     * Add an AI to the current game.
     * @return true if the AI
     */
    public boolean addAI(int userId, int gameId){
        return false;
    }

    /**
     * List the AI
     * @return a string array of the names of the AI
     */
    public String[] listAI(int userId){
        return null;
    }

    @Override
    public int getUserId() {
        return 0;
    }
}
