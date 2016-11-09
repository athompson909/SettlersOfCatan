package server;

import client.data.GameInfo;
import shared.model.ClientModel;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;

/**
 * Interface Facade for the Server. Contains both game com
 */
public interface IServerFacade {

    //MOVES COMMANDS

    IServerFacade instance = new MockServerFacade() {};

    static IServerFacade getInstance() {
        return instance;
    }

    /**
     * Finishes the players turn, and changes to the next turn.
     * @param userID of the player ending their turn.
     */
    ClientModel finishTurn(int userID, int gameID, FinishTurnCommand finishTurnObj);

    /**
     * Send a chat message.
     * @param userID of the player sending the message.
     * @param sendChatObj the player wants to display.
     */
    ClientModel sendChat(int userID, int gameID, SendChatCommand sendChatObj);

    /**
     * Discarding cards from rolling a 7
     * @param userID of the player discarding.
     * @param discardObj cards the player has selected to discard.
     */
    ClientModel discardCards(int userID, int gameID, DiscardCommand discardObj);

    /**
     * Roll dice command. Players need to recieve resources according to the passed in number.
     * @param rollDiceObj randomly calculated number.
     */
    ClientModel rollNumber(int userID, int gameID, RollDiceCommand rollDiceObj);

    /**
     * Rob a player.
     * @param userID who is robbing the victim.
     * @param gameID of specific game.
     */
    ClientModel robPlayer(int userID, int gameID, RobPlayerCommand robObj);

    /**
     * Buying a dev card.
     * @param userID of the player buying the card.
     */
    ClientModel purchaseDevCard(int userID, int gameID, PurchaseDevCardCommand purchDevCardObj);

    /**
     * Playing a solider dev card.
     * @param userID of the player using the soldier card.
     */
    ClientModel playSoldier(int userID, int gameID, PlaySoldierCommand soldierObj);

    /**
     * Player using a monument dev card.
     * @param userID of the player using the dev card.
     */
    ClientModel playMonument(int userID, int gameID, PlayMonumentCommand monumentObj);

    /**
     * Player using a Year of plenty Dev Card
     * @param userID player using the card.
     */
    ClientModel playYearOfPlenty(int userID, int gameID, PlayYearOfPlentyCommand yearOfPlentyObj);

    /**
     * Player using a road building dev card.
     * @param userID of player using the card.
     */
    ClientModel playRoadBuilding(int userID, int gameID, PlayRoadBuilderCommand roadBldgCardObj);

    /**
     * Player using a monopoly dev card.
     * @param userID of the player using the card.
     */
    ClientModel playMonopoly(int userID, int gameID, PlayMonopolyCommand monopolyOBj);

    /**
     * Player offering a trade.
     * @param userID of the player offering the trade.
     */
    ClientModel offerTrade(int userID, int gameID, OfferTradeCommand offerTradeObj);

    /**
     * Player choosing whether or not to accept a trade.
     * @param userID of the player choosing.
     */
    ClientModel acceptTrade(int userID, int gameID, AcceptTradeCommand acceptTradeObj);

    /**
     * Maratime Trade Request
     * @param userID of the player trading.
     */
    ClientModel maritimeTrade(int userID, int gameID, MaritimeTradeCommand maritTradeObj);

    /**
     * Player building a new road.
     * @param userID EdgeValue, which contains the player ID and location of the road.
     */
    ClientModel buildRoad(int userID, int gameID, BuildRoadCommand buildRoadObj);

    /**
     * Player building a settlement.
     */
    ClientModel buildSettlement(int userID, int gameID, BuildSettlementCommand buildSettObj);

    /**
     * Player building a city.
     */
    ClientModel buildCity(int userID, int gameID, BuildCityCommand buildCityObj);



    //USER AND GAME COMMANDS

    /**
     * User login.
     * @return true if login is successful.
     */
    boolean login(LoginCommand command);

    /**
     * User registering.
     * @return true if login is successful.
     */
    boolean register(RegisterCommand command);


    /**
     * List all of the games.
     * @return an array of the GameInfo objects used to display the list.
     */
    GameInfo[] list(int userId); //GET

    /**
     * Join a specific game.
     * @return true is succesful.
     */
    boolean join(int userId, GameJoinCommand command);

    /**
     * Create a new game.
     */
    GameInfo create(int userId, int gameId, GameCreateCommand command);

    /**
     * Get the model.
     * @return the model.
     */
    ClientModel model(int userId, int gameId, FetchNewModelCommand command); //GET

    /**
     * Add an AI to the current game.
     * @return true if the AI
     */
    boolean addAI(int userId, int gameId);

    /**
     * List the AI
     * @return a string array of the names of the AI
     */
    String[] listAI(int userId);


    /**
     * this is only for when a user logs in or registers, makes it possible to set the value in the cookie
     * @return the user's id
     */
    int getUserId();

}
