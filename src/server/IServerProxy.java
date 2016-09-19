package server;

/**
 * Created by Mitchell on 9/15/2016.
 */
public interface IServerProxy {
    String url = "";

   boolean httpPost(String json);

    boolean httpGet(String json);

    //Non-Move

 /**
  *Logs the caller in to the server, and sets their catan.user HTTP cookie
  * @pre username is not null
 password is not null
  * @post If the passed­in (username, password) pair is valid,
 1. The server returns an HTTP 200 success response with “Success” in the body.
 2. The HTTP response headers set the catan.user cookie to contain the identity of the
 logged­in player. The cookie uses ”Path=/”, and its value contains a url­encoded JSON object of
 the following form: { “name”: STRING, “password”: STRING, “playerID”: INTEGER }. For
 example, { “name”: “Rick”, “password”: “secret”, “playerID”: 14 }.
 If the passed­in (username, password) pair is not valid, or the operation fails for any other
 reason,
 1. The server returns an HTTP 400 error response, and the body contains an error
  * @param json -username and password
  */
    void userLogin(String json);

    /**
     * 1) Creates a new user account
     2) Logs the caller in to the server as the new user, and sets their catan.user HTTP cookie
     * @pre username is not null, password is not null, The specified username is not already in use.
     * @post If there is no existing user with the specified username,
    1. A new user account has been created with the specified username and password.
    2. The server returns an HTTP 200 success response with “Success” in the body.
    3. The HTTP response headers set the catan.user cookie to contain the identity of the
    logged­in player. The cookie uses ”Path=/”, and its value contains a url­encoded JSON object of
    the following form: { “name”: STRING, “password”: STRING, “playerID”: INTEGER }. For
    example, { “name”: “Rick”, “password”: “secret”, “playerID”: 14 }.
    If there is already an existing user with the specified name, or the operation fails for any other
    reason,
    1. The server returns an HTTP 400 error response, and the body contains an error
    message.
     * @param json -username and password
     */
    void userRegister(String json);

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
     * @param json -
     */
    void gamesList(String json);

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
     * @param json - name, randomTiles, randomNumbers, randomPorts
     */
    void gameCreate(String json);

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
    3. The server response includes the “Set­cookie” response header setting the catan.game
    HTTP cookie
    If the operation fails,
    1. The server returns an HTTP 400 error response, and the body contains an error
    message.
     * @param json
     */
    void gameJoin(String json);

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
     * @param json
     */
    void gameSave(String json);

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
     * @param json
     * @return
     */
    String gameLoad(String json);

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
    titled “Client Model JSON Documentation”
     * @param json
     */
    void gameModelVersion(String json);

    /**
     * Clears out the command history of the current game.
     For the default games created by the server, this method reverts the game to the state
     immediately after the initial placement round. For user­created games, this method reverts
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
     * @param json
     */
    void gameReset(String json);

    /**
     * Returns a list of commands that have been executed in the current game.
     This method can be used for testing and debugging. The command list returned by this
     method can be passed to the /game/command (POST) method to re­execute the commands
     in the game. This would typically be done after calling /game/reset to clear out the game’s
     command history. This is one way to capture the state of a game and restore it later. (See
     the /games/save and /games/load methods which provide another way to save and restore
     the state of a game.)
     For the default games created by the server, this method returns a list of all commands that
     have been executed after the initial placement round. For user­created games, this method
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
     * @param json
     */
    void getGameCommands(String json);

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
    1. The passed­in command list has been applied to the game.
    2. The server returns an HTTP 200 success response.
    3. The body contains the game’s updated client model JSON
    If the operation fails,
    1. The server returns an HTTP 400 error response, and the body contains an error
    message
     * @param json
     */
    void executeGameCommands(String json);

    /**
     * Returns a list of supported AI player types.
     Currently, LARGEST_ARMY is the only supported type.
     * @pre None
     * @post If the operation succeeds,
    1. The server returns an HTTP 200 success response.
    2. The body contains a JSON string array enumerating the different types of AI players.
    These are the values that may be passed to the /game/addAI method.
     * @param json
     */
    void listAI(String json);

    
    void addAI(String json);

    void utilChangeLogLevel(String json);

    //Move
    void sendChat(String json);

    void rollNumber(String json);

    void finishTurn(String json);

    void discardCards(String json);

    void buildRoad(String json);

    void buildSettlement(String json);

    void buildCity(String json);

    void offerTrade(String json);

    void acceptTrade(String json);

    void maritimeTrade(String json);

    void robPlayer(String json);

    void purchaseDevCard(String json);

    void playSoldier(String json);

    void playYearOfPlenty(String json);

    void playRoadBuilding(String json);

    void playMonopoly(String json);

    void playMonument(String json);

}
