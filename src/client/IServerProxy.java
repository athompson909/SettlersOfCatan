package client;
import exceptions.ClientException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mitchell on 9/15/2016.
 */
public interface IServerProxy {

   /**
    * Posts HTTP
    * @param url the url determined by other methods within IServerProxy
    * @param postData the data to be posted to the server
    * @return response text from server if successful
    */
   String httpPost(String url, String postData) throws ClientException; //I'M CHANGING THE PARAMETER FROM TYPE JSONObject to String

   /**
    * HTTP Get Method
    * @param url the url determined by other methods within IServerProxy
    * @return response text from server
    */
   String httpGet(String url) throws ClientException;

//Non-Move

   /**
    *Logs the caller in to the server, and sets their catan.user HTTP cookie
    * @pre username is not null
   password is not null
    * @post If the passed¬in (username, password) pair is valid,
   1. The server returns an HTTP 200 success response with “Success” in the body.
   2. The HTTP response headers set the catan.user cookie to contain the identity of the
   logged¬in player. The cookie uses ”Path=/”, and its value contains a url¬encoded JSON object of
   the following form: { “name”: JSONOBJECT, “password”: JSONOBJECT, “playerID”: INTEGER }. For
   example, { “name”: “Rick”, “password”: “secret”, “playerID”: 14 }.
   If the passed¬in (username, password) pair is not valid, or the operation fails for any other
   reason,
   1. The server returns an HTTP 400 error response, and the body contains an error
    * //@param json - username and password passed in as a string in the following JSON format:
    *                    { username: "username", password: "password"}
    * @return Model in JSON
    */
   String userLogin(JSONObject json) throws ClientException;

   /**
    * 1) Creates a new user account
    2) Logs the caller in to the server as the new user, and sets their catan.user HTTP cookie
    * @pre username is not null, password is not null, The specified username is not already in use.
    * @post If there is no existing user with the specified username,
   1. A new user account has been created with the specified username and password.
   2. The server returns an HTTP 200 success response with “Success” in the body.
   3. The HTTP response headers set the catan.user cookie to contain the identity of the
   logged¬in player. The cookie uses ”Path=/”, and its value contains a url¬encoded JSON object of
   the following form: { “name”: JSONOBJECT, “password”: JSONOBJECT, “playerID”: INTEGER }. For
   example, { “name”: “Rick”, “password”: “secret”, “playerID”: 14 }.
   If there is already an existing user with the specified name, or the operation fails for any other
   reason,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
    * @param json -username:JSONObject, password:JSONObject
    * @return Model in JSON
    */
   String userRegister(JSONObject json) throws ClientException;

   /**
    * Returns information about all of the current games on the server
    * @pre None
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response.
   2. The body contains a JSON array containing a list of objects that contain information
   about the server’s games
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
    * @return Model in JSON
    */
   JSONArray gamesList() throws ClientException;

   /**
    *Creates a new game on the server.
    * @pre name != null; randomTiles, randomNumbers, and randomPorts contain valid boolean values
    * @post If the operation succeeds,
   1. A new game with the specified properties has been created
   2. The server returns an HTTP 200 success response.
   3. The body contains a JSON object describing the newly created game
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
    * @param json - name:JSONObject, randomTiles:bool, randomNumbers:bool, randomPorts:bool
    * @return Model in JSON
    */
   JSONObject gameCreate(JSONObject json) throws ClientException;

   /**
    * Adds the player to the specified game and sets their catan.game cookie
    * @pre 1. The user has previously logged in to the server (i.e., they have a valid catan.user HTTP
   cookie).
   2. The player may join the game because
   2.a They are already in the game, OR
   2.b There is space in the game to add a new player
   3. The specified game ID is valid
   4. The specified color is valid (red, green, blue, yellow, puce, brown, white, purple,
   orange)
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response with “Success” in the body.
   2. The player is in the game with the specified color (i.e. calls to /games/list method will
   show the player in the game with the chosen color).
   3. The server response includes the “Set¬cookie” response header setting the catan.game
   HTTP cookie
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
    * @param json - gameID:int, color:JSONObject
    * @return Model in JSON
    */
   String gameJoin(JSONObject json) throws ClientException;


   /**
    * this function is used by ServerPoller
    * @return true if the user has already joined a specific game
    */
   boolean hasJoined() throws ClientException;

   /**
    * This method is for testing and debugging purposes. When a bug is found, you can use the
    /games/save method to save the state of the game to a file, and attach the file to a bug report.
    A developer can later restore the state of the game when the bug occurred by loading the
    previously saved file using the /games/load method. Game files are saved to and loaded from
    the server's saves/ directory.

    * @pre 1. The specified game ID is valid
   2. The specified file name is valid (i.e., not null or empty)
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response with “Success” in the body.
   2. The current state of the specified game (including its ID) has been saved to the
   specified file name in the server’s saves/ directory
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message
    * @param json - gameID:int, fileName:file
    * @return Model in JSON
    */
   String gameSave(JSONObject json) throws ClientException;

