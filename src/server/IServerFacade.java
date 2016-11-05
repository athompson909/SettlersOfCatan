package server;

import client.data.GameInfo;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.ClientModel;
import shared.model.map.EdgeValue;
import shared.model.map.VertexObject;
import shared.model.resourcebank.ResourceList;

/**
 * Interface Facade for the Server. Contains both game com
 */
public interface IServerFacade {

    //MOVES COMMANDS

    IServerFacade instance = new MockServerFacade() {};

    static IServerFacade getInstance() {
        return instance;
    };

    /**
     * Finishes the players turn, and changes to the next turn.
     * @param index of the player ending their turn.
     */
    void finishTurn(int index);

    /**
     * Send a chat message.
     * @param index of the player sending the message.
     * @param message the player wants to display.
     */
    void sendChat(int index, String message);

    /**
     * Discarding cards from rolling a 7
     * @param index of the player discarding.
     * @param discarded cards the player has selected to discard.
     */
    void discardCards(int index, ResourceList discarded);

    /**
     * Roll dice command. Players need to recieve resources according to the passed in number.
     * @param number randomly calculated number.
     */
    void rollNumber(int number);

    /**
     * Rob a player.
     * @param playerIndex who is robbing the victim.
     * @param location that is now being robbed.
     * @param victimIndex who is losing a card. Null if no player can be robbed.
     */
    void robPlayer(int playerIndex, HexLocation location, int victimIndex);

    /**
     * Buying a dev card.
     * @param index of the player buying the card.
     */
    void buyDevCard(int index);

    /**
     * Playing a solider dev card.
     * @param index of the player using the soldier card.
     * @param robberLocation new location being robbed.
     * @param victimIndex being robbed.
     */
    void soldier(int index, HexLocation robberLocation, int victimIndex);

    /**
     * Player using a monument dev card.
     * @param index of the player using the dev card.
     */
    void Monument(int index);

    /**
     * Player using a Year of plenty Dev Card
     * @param playerIndex player using the card.
     * @param resource1 from the bank.
     * @param resource2 from the bank.
     */
    void yearOfPlenty(int playerIndex, ResourceType resource1, ResourceType resource2);

    /**
     * Player using a road building dev card.
     * @param playerIndex of player using the card.
     * @param edgeLocation1 of the first road.
     * @param edgeLocation2 of the second road.
     */
    void roadBuilding(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2);

    /**
     * Player using a monopoly dev card.
     * @param index of the player using the card.
     * @param res the player is monopolizing.
     */
    void monopoly(int index, ResourceType res);

    /**
     * Player offering a trade.
     * @param index of the player offering the trade.
     * @param off resource list offer.
     * @param receiverIndex index of the player receiving the offer
     */
    void offerTrade(int index, ResourceList off, int receiverIndex);

    /**
     * Player choosing whether or not to accept a trade.
     * @param index of the player choosing.
     * @param accept returns true if they accept.
     */
    void acceptTrade(int index, boolean accept);

    /**
     * Maratime Trade Request
     * @param index of the player trading.
     * @param ratio of the trade.
     * @param inputResource to trade.
     * @param outputResource to recieve.
     */
    void martimeTrade(int index, int ratio, ResourceType inputResource, ResourceType outputResource);

    /**
     * Player building a new road.
     * @param newRoad EdgeValue, which contains the player ID and location of the road.
     */
    void buildRoad(EdgeValue newRoad);

    /**
     * Player building a settlement.
     * @param newSettlement VertexObject, which contains the player ID and location of the settlement.
     */
    void buildSettlement(VertexObject newSettlement);

    /**
     * Player building a city.
     * @param newCity VertexObject, which contains the player ID and location of the City.
     */
    void buildCity(VertexObject newCity);



    //USER AND GAME COMMANDS

    /**
     * User login.
     * @param username of the user.
     * @param password of the user.
     * @return true if login is successful.
     */
    boolean login(String username, String password);

    /**
     * User registering.
     * @param username of the user.
     * @param password of the user.
     * @return true if login is successful.
     */
    boolean register(String username, String password);


    /**
     * List all of the games.
     * @return an array of the GameInfo objects used to display the list.
     */
    GameInfo[] list(); //GET

    /**
     * Join a specific game.
     * @param gameID of the game.
     * @param color the player has selected.
     * @return true is succesful.
     */
    boolean join(int gameID, CatanColor color);

    /**
     * Create a new game.
     * @param name of the game.
     * @param randomTiles true if randomized.
     * @param randomNumbers true if randomized.
     * @param randomPorts true if randomized.
     */
    GameInfo create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts);

    /**
     * Get the model.
     * @param version of the model, compared to see if its different.
     * @return the model.
     */
    ClientModel model(int version); //GET

    /**
     * Add an AI to the current game.
     * @return true if the AI
     */
    boolean addAI();

    /**
     * List the AI
     * @return a string array of the names of the AI
     */
    String[] listAI();

}
