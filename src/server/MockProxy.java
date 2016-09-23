package server;
import org.json.JSONObject;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * all the JSONObject parameters of overridden functions here can be passed in as null
 */
public class MockProxy implements IServerProxy {

    public String getLoginCookie() {
        return null;
    }

    public String getRegisterCookie() {
        return null;
    }

    /**
     * note: the mock proxy does not interact with the server
     *
     * in ServerProxy:
     *  Posts HTTP
     *
     * @param url the url determined by other methods within IServerProxy
     * @param postData - string in JSON format used to send with the HTTP post request
     * @return response from http
     */
    @Override
    public String httpPost(String url, String postData) {
        return "no interaction with server in mock proxy";
    }

    /**
     * note: the mock proxy does not interact with the server
     *
     * in ServerProxy:
     *  HTTP Get Method
     *
     * @param url the url determined by other methods within IServerProxy
     * @return response from http
     */
    @Override
    public String httpGet(String url) {
        return "no interaction with server in mock proxy";
    }

    /**
     *  returns a hard coded JSONObject in the format as if it came from the server
     *  this is for testing the server poller
     *
     *  in ServerProxy:
     *   the purpose is to log the caller in to the server, and sets their catan.user HTTP cookie
     *
     * @param json the value should always be null when passed into the mock proxy
     */
    @Override
    public String userLogin(JSONObject json) {
       return SUCCESS;
    }

    /**
     *  returns a hard coded JSONObject in the format as if it came from the server
     *  this is for testing the server poller
     *
     *  in ServerProxy:
     *   1) Creates a new user account
     *   2) Logs the caller in to the server as the new user, and sets their catan.user HTTP cookie
     *
     * @param json the value should always be null when passed into the mock proxy
     * @return the JSON Model
     */
    @Override
    public String userRegister(JSONObject json) {
        return SUCCESS;
    }

    /**
     *  returns a hard coded JSONObject representing the Model in the format as if it came from the server
     *  this is for testing the server poller
     *
     *  in ServerProxy:
     *   Returns information about all of the current games on the server
     *
     * @return Model in JSON
     */
    @Override
    public JSONObject gamesList() {
        return new JSONObject(GAMES_LIST);
    }

    /**
     * in ServerProxy:
     *  Creates a new game on the server.
     *
     * @param json - name:string, randomTiles:bool, randomNumbers:bool, randomPorts:bool, (hardcoded here)
     * @return data regarding the new game, (hardcoded here)
     */
    @Override
    public JSONObject gameCreate(JSONObject json) {
        return new JSONObject(GAME_INFO);
    }

    /**
     * in ServerProxy:
     *  Adds the player to the specified game and sets their catan.game cookie
     *
     * @param json - gameID:int, color:string
     * @return a string notifying the user whether the request was successful or not
     */
    @Override
    public String gameJoin(JSONObject json) {
        return SUCCESS;
    }

    /**
     * in ServerProxy:
     *  This method is for testing and debugging purposes. When a bug is found, you can use the
     *  /games/save method to save the state of the game to a file, and attach the file to a bug report.
     *  A developer can later restore the state of the game when the bug occurred by loading the
     *  previously saved file using the /games/load method. Game files are saved to and loaded from
     *  the server's saves/ directory.
     *
     * @param json - gameID:int, fileName:file
     * @return a string notifying the user whether the request was successful or not
     */
    @Override
    public String gameSave(JSONObject json) {
        return SUCCESS;
    }

    /**
     * in ServerProxy:
     *  This method is for testing and debugging purposes. When a bug is found, you can use the
     *  /games/save method to save the state of the game to a file, and attach the file to a bug report.
     *  A developer can later restore the state of the game when the bug occurred by loading the
     *  previously saved file using the /games/load method. Game files are saved to and loaded from
     *  the server's saves/ directory
     *
     * @param json - fileName:file
     * @return a string notifying the user whether the request was successful or not
     */
    @Override
    public String gameLoad(JSONObject json) {
        return SUCCESS;
    }