   /**
    * This method is for testing and debugging purposes. When a bug is found, you can use the
    /games/save method to save the state of the game to a file, and attach the file to a bug report.
    A developer can later restore the state of the game when the bug occurred by loading the
    previously saved file using the /games/load method. Game files are saved to and loaded from
    the server's saves/ directory
    * @pre 1. A previously saved game file with the specified name exists in the server’s saves/
   directory.
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response with “Success” in the body.
   2. The game in the specified file has been loaded into the server and its state restored
   (including its ID).
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
    * @param json - fileName:file
    * @return Model in JSON
    */
   String gameLoad(JSONObject json) throws ClientException;

   /**
    * Returns the current state of the game in JSON format.
    In addition to the current game state, the returned JSON also includes a “version” number for
    the client model. The next time /game/model is called, the version number from the
    previously retrieved model may optionally be included as a query parameter in the request
    (/game/model?version=N). The server will only return the full JSON game state if its version
    number is not equal to N. If it is equal to N, the server returns “true” to indicate that the caller
    already has the latest game state. This is merely an optimization. If the version number is
    not included in the request URL, the server will return the full game state.
    * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
   valid catan.user and catan.game HTTP cookies).
   2. If specified, the version number is included as the “version” query parameter in the
   request URL, and its value is a valid integer
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response.
   2. The response body contains JSON data
   a. The full client model JSON is returned if the caller does not provide a version
   number, or the provide version number does not match the version on the server
   b. “true” (true in double quotes) is returned if the caller provided a version number,
   and the version number matched the version number on the server
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
   The format of the returned JSON can be found on the server’s Swagger page, or in the document
   titled “client Model JSON Documentation”
    * removing this: @param/modelVer - version:int
    * @return JSON model in String format or "true" if version is already up to date,
    *  will be needed to be later converted into a JSONObject
    */
   String gameModelVersion() throws ClientException;

   /**
    * Clears out the command history of the current game.
    For the default games created by the server, this method reverts the game to the state
    immediately after the initial placement round. For user¬created games, this method reverts
    the game to the very beginning (i.e., before the initial placement round).
    This method returns the client model JSON for the game after it has been reset.
    You must login and join a game before calling this method.
    * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
   valid catan.user and catan.game HTTP cookies).
    * @post If the operation succeeds,
   1. The game’s command history has been cleared out
   2. The game’s players have NOT been cleared out
   3. The server returns an HTTP 200 success response.
   4. The body contains the game’s updated client model JSON
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
   Note:
   When a game is reset, the players in the game are maintained
    * @return Model in JSON
    */
   JSONObject gameReset() throws ClientException;

   /**
    * Returns a list of commands that have been executed in the current game.
    This method can be used for testing and debugging. The command list returned by this
    method can be passed to the /game/command (POST) method to re¬execute the commands
    in the game. This would typically be done after calling /game/reset to clear out the game’s
    command history. This is one way to capture the state of a game and restore it later. (See
    the /games/save and /games/load methods which provide another way to save and restore
    the state of a game.)
    For the default games created by the server, this method returns a list of all commands that
    have been executed after the initial placement round. For user¬created games, this method
    returns a list of all commands that have been executed since the very beginning of the game
    (i.e., before the initial placement round).
    You must login and join a game before calling this method
    * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
   valid catan.user and catan.game HTTP cookies)
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response.
   2. The body contains a JSON array of commands that have been executed in the game.
   This command array is suitable for passing back to the /game/command [POST] method to
   restore the state of the game later (after calling /game/reset to revert the game to its initial state).
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message
    * @return Model in JSON
    */
   JSONArray getGameCommands() throws ClientException;

   /**
    * Executes the specified command list in the current game.
    This method can be used for testing and debugging. The command list returned by the
    /game/command [GET] method is suitable for passing to this method.
    This method returns the client model JSON for the game after the command list has been
    applied.
    You must login and join a game before calling this method
    * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
   valid catan.user and catan.game HTTP cookies).
    * @post If the operation succeeds,
   1. The passed¬in command list has been applied to the game.
   2. The server returns an HTTP 200 success response.
   3. The body contains the game’s updated client model JSON
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message
    * @param json array of commands
    * @return Model in JSON
    */
   JSONObject executeGameCommands(JSONArray json) throws ClientException;

   /**
    * Returns a list of supported AI player types.
    Currently, LARGEST_ARMY is the only supported type.
    * @pre None
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response.
   2. The body contains a JSON JSONObject array enumerating the different types of AI players.
   These are the values that may be passed to the /game/addAI method.
    * @return a list of all AI's in a JSONArray
    */
   JSONArray listAI() throws ClientException;

