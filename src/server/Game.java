package server;

import client.Client;
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
 * Created by adamthompson on 11/4/16.
 */
public class Game {

    /**
     * Game info object for the current game.
     */
    private GameInfo gameInfo;

    /**
     * Client model for the game.
     */
    private ClientModel clientModel;



    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    //MOVES COMMANDS

    /**
     * Finishes the players turn, and changes to the next turn.
     * @param index of the player ending their turn.
     */
    public ClientModel finishTurn(int index){
        clientModel.finishTurn(index);
        return clientModel;
    }

    /**
     * Send a chat message.
     * @param index of the player sending the message.
     * @param message the player wants to display.
     */
    public ClientModel sendChat(int index, String message){
        clientModel.sendChat(index, message);
        return clientModel;
    }

    /**
     * Discarding cards from rolling a 7
     * @param index of the player discarding.
     * @param discarded cards the player has selected to discard.
     */
    public ClientModel discardCards(int index, ResourceList discarded){
        clientModel.discardCards(index, discarded);
        return clientModel;
    }

    /**
     * Roll dice command. Players need to recieve resources according to the passed in number.
     * @param number randomly calculated number.
     */
    public ClientModel rollNumber(int number){
        clientModel.receiveResourcesFromDiceRoll(number);
        return clientModel;
    }

    /**
     * Rob a player.
     * @param playerIndex who is robbing the victim.
     * @param location that is now being robbed.
     * @param victimIndex who is losing a card. Null if no player can be robbed.
     */
    public ClientModel robPlayer(int playerIndex, HexLocation location, int victimIndex){
        clientModel.placeRobber(playerIndex, location, victimIndex);
        return clientModel;
    }

    /**
     * Buying a dev card.
     * @param index of the player buying the card.
     */
    public ClientModel buyDevCard(int index){
        clientModel.purchaseDevCard(index);
        return clientModel;
    }

    /**
     * Playing a solider dev card.
     * @param index of the player using the soldier card.
     * @param robberLocation new location being robbed.
     * @param victimIndex being robbed.
     */
    public ClientModel soldier(int index, HexLocation robberLocation, int victimIndex){
        clientModel.playSoldierCard(index, robberLocation, victimIndex);
        return clientModel;
    }

    /**
     * Player using a monument dev card.
     * @param index of the player using the dev card.
     */
    public ClientModel Monument(int index){
        clientModel.playMonumentCard(index);
        return clientModel;
    }

    /**
     * Player using a Year of plenty Dev Card
     * @param playerIndex player using the card.
     * @param resource1 from the bank.
     * @param resource2 from the bank.
     */
    public ClientModel yearOfPlenty(int playerIndex, ResourceType resource1, ResourceType resource2){
        clientModel.playYearOfPlentyCard(playerIndex, resource1, resource2);
        return clientModel;
    }

    /**
     * Player using a road building dev card.
     * @param playerIndex of player using the card.
     * @param edgeLocation1 of the first road.
     * @param edgeLocation2 of the second road.
     */
    public ClientModel roadBuilding(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2){
        clientModel.playRoadBuildingCard(playerIndex, edgeLocation1, edgeLocation2);
        return clientModel;
    }

    /**
     * Player using a monopoly dev card.
     * @param index of the player using the card.
     * @param res the player is monopolizing.
     */
    public ClientModel monopoly(int index, ResourceType res){
        clientModel.playMonopolyCard(index, res);
        return clientModel;
    }

    /**
     * Player offering a trade.
     * @param index of the player offering the trade.
     * @param off resource list offer.
     * @param receiverIndex index of the player receiving the offer
     */
    public ClientModel offerTrade(int index, ResourceList off, int receiverIndex){
        clientModel.offerTrade(index, off, receiverIndex);
        return clientModel;
    }

    /**
     * Player choosing whether or not to accept a trade.
     * @param index of the player choosing.
     * @param accept returns true if they accept.
     */
    public ClientModel acceptTrade(int index, boolean accept){
        clientModel.acceptTrade(index, accept);
        return clientModel;
    }

    /**
     * Maratime Trade Request
     * @param index of the player trading.
     * @param ratio of the trade.
     * @param inputResource to trade.
     * @param outputResource to recieve.
     */
    public ClientModel martimeTrade(int index, int ratio, ResourceType inputResource, ResourceType outputResource){
        clientModel.martimeTrade(index, ratio, inputResource, outputResource);
        return clientModel;
    }

    /**
     * Player building a new road.
     * @param edgeLocation edgeLocation, which contains the player ID and location of the road.
     */
    public ClientModel buildRoad(EdgeLocation edgeLocation, int index, boolean free){
        clientModel.buildRoad(edgeLocation, index, free);
        return clientModel;
    }

    /**
     * Player building a settlement.
     * @param newSettlement VertexObject, which contains the player ID and location of the settlement.
     */
    public ClientModel buildSettlement(VertexObject newSettlement, boolean free){
        clientModel.buildSettlement(newSettlement, free);
        return clientModel;
    }

    /**
     * Player building a city.
     * @param newCity VertexObject, which contains the player ID and location of the City.
     */
    public ClientModel buildCity(VertexObject newCity, boolean free){
        clientModel.buildCity(newCity, free);
        return clientModel;
    }



    //USER AND GAME COMMANDS

    /**
     * User login.
     * @param username of the user.
     * @param password of the user.
     * @return true if login is successful.
     */
    public boolean login(String username, String password){

        return (UserManager.getInstance().isValidLogin(username, password));
    }

    /**
     * User registering.
     * @param username of the user.
     * @param password of the user.
     * @return true if login is successful.
     */
    public boolean register(String username, String password){
        return false;
    }


    /**
     * List all of the games.
     * @return an array of the GameInfo objects used to display the list.
     */
    public GameInfo[] list(){
        return null;
    } //GET

    /**
     * Join a specific game.
     * @param gameID of the game.
     * @param color the player has selected.
     * @return true is succesful.
     */
    public boolean join(int gameID, CatanColor color){
        return false;
    }

    /**
     * Create a new game.
     * @param name of the game.
     * @param randomTiles true if randomized.
     * @param randomNumbers true if randomized.
     * @param randomPorts true if randomized.
     */
    public GameInfo create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts){
        return null;
    }

    /**
     * Get the model.
     * @param version of the model, compared to see if its different.
     * @return the model.
     */
    public ClientModel model(int version){
        return null;
    } //GET

    /**
     * Add an AI to the current game.
     * @return true if the AI
     */
    public boolean addAI(){
        return false;
    }

    /**
     * List the AI
     * @return a string array of the names of the AI
     */
    public String[] listAI(){
        return null;
    }
}
