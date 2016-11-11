package server;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.ClientModel;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.VertexObject;
import shared.model.resourcebank.ResourceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adamthompson on 11/4/16.
 */
public class ServerFacade implements IServerFacade {

    int userID;

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

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = robObj.getPlayerIndex();
            HexLocation location = robObj.getLocation();
            int victimIndex = robObj.getVictimIndex();

            ClientModel model = game.robPlayer(playerIndex, location, victimIndex);
            return model;
        }
        return null;
    }

    /**
     * Buying a dev card.
     * @param userID of the player buying the card.
     */
    public ClientModel buyDevCard(int userID, int gameID, PurchaseDevCardCommand purchDevCardObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = purchDevCardObj.getPlayerIndex();

            ClientModel model = game.buyDevCard(playerIndex);
            return model;
        }
        return null;
    }

    /**
     * Playing a solider dev card.
     * @param userID of the player using the soldier card.
     */
    public ClientModel playSoldier(int userID, int gameID, PlaySoldierCommand soldierObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = soldierObj.getPlayerIndex();
            HexLocation location = soldierObj.getRobberLoc();
            int victimIndex = soldierObj.getVictimIndex();

            ClientModel model = game.soldier(playerIndex, location, victimIndex);
            return model;
        }
        return null;
    }

    /**
     * Player using a monument dev card.
     * @param userID of the player using the dev card.
     */
    public ClientModel playMonument(int userID, int gameID, PlayMonumentCommand monumentObj) {
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = monumentObj.getPlayerIndex();

            ClientModel model = game.monument(playerIndex);
            return model;
        }
        return null;
    }

    /**
     * Player using a Year of plenty Dev Card
     * @param userID player using the card.
     */
    public ClientModel playYearOfPlenty(int userID, int gameID, PlayYearOfPlentyCommand yearOfPlentyObj) {
        if(validateParams(userID, gameID)) {

            Game game =  GamesManager.getInstance().getGame(gameID);
            int playerIndex = yearOfPlentyObj.getPlayerIndex();
            ResourceType res1 = yearOfPlentyObj.getResource1();
            ResourceType res2 = yearOfPlentyObj.getResource2();

            ClientModel model = game.yearOfPlenty(playerIndex, res1, res2);
            return model;
        }
        return null;
    }

    /**
     * Player using a road building dev card.
     * @param userID of player using the card.
     */
    public ClientModel playRoadBuilding(int userID, int gameID, PlayRoadBuilderCommand roadBldgCardObj) {
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = roadBldgCardObj.getPlayerIndex();
            EdgeLocation loc1 = roadBldgCardObj.getLocationONE();
            EdgeLocation loc2 = roadBldgCardObj.getLocationTWO();

            ClientModel model = game.roadBuilding(playerIndex, loc1, loc2);
            return model;
        }
        return null;
    }

    /**
     * Player using a monopoly dev card.
     * @param userID of the player using the card.
     */
    public ClientModel playMonopoly(int userID, int gameID, PlayMonopolyCommand monopolyObj) {
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = monopolyObj.getPlayerIndex();
            ResourceType resource = monopolyObj.getResource();

            ClientModel model = game.monopoly(playerIndex, resource);
            return model;
        }
        return null;
    }

    /**
     * Player offering a trade.
     * @param userID of the player offering the trade.
     */
    public ClientModel offerTrade(int userID, int gameID, OfferTradeCommand offerTradeObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = offerTradeObj.getPlayerIndex();
            ResourceList resList = offerTradeObj.getOffer();
            int receiverIndex = offerTradeObj.getReceiver();

            ClientModel model = game.offerTrade(playerIndex, resList, receiverIndex);
            return model;
        }
        return null;
    }

    /**
     * Player choosing whether or not to accept a trade.
     * @param userID of the player choosing.
     */
    public ClientModel acceptTrade(int userID, int gameID, AcceptTradeCommand acceptTradeObj) {
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = acceptTradeObj.getPlayerIndex();
            boolean accept = acceptTradeObj.getWillAccept();

            ClientModel model = game.acceptTrade(playerIndex, accept);
            return model;
        }
        return null;
    }

    /**
     * Maratime Trade Request
     * @param userID of the player trading.
     */
    public ClientModel maritimeTrade(int userID, int gameID, MaritimeTradeCommand maritTradeObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            int playerIndex = maritTradeObj.getPlayerIndex();
            int ratio = maritTradeObj.getRatio();
            ResourceType input = maritTradeObj.getToTrade();
            ResourceType output = maritTradeObj.getToReceive();

            ClientModel model = game.martimeTrade(playerIndex, ratio, input, output);
            return model;
        }
        return null;
    }

    /**
     * Player building a new road.
     * @param userID EdgeValue, which contains the player ID and location of the road.
     */
    public ClientModel buildRoad(int userID, int gameID, BuildRoadCommand buildRoadObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            EdgeLocation location = buildRoadObj.getRoadLocation();
            int playerIndex = buildRoadObj.getPlayerIndex();
            boolean free = buildRoadObj.getFree();

            ClientModel model = game.buildRoad(location, playerIndex, free);
            return model;
        }
        return null;
    }

    /**
     * Player building a settlement.
     */
    public ClientModel buildSettlement(int userID, int gameID, BuildSettlementCommand buildSettObj) {
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            VertexObject settlementLoc = buildSettObj.getVertex();
            boolean free = buildSettObj.getFree();

            ClientModel model = game.buildSettlement(settlementLoc, free);
            return model;
        }
        return null;
    }

    /**
     * Player building a city.
     */
    public ClientModel buildCity(int userID, int gameID, BuildCityCommand buildCityObj){
        if(validateParams(userID, gameID)) {

            Game game = GamesManager.getInstance().getGame(gameID);
            VertexObject cityLoc = buildCityObj.getVertex();

            ClientModel model = game.buildCity(cityLoc);
            return model;
        }
        return null;
    }


    //USER AND GAME COMMANDS

    /**
     * User login.
     * @param command - LoginCommand containing needed login info
     * @return true if login is successful.
     */
    public boolean login(LoginCommand command){
        String username = command.getUsername();
        String password = command.getPassword();

        User user = UserManager.getInstance().getUserByUsername(username, password);

        if(user != null) {
            userID = user.getUserID();
            return true;
        }
        else {return false;}
    }

    /**
     * User registering.
     * @param command - RegisterCommand containing needed register info
     * @return true if login is successful.
     */
    public boolean register(RegisterCommand command){
        String username = command.getUsername();
        String password = command.getPassword();

        boolean valid = UserManager.getInstance().addUser(username, password);
        User user = UserManager.getInstance().getUserByUsername(username, password);

        if(valid) {userID = user.getUserID();}
        return valid;
    }


    /**
     * List all of the games.
     * @return an array of the GameInfo objects used to display the list.
     */
    public GameInfo[] list(int userId){
        User user = UserManager.getInstance().getUser(userID);

        if(user != null) {
            HashMap<Integer, Game> allGames = GamesManager.getInstance().getAllGames();
            GameInfo[] gameInfoList = new GameInfo[allGames.size()];

            for(Integer key: allGames.keySet()) {
                gameInfoList[key] = allGames.get(key).getGameInfo();
            }
            return gameInfoList;
        }
        return null;
    } //GET

    /**
     * Join a specific game.
     * @param command
     * @return true is successful.
     */
    public boolean join(int userId, GameJoinCommand command){
        User user = UserManager.getInstance().getUser(userID);

        if(user != null) {
            int gameID = command.getGameID();
            CatanColor color = command.getColor();
            String username = user.getUserName();

            Game game = GamesManager.getInstance().getGame(gameID);
            GameInfo gameInfo = GamesManager.getInstance().getGame(gameID).getGameInfo();

            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setColor(color);
            playerInfo.setId(userID);
            playerInfo.setName(username);
            int playerIndex = gameInfo.getPlayers().size();
            playerInfo.setPlayerIndex(playerIndex);

            List<PlayerInfo> players = gameInfo.getPlayers();

            if(gameInfo.getPlayers().size() != 4) {
                //Adds new playerInfo object to the gameInfo list
                gameInfo.addPlayer(playerInfo);
                //Adds new player to the clientModel list
                return game.join(color, user);
            }
            else {
                for(PlayerInfo p: players) {
                    if(p.getId() == userId) {return true;}
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Create a new game.
     */
    public GameInfo create(int userId, GameCreateCommand command){
        User user = UserManager.getInstance().getUser(userID);

        if(user != null) {
            int gameID = GamesManager.getInstance().getAllGames().size();
            String title = command.getName();

            GameInfo gameInfo = new GameInfo();

            gameInfo.setId(gameID);
            gameInfo.setTitle(title);

            Game game = new Game(gameInfo);

            GamesManager.getInstance().addGame(game);

        }
        return null;
    }

    /**
     * Get the model.
     * @return the model.
     */
    //IS THIS THE METHOD WE CALL EACH TIME THE POLLER ASKS FOR A NEW MODEL??
    //DO I NEED TO BE UPDATING THE VERSION NUMBER OF THE CLIENTMODEL??
    // I actually think that we need to update the version in the Game object functions...right?
    public ClientModel model(int userId, int gameID, FetchNewModelCommand command){
        Game game = GamesManager.getInstance().getGame(gameID);
        ClientModel model = game.getClientModel();

        return model;
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
    public String[] listAI(){
        return null;
    }

    @Override
    public int getUserId() {
        return userID;
    }

    @Override
    public int getGameId() {
        return 0;
    }
}