   /**
    * Adds an AI player to the current game.
    You must login and join a game before calling this method
    * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
   valid catan.user and catan.game HTTP cookies).
   2. There is space in the game for another player (i.e., the game is not “full”).
   3. The specified “AIType” is valid (i.e., one of the values returned by the /game/listAI
   method)
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response with “Success” in the body.
   2. A new AI player of the specified type has been added to the current game. The server
   selected a name and color for the player.
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message
    * @return Model in JSON
    */
   String addAI(JSONObject json) throws ClientException;

   /**
    * Sets the server’s logging level
    * @pre 1.The caller specifies a valid logging level. Valid values include: SEVERE, WARNING,
   INFO, CONFIG, FINE, FINER, FINEST
    * @post If the operation succeeds,
   1. The server returns an HTTP 200 success response with “Success” in the body.
   2. The Server is using the specified logging level
   If the operation fails,
   1. The server returns an HTTP 400 error response, and the body contains an error
   message.
    * @param json - loggingLevel:LoggingLevel
    * @return "Success" or failure
    */
   String utilChangeLogLevel(JSONObject json) throws ClientException;


   //Move

   /**
    * Adds a message to the end of the chat
    * @pre Caller has already logged in to the server and joined a game.
    * @post The chat contains your message at the end
    * @param json - playerIndex:int, content:JSONObject
    * @return Model in JSON
    */
   JSONObject sendChat(JSONObject json) throws ClientException;

   /**
    * Tells the server what number was rolled so resources can be distributed, discarded or robbed.
    * @pre Caller has already logged in to the server and joined a game. It is your turn. The client model’s status is ‘Rolling’
    * @post The client model’s status is now in ‘Discarding’ or ‘Robbing’ or ‘Playing’
    * @param json - playerIndex:int, number:int(2-12)
    * @return Model in JSON
    */
   JSONObject rollNumber(JSONObject json) throws ClientException;

   /**
    * Ends the players turn.
    * @pre Caller has already logged in to the server and joined a game.
    * @post The cards in your new dev card hand have been transferred to your old dev card
   hand. It is the next player’s turn
    * @param json - playerIndex:int
    * @return Model in JSON
    */
   JSONObject finishTurn(JSONObject json) throws ClientException;

   /**
    * Tells Server what cards to remove from the player's hand.
    * @pre Caller has already logged in to the server and joined a game. The status of the client model is 'Discarding'.
    * You have over 7 cards. You have the cards you're choosing to discard.
    * @post You gave up the specified resources. If you're the last one to discard, the client model status changes to 'Robbing'
    * @param json - playerIndex:int, discardedCards:ResourceList
    * @return Model in JSON
    */
   JSONObject discardCards(JSONObject json) throws ClientException;

   /**
    * Tells Server to build a road for the given player in the given location.
    * @pre Caller has already logged in to the server and joined a game. The road location is open.
    * The road location is connected to another road owned by the player. The road location is not on water.
    * You have the required resources (1 wood, 1 brick; 1 road).
    * Setup round: Must be placed by settlement owned by the player with no adjacent
   road
    * @post You lost the resources required to build a road (1 wood, 1 brick; 1 road).
    * The road is on the map at the specified location.
    * If applicable, “longest road” has been awarded to the player with the longest road
    * @param json - playerIndex:int, roadLocation:HexLocation, free:bool
    * @return Model in JSON
    */
   JSONObject buildRoad(JSONObject json) throws ClientException;

   /**
    * Tells Server to build a settlement for the given player in the given location.
    * @pre Caller has already logged in to the server and joined a game. The settlement location is open.
    * The settlement location is not on water.
   The settlement location is connected to one of your roads except during setup.
   You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement).
   The settlement cannot be placed adjacent to another settlement
    * @post You lost the resources required to build a settlement if not setup rounds(1 wood, 1 brick, 1 wheat, 1
   sheep; 1 settlement).
   The settlement is on the map at the specified location
    * @param json - playerIndex:int, vertexLocation:VertexObject, free:bool
    * @return Model in JSON
    */
   JSONObject buildSettlement(JSONObject json) throws ClientException;

   /**
    * Tells Server to build a city for the given player in the given location.
    * @pre Caller has already logged in to the server and joined a game. The city location is where you currently have a settlement.
    * You have the required resources (2 wheat, 3 ore; 1 city)
    * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city).
    * The city is on the map at the specified location.
    * You got a settlement back
    * @param json - playerIndex:int, vertexLocation:VertexObject
    * @return Model in JSON
    */
   JSONObject buildCity(JSONObject json) throws ClientException;

   /**
    * Tells Server to send a trade offer to the other player.
    * @pre Caller has already logged in to the server and joined a game. You have the resources you are offering.
    * @post The trade is offered to the other player (stored in the server model).
    * @param json - playerIndex:int, offer:ResourceList, receiver:int)
    * @return Model in JSON
    */
   JSONObject offerTrade(JSONObject json) throws ClientException;

