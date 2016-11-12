package server;

import client.data.GameInfo;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;
import shared.model.commandmanager.CommandManager;
import shared.model.map.VertexObject;
import shared.model.resourcebank.ResourceList;

import java.util.HashMap;

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

    /**
     * PlayerIndex mapped to User
     */
    private HashMap<Integer, User> userList;

    /**
     * Stores the commands for this game.
     */
    private CommandManager commandManager = new CommandManager();

    public Game(GameInfo gameInfo, boolean randTiles, boolean randNumbers, boolean randPorts) {
        this.gameInfo = gameInfo;
        this.clientModel = new ClientModel(gameInfo.getId(),randTiles, randNumbers, randPorts);
        this.userList = new HashMap<Integer, User>();
    }

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
        if(clientModel.finishTurn(index)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Send a chat message.
     * @param index of the player sending the message.
     * @param message the player wants to display.
     */
    public ClientModel sendChat(int index, String message){
        if(clientModel.sendChat(index, message)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Discarding cards from rolling a 7
     * @param index of the player discarding.
     * @param discarded cards the player has selected to discard.
     */
    public ClientModel discardCards(int index, ResourceList discarded){
        if(clientModel.discardCards(index, discarded)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Roll dice command. Players need to recieve resources according to the passed in number.
     * @param number randomly calculated number.
     */
    public ClientModel rollNumber(int number){
        if(number == 7){
            if(clientModel.roll7()){
                return clientModel;
            }
        } else {
            if(clientModel.receiveResourcesFromDiceRoll(number)){
                return clientModel;
            }
        }
        return null;
    }

    /**
     * Rob a player.
     * @param playerIndex who is robbing the victim.
     * @param location that is now being robbed.
     * @param victimIndex who is losing a card. Null if no player can be robbed.
     */
    public ClientModel robPlayer(int playerIndex, HexLocation location, int victimIndex){
        if(clientModel.placeRobber(playerIndex, location, victimIndex)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Buying a dev card.
     * @param index of the player buying the card.
     */
    public ClientModel buyDevCard(int index){
        if(clientModel.purchaseDevCard(index)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Playing a solider dev card.
     * @param index of the player using the soldier card.
     * @param robberLocation new location being robbed.
     * @param victimIndex being robbed.
     */
    public ClientModel soldier(int index, HexLocation robberLocation, int victimIndex){
        if(clientModel.playSoldierCard(index, robberLocation, victimIndex)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player using a monument dev card.
     * @param index of the player using the dev card.
     */
    public ClientModel monument(int index){
        if(clientModel.playMonumentCard(index)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player using a Year of plenty Dev Card
     * @param playerIndex player using the card.
     * @param resource1 from the bank.
     * @param resource2 from the bank.
     */
    public ClientModel yearOfPlenty(int playerIndex, ResourceType resource1, ResourceType resource2){
        if(clientModel.playYearOfPlentyCard(playerIndex, resource1, resource2)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player using a road building dev card.
     * @param playerIndex of player using the card.
     * @param edgeLocation1 of the first road.
     * @param edgeLocation2 of the second road.
     */
    public ClientModel roadBuilding(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2){
        if(clientModel.playRoadBuildingCard(playerIndex, edgeLocation1, edgeLocation2)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player using a monopoly dev card.
     * @param index of the player using the card.
     * @param res the player is monopolizing.
     */
    public ClientModel monopoly(int index, ResourceType res){
        if(clientModel.playMonopolyCard(index, res)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player offering a trade.
     * @param index of the player offering the trade.
     * @param off resource list offer.
     * @param receiverIndex index of the player receiving the offer
     */
    public ClientModel offerTrade(int index, ResourceList off, int receiverIndex){
        if(clientModel.offerTrade(index, off, receiverIndex)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player choosing whether or not to accept a trade.
     * @param index of the player choosing.
     * @param accept returns true if they accept.
     */
    public ClientModel acceptTrade(int index, boolean accept){
        if(clientModel.acceptTrade(index, accept)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Maratime Trade Request
     * @param index of the player trading.
     * @param ratio of the trade.
     * @param inputResource to trade.
     * @param outputResource to recieve.
     */
    public ClientModel martimeTrade(int index, int ratio, ResourceType inputResource, ResourceType outputResource){
        if(clientModel.maritimeTrade(index, ratio, inputResource, outputResource)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player building a new road.
     * @param edgeLocation edgeLocation, which contains the player ID and location of the road.
     */
    public ClientModel buildRoad(EdgeLocation edgeLocation, int index, boolean free){
        if(clientModel.buildRoad(edgeLocation, index, free)){
            return clientModel;
        }
        return null;
    }

    /**
     * Player building a settlement.
     * @param newSettlement VertexObject, which contains the player ID and location of the settlement.
     */
    public ClientModel buildSettlement(VertexObject newSettlement, boolean free){
        if(clientModel.buildSettlement(newSettlement, free)) {
            return clientModel;
        }
        return null;
    }

    /**
     * Player building a city.
     * @param newCity VertexObject, which contains the player ID and location of the City.
     */
    public ClientModel buildCity(VertexObject newCity){
        if (clientModel.buildCity(newCity)) {
            return clientModel;
        }
        return null;
    }



    //USER AND GAME COMMANDS


//    /**
//     * User login.
//     * @param username of the user.
//     * @param password of the user.
//     * @return true if login is successful.
//     */
//    public boolean login(String username, String password){
//
//        return (UserManager.getInstance().isValidLogin(username, password));
//    }
//
//    /**
//     * User registering.
//     * @param username of the user.
//     * @param password of the user.
//     * @return true if login is successful.
//     */
//    public boolean register(String username, String password){
//        return false;
//    }
//
//
//    /**
//     * List all of the games.
//     * @return an array of the GameInfo objects used to display the list.
//     */
//    public GameInfo[] list(){
//        return null;
//    } //GET

    /**
     * Join a specific game.
     * @param color the player has selected.
     * @return true is succesful.
     */
    public boolean join(CatanColor color, User user){
        return (clientModel.joinGame(color, user));
    }

//    /**
//     * Create a new game.
//     * @param name of the game.
//     * @param randomTiles true if randomized.
//     * @param randomNumbers true if randomized.
//     * @param randomPorts true if randomized.
//     */
//    public GameInfo create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts){
//        return null;
//    }
//
//    /**
//     * Get the model.
//     * @param version of the model, compared to see if its different.
//     * @return the model.
//     */
//    public ClientModel model(int version){
//        return null;
//    } //GET
//
//    /**
//     * Add an AI to the current game.
//     * @return true if the AI
//     */
//    public boolean addAI(){
//        return false;
//    }
//
//    /**
//     * List the AI
//     * @return a string array of the names of the AI
//     */
//    public String[] listAI(){
//        return null;
//    }

    public void logCommand(BaseCommand command){
        commandManager.addCommandtoList(command);
    }
}
