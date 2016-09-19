/**
 * Created by Alise on 9/17/2016.
 */

import java.io.File;

import Client.model.LoggingLevel;
import Client.model.map.HexLocation;
import Client.model.map.VertexObject;
import Client.model.map.EdgeLocation;
import Client.model.resourcebank.Resource;
import Client.model.resourcebank.ResourceList;

public class ClientFacade {
    /**
     * Logs the caller in to the server, and sets their catan.user HTTP cookie
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
     * @param username
     * @param password
     */
    public void userLogin(String username, String password){

    }

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
     * @param username
     * @param password
     */
    public void userRegister(String username, String password){

    }

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
     */
    public void gamesList(){

    }

    /**
     * Creates a new game on the server.
     * @pre name != null; randomTiles, randomNumbers, and randomPorts contain valid boolean values
     * @post If the operation succeeds,
    1. A new game with the specified properties has been created
    2. The server returns an HTTP 200 success response.
    3. The body contains a JSON object describing the newly created game
    If the operation fails,
    1. The server returns an HTTP 400 error response, and the body contains an error
    message.
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     */
    public void gameCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts){

    }

    /**
     *  Adds the player to the specified game and sets their catan.game cookie
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
     * @param gameID
     * @param color
     */
    public void gameJoin(int gameID, String color){

    }

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
     * @param gameID
     * @param fileName
     */
    public void gameSave(int gameID,File fileName){

    }

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
     * @param fileName
     */
    public void gameLoad(File fileName){

    }

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
     * @param version
     */
    public void gameModelVersion(int version){

    }

    /**
     *
     */
    public void gameReset(){

    }

    /**
     *
     */
    public void getGameCommands(){

    }

    /**
     *
     */
    public void executeGameCommands(){

    }

    /**
     *
     */
    public void listAI(){

    }

    /**
     *
     */
    public void addAI(){

    }

    /**
     *
     * @param loggingLevel
     */
    public void utilChangeLogLevel(LoggingLevel loggingLevel){

    }



    //Move

    /**
     *
     * @param playerIndex
     * @param content
     */
    public void sendChat(int playerIndex, String content){

    }

    /**
     *
     * @param playerIndex
     * @param number
     */
    public void rollNumber(int playerIndex, int number){

    }

    /**
     *
     * @param playerIndex
     */
    public void finishTurn(int playerIndex){

    }

    /**
     *
     * @param playerIndex
     * @param discardedCards
     */
    public void discardCards(int playerIndex, ResourceList discardedCards){

    }

    /**
     *
     * @param playerIndex
     * @param roadLocation
     * @param free
     */
    public void buildRoad(int playerIndex, HexLocation roadLocation, boolean free){

    }

    /**
     *
     * @param playerIndex
     * @param vertexLocation
     * @param free
     */
    public void buildSettlement(int playerIndex, VertexObject vertexLocation, boolean free){

    }

    /**
     *
     * @param playerIndex
     * @param vertexLocation
     */
    public void buildCity(int playerIndex, VertexObject vertexLocation){

    }

    /**
     *
     * @param playerIndex
     * @param offer
     * @param receiver
     */
    public void offerTrade(int playerIndex, ResourceList offer, int receiver){

    }

    /**
     *
     * @param playerIndex
     * @param willAccept
     */
    public void acceptTrade(int playerIndex, boolean willAccept){

    }

    /**
     *
     * @param playerIndex
     * @param ratio
     * @param inputResource
     * @param outputResource
     */
    public void maritimeTrade(int playerIndex, int ratio, Resource inputResource, Resource outputResource){

    }

    /**
     *
     * @param playerIndex
     * @param location
     * @param victimIndex
     */
    public void robPlayer(int playerIndex, HexLocation location, int victimIndex){

    }

    /**
     *
     * @param playerIndex
     */
    public void purchaseDevCard(int playerIndex){

    }

    /**
     *
     * @param playerIndex
     * @param location
     * @param victimIndex
     */
    public void playSoldier(int playerIndex, HexLocation location, int victimIndex){

    }

    /**
     *
     * @param playerIndex
     * @param resource1
     * @param resource2
     */
    public void playYearOfPlenty(int playerIndex, Resource resource1, Resource resource2){

    }

    /**
     *
     * @param playerIndex
     * @param spot1
     * @param spot2
     */
    public void playRoadBuilding(int playerIndex, EdgeLocation spot1, EdgeLocation spot2){

    }

    /**
     *
     * @param playerIndex
     * @param resource
     */
    public void playMonopoly(int playerIndex, Resource resource){

    }

    /**
     *
     * @param playerIndex
     */
    public void playMonument(int playerIndex){

    }
}