   /**
    * Tells the Server to trade the players' cards.
    * @pre Caller has already logged in to the server and joined a game. You have been offered a domestic trade.
    * To accept the offered trade, you have the required resources
    * @post If you accepted, you and the player who offered swap the specified resources.
    * If you declined no resources are exchanged.
    * The trade offer is removed
    * @param json - playerIndex:int, willAccept:bool
    * @return Model in JSON
    */
   JSONObject acceptTrade(JSONObject json) throws ClientException;

   /**
    * Tells the Server to trade bank and player cards.
    * @pre Caller has already logged in to the server and joined a game. You have the resources you are giving.
    * For ratios less than 4, you have the correct port for the trade
    * @post The trade has been executed (the offered resources are in the bank, and the
   requested resource has been received)
    * @param json - playerIndex:int, ratio:int(2,3 or4), inputResource:Resource, outputResource:Resource
    * @return Model in JSON
    */
   JSONObject maritimeTrade(JSONObject json) throws ClientException ;

   /**
    * Tells the Server to move the robber and move the stolen card.
    * @pre Caller has already logged in to the server and joined a game. The robber is not being kept in the same location.
    * If a player is being robbed (i.e., victimIndex != ¬1), the player being robbed has
   resource cards
    * @post The robber is in the new location. The player being robbed (if any) gave you one of his resource cards (randomly
   selected)
    * @param json - playerIndex:int, location:HexLocation, victimIndex:int(-1,0,1,2,or 3)
    * @return Model in JSON
    */
   JSONObject robPlayer(JSONObject json) throws ClientException;

   /**
    * Tells the Server to give the player a new Development Card.
    * @pre Caller has already logged in to the server and joined a game. You have the required resources (1 ore, 1 wheat, 1 sheep).
    * There are dev cards left in the deck
    * @post You have a new card
   - If it is a monument card, it has been added to your old devcard hand
   - If it is a non¬monument card, it has been added to your new devcard hand
   (unplayable this turn)
    * @param json - playerIndex:int
    * @return Model in JSON
    */
   JSONObject purchaseDevCard(JSONObject json) throws ClientException;

   /**
    * Tells the Server to allow the player to rob another player.
    * @pre Caller has already logged in to the server and joined a game. The robber is not being kept in the same location.
    * If a player is being robbed (i.e., victimIndex != ¬1), the player being robbed has
   resource cards
    * @post The robber is in the new location.
    * The player being robbed (if any) gave you one of his resource cards (randomly
   selected).
   If applicable, “largest army” has been awarded to the player who has played the
   most soldier cards.
   You are not allowed to play other development cards during this turn (except for
   monument cards, which may still be played)
    * @param json - playerIndex:int, location:HexLocation, victimIndex:int(-1,0,1,2,or 3)
    * @return Model in JSON
    */
   JSONObject playSoldier(JSONObject json) throws ClientException;

   /**
    * Tells the Server to allow the player to pick two resources from the Bank
    * @pre Caller has already logged in to the server and joined a game. The two specified resources are in the bank
    * @post You gained the two specified resources
    * @param json - playerIndex:int, resource1:Resource, resource2:Resource
    * @return Model in JSON
    */
   JSONObject playYearOfPlenty(JSONObject json) throws ClientException;

   /**
    * Tells the Server to allow the player to build two roads for free.
    * @pre Caller has already logged in to the server and joined a game. The first road location (spot1) is connected to one of your roads..
    * The second road location (spot2) is connected to one of your roads or to the first
   road location (spot1).
   Neither road location is on water.
   You have at least two unused roads
    * @post You have two fewer unused roads.
    * Two new roads appear on the map at the specified locations.
    * If applicable, “longest road” has been awarded to the player with the longest road
    * @param json - playerIndex:int, spot1:EdgeLocation, spot2:EdgeLocation
    * @return Model in JSON
    */
   JSONObject playRoadBuilding(JSONObject json) throws ClientException;

   /**
    * Tells the Server to allow the player to pick a resource to gather from the other players.
    * @pre Caller has already logged in to the server and joined a game.
    * @post All of the other players have given you all of their resource cards of the specified
   type
    * @param json - playerIndex:int, resource:Resource
    * @return Model in JSON
    */
   JSONObject playMonopoly(JSONObject json) throws ClientException;

   /**
    * Tells the Server to add one victory point to the player.
    * @pre Caller has already logged in to the server and joined a game.
    * You have enough monument cards to win the game (i.e., reach 10 victory points)
    * @post You gained a victory point.
    * @param json - playerIndex:int
    * @return Model in JSON
    */
   JSONObject playMonument(JSONObject json) throws ClientException;

}