    /**
     * in Server Proxy:
     *  Returns the current state of the game in JSON format.
     *  In addition to the current game state, the returned JSON also includes a “version” number for
     *  the tests.client model. The next time /game/model is called, the version number from the
     *  previously retrieved model may optionally be included as a query parameter in the request
     *  (/game/model?version=N). The server will only return the full JSON game state if its version
     *  number is not equal to N. If it is equal to N, the server returns “true” to indicate that the caller
     *  already has the latest game state. This is merely an optimization. If the version number is
     *  not included in the request URL, the server will return the full game state.
     *
     * @param json - version:int
     * @return the JSON Model
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * 2. If specified, the version number is included as the “version” query parameter in the
     * request URL, and its value is a valid integer
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response.
     * 2. The response body contains JSON data
     * a. The full tests.client model JSON is returned if the caller does not provide a version
     * number, or the provide version number does not match the version on the server
     * b. “true” (true in double quotes) is returned if the caller provided a version number,
     * and the version number matched the version number on the server
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     * The format of the returned JSON can be found on the server’s Swagger page, or in the document
     * titled “tests.client Model JSON Documentation”
     */
    @Override
    public JSONObject gameModelVersion(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Clears out the command history of the current game.
     *  For the default games created by the server, this method reverts the game to the state
     *  immediately after the initial placement round. For user­created games, this method reverts
     *  the game to the very beginning (i.e., before the initial placement round).
     *  This method returns the tests.client model JSON for the game after it has been reset.
     *  You must login and join a game before calling this method.
     *
     * @return the JSON Model
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * @post If the operation succeeds,
     * 1. The game’s command history has been cleared out
     * 2. The game’s players have NOT been cleared out
     * 3. The server returns an HTTP 200 success response.
     * 4. The body contains the game’s updated tests.client model JSON
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     * Note:
     * When a game is reset, the players in the game are maintained
     */
    @Override
    public JSONObject gameReset() {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Returns a list of commands that have been executed in the current game.
     *  This method can be used for testing and debugging. The command list returned by this
     *  method can be passed to the /game/command (POST) method to re­execute the commands
     *  in the game. This would typically be done after calling /game/reset to clear out the game’s
     *  command history. This is one way to capture the state of a game and restore it later. (See
     *  the /games/save and /games/load methods which provide another way to save and restore
     *  the state of a game.)
     *  For the default games created by the server, this method returns a list of all commands that
     *  have been executed after the initial placement round. For user­created games, this method
     *  returns a list of all commands that have been executed since the very beginning of the game
     *  (i.e., before the initial placement round).
     *  You must login and join a game before calling this method
     *
     * @return Model in JSON (hardcoded here)
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies)
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response.
     * 2. The body contains a JSON array of commands that have been executed in the game.
     * This command array is suitable for passing back to the /game/command [POST] method to
     * restore the state of the game later (after calling /game/reset to revert the game to its initial state).
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message
     */
    @Override
    public JSONObject getGameCommands() {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Executes the specified command list in the current game.
     *  This method can be used for testing and debugging. The command list returned by the
     *  /game/command [GET] method is suitable for passing to this method.
     *  This method returns the tests.client model JSON for the game after the command list has been
     *  applied.
     *  You must login and join a game before calling this method
     *
     * @return a list of commands that have been executed
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * @post If the operation succeeds,
     * 1. The passed­in command list has been applied to the game.
     * 2. The server returns an HTTP 200 success response.
     * 3. The body contains the game’s updated tests.client model JSON
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message
     *
     * todo: change values of the commands to valid commands
     */
    @Override
    public JSONObject executeGameCommands(JSONObject json) {
        return new JSONObject("{\"command1\", \"command2\", \"command3\"}");
    }

    /**
     * in ServerProxy:
     *  Returns a list of supported AI player types.
     *  Currently, LARGEST_ARMY is the only supported type.
     *
     * @return a list of supported AI player types (hardcoded here)
     * @pre None
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response.
     * 2. The body contains a JSON string array enumerating the different types of AI players.
     * These are the values that may be passed to the /game/addAI method.
     */
    @Override
    public JSONObject listAI() {
        return new JSONObject(AI);
    }

    /**
     * in ServerProxy:
     *  Adds an AI player to the current game.
     *  You must login and join a game before calling this method
     *
     * @return the JSON Model
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * 2. There is space in the game for another player (i.e., the game is not “full”).
     * 3. The specified “AIType” is valid (i.e., one of the values returned by the /game/listAI
     * method)
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response with “Success” in the body.
     * 2. A new AI player of the specified type has been added to the current game. The server
     * selected a name and color for the player.
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message
     */
    @Override
    public JSONObject addAI(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Sets the server’s logging level
     *
     * @param json - loggingLevel:LoggingLevel
     * @return the JSON Model
     * @pre 1.The caller specifies a valid logging level. Valid values include: SEVERE, WARNING,
     * INFO, CONFIG, FINE, FINER, FINEST
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response with “Success” in the body.
     * 2. The Server is using the specified logging level
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     */
    @Override
    public JSONObject utilChangeLogLevel(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Adds a message to the end of the chat
     *
     * @param json - playerIndex:int, content:string
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game.
     * @post The chat contains your message at the end
     */
    @Override
    public JSONObject sendChat(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the server what number was rolled so resources can be distributed, discarded or robbed.
     *
     * @param json - playerIndex:int, number:int(2-12)
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. It is your turn. The tests.client model’s status is ‘Rolling’
     * @post The tests.client model’s status is now in ‘Discarding’ or ‘Robbing’ or ‘Playing’
     */
    @Override
    public JSONObject rollNumber(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Ends the players turn.
     *
     * @param json - playerIndex:int
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game.
     * @post The cards in your new dev card hand have been transferred to your old dev card
     * hand. It is the next player’s turn
     */
    @Override
    public JSONObject finishTurn(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells Server what cards to remove from the player's hand.
     *
     * @param json - playerIndex:int, discardedCards:ResourceList
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The status of the tests.client model is 'Discarding'.
     * You have over 7 cards. You have the cards you're choosing to discard.
     * @post You gave up the specified resources. If you're the last one to discard, the tests.client model status changes to 'Robbing'
     */
    @Override
    public JSONObject discardCards(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells Server to build a road for the given player in the given location.
     *
     * @param json - playerIndex:int, roadLocation:HexLocation, free:bool
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The road location is open.
     * The road location is connected to another road owned by the player. The road location is not on water.
     * You have the required resources (1 wood, 1 brick; 1 road).
     * Setup round: Must be placed by settlement owned by the player with no adjacent
     * road
     * @post You lost the resources required to build a road (1 wood, 1 brick; 1 road).
     * The road is on the map at the specified location.
     * If applicable, “longest road” has been awarded to the player with the longest road
     */
    @Override
    public JSONObject buildRoad(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells Server to build a settlement for the given player in the given location.
     *
     * @param json - playerIndex:int, vertexLocation:VertexObject, free:bool
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The settlement location is open.
     * The settlement location is not on water.
     * The settlement location is connected to one of your roads except during setup.
     * You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement).
     * The settlement cannot be placed adjacent to another settlement
     * @post You lost the resources required to build a settlement if not setup rounds(1 wood, 1 brick, 1 wheat, 1
     * sheep; 1 settlement).
     * The settlement is on the map at the specified location
     */
    @Override
    public JSONObject buildSettlement(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells Server to build a city for the given player in the given location.
     *
     * @param json - playerIndex:int, vertexLocation:VertexObject
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The city location is where you currently have a settlement.
     * You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city).
     * The city is on the map at the specified location.
     * You got a settlement back
     */
    @Override
    public JSONObject buildCity(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells Server to send a trade offer to the other player.
     *
     * @param json - playerIndex:int, offer:ResourceList, receiver:int)
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. You have the resources you are offering.
     * @post The trade is offered to the other player (stored in the server model).
     */
    @Override
    public JSONObject offerTrade(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to trade the players' cards.
     *
     * @param json - playerIndex:int, willAccept:bool
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. You have been offered a domestic trade.
     * To accept the offered trade, you have the required resources
     * @post If you accepted, you and the player who offered swap the specified resources.
     * If you declined no resources are exchanged.
     * The trade offer is removed
     */
    @Override
    public JSONObject acceptTrade(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to trade bank and player cards.
     *
     * @param json - playerIndex:int, ratio:int(2,3 or4), inputResource:Resource, outputResource:Resource
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. You have the resources you are giving.
     * For ratios less than 4, you have the correct port for the trade
     * @post The trade has been executed (the offered resources are in the bank, and the
     * requested resource has been received)
     */
    @Override
    public JSONObject maritimeTrade(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to move the robber and move the stolen card.
     *
     * @param json - playerIndex:int, location:HexLocation, victimIndex:int(-1,0,1,2,or 3)
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The robber is not being kept in the same location.
     * If a player is being robbed (i.e., victimIndex != ­1), the player being robbed has
     * resource cards
     * @post The robber is in the new location. The player being robbed (if any) gave you one of his resource cards (randomly
     * selected)
     */
    @Override
    public JSONObject robPlayer(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to give the player a new Development Card.
     *
     * @param json - playerIndex:int
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. You have the required resources (1 ore, 1 wheat, 1 sheep).
     * There are dev cards left in the deck
     * @post You have a new card
     * - If it is a monument card, it has been added to your old devcard hand
     * - If it is a non­monument card, it has been added to your new devcard hand
     * (unplayable this turn)
     */
    @Override
    public JSONObject purchaseDevCard(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to allow the player to rob another player.
     *
     * @param json - playerIndex:int, location:HexLocation, victimIndex:int(-1,0,1,2,or 3)
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The robber is not being kept in the same location.
     * If a player is being robbed (i.e., victimIndex != ­1), the player being robbed has
     * resource cards
     * @post The robber is in the new location.
     * The player being robbed (if any) gave you one of his resource cards (randomly
     * selected).
     * If applicable, “largest army” has been awarded to the player who has played the
     * most soldier cards.
     * You are not allowed to play other development cards during this turn (except for
     * monument cards, which may still be played)
     */
    @Override
    public JSONObject playSoldier(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to allow the player to pick two resources from the Bank
     *
     * @param json - playerIndex:int, resource1:Resource, resource2:Resource
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The two specified resources are in the bank
     * @post You gained the two specified resources
     */
    @Override
    public JSONObject playYearOfPlenty(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to allow the player to build two roads for free.
     *
     * @param json - playerIndex:int, spot1:EdgeLocation, spot2:EdgeLocation
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game. The first road location (spot1) is connected to one of your roads..
     * The second road location (spot2) is connected to one of your roads or to the first
     * road location (spot1).
     * Neither road location is on water.
     * You have at least two unused roads
     * @post You have two fewer unused roads.
     * Two new roads appear on the map at the specified locations.
     * If applicable, “longest road” has been awarded to the player with the longest road
     */
    @Override
    public JSONObject playRoadBuilding(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to allow the player to pick a resource to gather from the other players.
     *
     * @param json - playerIndex:int, resource:Resource
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game.
     * @post All of the other players have given you all of their resource cards of the specified
     * type
     */
    @Override
    public JSONObject playMonopoly(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }

    /**
     * in ServerProxy:
     *  Tells the Server to add one victory point to the player.
     *
     * @param json - playerIndex:int
     * @return the JSON Model
     * @pre Caller has already logged in to the server and joined a game.
     * You have enough monument cards to win the game (i.e., reach 10 victory points)
     * @post You gained a victory point.
     */
    @Override
    public JSONObject playMonument(JSONObject json) {
        return new JSONObject(GAME_MODEL);
    }



    private final String SUCCESS = "Success";

    // not going to change to local variable so code is easier to read
    private final String GAMES_LIST = "[\n" +
            "  {\n" +
            "    \"title\": \"Default Game\",\n" +
            "    \"id\": 0,\n" +
            "    \"players\": [\n" +
            "      {\n" +
            "        \"color\": \"orange\",\n" +
            "        \"name\": \"Sam\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"blue\",\n" +
            "        \"name\": \"Brooke\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"red\",\n" +
            "        \"name\": \"Pete\",\n" +
            "        \"id\": 10\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"green\",\n" +
            "        \"name\": \"Mark\",\n" +
            "        \"id\": 11\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"title\": \"AI Game\",\n" +
            "    \"id\": 1,\n" +
            "    \"players\": [\n" +
            "      {\n" +
            "        \"color\": \"orange\",\n" +
            "        \"name\": \"Pete\",\n" +
            "        \"id\": 10\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"red\",\n" +
            "        \"name\": \"Steve\",\n" +
            "        \"id\": -2\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"yellow\",\n" +
            "        \"name\": \"Miguel\",\n" +
            "        \"id\": -3\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"white\",\n" +
            "        \"name\": \"Ken\",\n" +
            "        \"id\": -4\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"title\": \"Empty Game\",\n" +
            "    \"id\": 2,\n" +
            "    \"players\": [\n" +
            "      {\n" +
            "        \"color\": \"orange\",\n" +
            "        \"name\": \"Sam\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"blue\",\n" +
            "        \"name\": \"Brooke\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"red\",\n" +
            "        \"name\": \"Pete\",\n" +
            "        \"id\": 10\n" +
            "      },\n" +
            "      {\n" +
            "        \"color\": \"green\",\n" +
            "        \"name\": \"Mark\",\n" +
            "        \"id\": 11\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    private final String GAME_INFO = "{\n" +
            "  \"title\": \"string\",\n" +
            "  \"id\": \"integer\",\n" +
            "  \"players\": [\n" +
            "    {}\n" +
            "  ]\n" +
            "}";

    private final String GAME_MODEL = "{\n" +
            "  \"bank\": {\n" +
            "    \"brick\": \"integer\",\n" +
            "    \"ore\": \"integer\",\n" +
            "    \"sheep\": \"integer\",\n" +
            "    \"wheat\": \"integer\",\n" +
            "    \"wood\": \"integer\"\n" +
            "  },\n" +
            "  \"chat\": {\n" +
            "    \"lines\": [\n" +
            "      {\n" +
            "        \"message\": \"string\",\n" +
            "        \"source\": \"string\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"log\": {\n" +
            "    \"lines\": [\n" +
            "      {\n" +
            "        \"message\": \"string\",\n" +
            "        \"source\": \"string\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"map\": {\n" +
            "    \"hexes\": [\n" +
            "      {\n" +
            "        \"location\": {\n" +
            "          \"x\": \"integer\",\n" +
            "          \"y\": \"integer\"\n" +
            "        },\n" +
            "        \"resource\": \"string\",\n" +
            "        \"number\": \"integer\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"ports\": [\n" +
            "      {\n" +
            "        \"resource\": \"string\",\n" +
            "        \"location\": {\n" +
            "          \"x\": \"integer\",\n" +
            "          \"y\": \"integer\"\n" +
            "        },\n" +
            "        \"direction\": \"string\",\n" +
            "        \"ratio\": \"integer\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"roads\": [\n" +
            "      {\n" +
            "        \"owner\": \"index\",\n" +
            "        \"location\": {\n" +
            "          \"x\": \"integer\",\n" +
            "          \"y\": \"integer\",\n" +
            "          \"direction\": \"string\"\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"settlements\": [\n" +
            "      {\n" +
            "        \"owner\": \"index\",\n" +
            "        \"location\": {\n" +
            "          \"x\": \"integer\",\n" +
            "          \"y\": \"integer\",\n" +
            "          \"direction\": \"string\"\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"cities\": [\n" +
            "      {\n" +
            "        \"owner\": \"index\",\n" +
            "        \"location\": {\n" +
            "          \"x\": \"integer\",\n" +
            "          \"y\": \"integer\",\n" +
            "          \"direction\": \"string\"\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"radius\": \"integer\",\n" +
            "    \"robber\": {\n" +
            "      \"x\": \"integer\",\n" +
            "      \"y\": \"integer\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"players\": [\n" +
            "    {\n" +
            "      \"cities\": \"index\",\n" +
            "      \"color\": \"string\",\n" +
            "      \"discarded\": \"boolean\",\n" +
            "      \"monuments\": \"index\",\n" +
            "      \"name\": \"string\",\n" +
            "      \"newDevCards\": {\n" +
            "        \"monopoly\": \"index\",\n" +
            "        \"monument\": \"index\",\n" +
            "        \"roadBuilding\": \"index\",\n" +
            "        \"soldier\": \"index\",\n" +
            "        \"yearOfPlenty\": \"index\"\n" +
            "      },\n" +
            "      \"oldDevCards\": {\n" +
            "        \"monopoly\": \"index\",\n" +
            "        \"monument\": \"index\",\n" +
            "        \"roadBuilding\": \"index\",\n" +
            "        \"soldier\": \"index\",\n" +
            "        \"yearOfPlenty\": \"index\"\n" +
            "      },\n" +
            "      \"playerIndex\": \"index\",\n" +
            "      \"playedDevCard\": \"boolean\",\n" +
            "      \"playerID\": \"integer\",\n" +
            "      \"resources\": {\n" +
            "        \"brick\": \"integer\",\n" +
            "        \"ore\": \"integer\",\n" +
            "        \"sheep\": \"integer\",\n" +
            "        \"wheat\": \"integer\",\n" +
            "        \"wood\": \"integer\"\n" +
            "      },\n" +
            "      \"roads\": \"index\",\n" +
            "      \"settlements\": \"integer\",\n" +
            "      \"soldiers\": \"integer\",\n" +
            "      \"victoryPoints\": \"integer\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"tradeOffer\": {\n" +
            "    \"sender\": \"integer\",\n" +
            "    \"receiver\": \"integer\",\n" +
            "    \"offer\": {\n" +
            "      \"brick\": \"integer\",\n" +
            "      \"ore\": \"integer\",\n" +
            "      \"sheep\": \"integer\",\n" +
            "      \"wheat\": \"integer\",\n" +
            "      \"wood\": \"integer\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"turnTracker\": {\n" +
            "    \"currentTurn\": \"index\",\n" +
            "    \"status\": \"string\",\n" +
            "    \"longestRoad\": \"index\",\n" +
            "    \"largestArmy\": \"index\"\n" +
            "  },\n" +
            "  \"version\": \"index\",\n" +
            "  \"winner\": \"index\"\n" +
            "}";

    public final String AI = "[\n" +
            "  \"LARGEST_ARMY\"\n" +
            "]";
}
