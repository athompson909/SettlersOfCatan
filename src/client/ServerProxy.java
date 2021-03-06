package client;

import exceptions.ClientException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 *
 * ServerProxy implements the IServerProxy interface.
 * Unlike the MockProxy, ServerProxy actually sends requests to/receives
 * responses from to the real server.
 *
 * Created by Sierra on 9/18/16.
 *
 * Adam is working on this
 */
public class ServerProxy implements IServerProxy {

    private boolean isLogin = false;

    private boolean hasLoggedIn = false;

    private boolean isRegister = false;

    private boolean isJoin = false;

    private boolean hasJoined = false;

    private boolean pollerCanRun = false;

    private String host = "";

    private String port = "";

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        System.out.println(">SERVERPROXY: set Host# to " + host);

        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public void setPort(String port) {
        System.out.println(">SERVERPROXY: set Port# to " + port);

        this.port = port;
    }

    /**
     * this function is used by ServerPoller
     * @return hasJoined
     */
    @Override
    public boolean hasJoined() { return hasJoined; }

    @Override
    public void setHasJoined(boolean hasJoined) {
        this.hasJoined = hasJoined;
    }

    @Override
    public boolean runPoller() throws ClientException {
        return pollerCanRun;
    }

    @Override
    public void startPoller() throws ClientException {
        pollerCanRun = true;
    }

    /**
     * the cookie returned when userLogin(...) is ran
     *  "all subsequent web service calls
     * should include an HTTP Cookie header that includes both the catan.user and
     * catan.game cookies"
     */
    private String loginCookie;

    /**
     * the cookie returned when userRegister(...) is ran
     *  "all subsequent web service calls
     * should include an HTTP Cookie header that includes both the catan.user and
     * catan.game cookies"
     * todo: delete
     */
    private String registerCookie;

    private String joinCookie;

    public String getLoginCookie() {
        return loginCookie;
    }

    public String getRegisterCookie() {
        return registerCookie;
    }

    public String getJoinCookie() {
        return joinCookie;
    }

    public void setLoginCookie(String loginCookie) {
        this.loginCookie = loginCookie;
    }

    public void setRegisterCookie(String registerCookie) {
        this.registerCookie = registerCookie;
    }

    public void setJoinCookie(String joinCookie) {

    System.out.println(">SERVERPROXY: just set joinCookie: " + joinCookie);
        this.joinCookie = joinCookie;
    }

    /**
     * Posts HTTP
     *
     * @param url the url determined by other methods within IServerProxy
     * @param postData - string in JSON format used to send with the HTTP post request
     * @return response from http
     */
    @Override
    public String httpPost(String url, String postData) throws ClientException {

        try {

            URL obj = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("POST");
            setCookies(connection);
            connection.setDoOutput(true);
            connection.connect();

            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(postData.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                getCookies(connection);

                InputStream responseBody = connection.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                return baos.toString();
            }
            else return "{\"http error " + connection.getResponseCode() + "\":\"" + connection.getResponseMessage() + "\"}";


        } catch (MalformedURLException e) {
            System.out.println("Error: bad url");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: probably caused by obj.openConnection()");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: other exception");
            e.printStackTrace();
        } finally {
            if (isJoin) isJoin = false;
        }

        return "error, exception thrown";
    }

    /**
     * gets cookies from server for when user is logging in/registering/joining a game
     *
     * @param connection the connection after response code has been verified to be "200"
     */
    private void getCookies(HttpURLConnection connection) throws ClientException {

        if (isLogin) {
            String fullCookieStr = connection.getHeaderFields().get("Set-cookie").get(0);
            // to take off the unneeded parts of the header:
            String undecodedLoginCookie = fullCookieStr.substring(11, fullCookieStr.length() - 8);
            setLoginCookie(undecodedLoginCookie);

            saveUserIDFromLoginCookie();
            hasLoggedIn = true;
            isLogin = false;
        }
        if (isRegister) {
            String fullCookieStr = connection.getHeaderFields().get("Set-cookie").get(0);
            String undecodedRegisterCookie = fullCookieStr.substring(11, fullCookieStr.length() - 8);
            setLoginCookie(undecodedRegisterCookie);
            //setRegisterCookie(URLDecoder.decode(undecodedRegisterCookie));

            saveUserIDFromLoginCookie();  //TEST

            hasLoggedIn = true;
            isRegister = false;
        }
        if (isJoin) {
            String fullCookieStr = connection.getHeaderFields().get("Set-cookie").get(0);
            String undecodedJoinCookie = fullCookieStr.substring(11, fullCookieStr.length() - 8);
            setJoinCookie(undecodedJoinCookie);
            //setRegisterCookie(URLDecoder.decode(undecodedJoinCookie));

            isJoin = false;

        }
    }

    /**
     * This function is called right when the loginCookie is saved,
     * so we can save the user's playerID to the ClientUser singleton.
     *  - Sierra
     */
    public void saveUserIDFromLoginCookie() {
        //GET THEIR ID OUT OF THE COOKIE
        String rawCookie = getLoginCookie();
        String decodedCookie = null;
        try{
            decodedCookie = URLDecoder.decode(rawCookie, "UTF-8");
            //I'm putting it in a JSONObject so I can pull stuff out of it easier
            JSONObject decodedCookieJSON = new JSONObject(decodedCookie);
            int userID = decodedCookieJSON.getInt("playerID");
            ClientUser.getInstance().setId(userID);
        }
        catch (UnsupportedEncodingException bad){
            System.out.println(">SERVERPROXY: GETCOOKIES: Could not decode cookie");
        }
    }


    /**
     * sets in game cookies
     *
     * included in connection exists a map: Map<String, List<String>> headers = connection.getHeaderFields();
     * @param connection the connection that was just initialized with request method set
     */
    private void setCookies(HttpURLConnection connection) throws ClientException {
        if (hasJoined) {
            connection.setRequestProperty("Cookie", "catan.user=" + loginCookie + "; catan.game=" + joinCookie);
            return;
        }
        else if(hasLoggedIn) {
            connection.setRequestProperty("Cookie", "catan.user=" + loginCookie);
        }

        if (isJoin) {
//            connection.setRequestProperty("Cookie", "catan.user=" + loginCookie);
            hasJoined = true;
        }

    }

    /**
     * HTTP Get Method
     *
     * @param url the url determined by other methods within IServerProxy
     * @return response from http
     */
    @Override
    public String httpGet(String url) throws ClientException {

        try {

            URL obj = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");
            setCookies(connection);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream responseBody = connection.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                return baos.toString();
            }
            else return "{\"http error " + connection.getResponseCode() + "\":\"" + connection.getResponseMessage() + "\"}";

        } catch (MalformedURLException e) {
            System.out.println("Error: bad url");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: probably caused by obj.openConnection()");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: other exception");
            e.printStackTrace();
        }

        return "error, exception thrown";
    }

    /**
     * Logs the caller in to the server, and sets their catan.user HTTP cookie
     *
     * @param json - username:string, password:string
     * @return Model in JSON
     * @pre username is not null
     * password is not null
     * @post If the passed­in (username, password) pair is valid,
     * 1. The server returns an HTTP 200 success response with “Success” in the body.
     * 2. The HTTP response headers set the catan.user cookie to contain the identity of the
     * logged­in player. The cookie uses ”Path=/”, and its value contains a url­encoded JSON object of
     * the following form: { “name”: STRING, “password”: STRING, “playerID”: INTEGER }. For
     * example, { “name”: “Rick”, “password”: “secret”, “playerID”: 14 }.
     * If the passed­in (username, password) pair is not valid, or the operation fails for any other
     * reason,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     *
     * [Adam]
     * //@param loginParams - username and password passed in as a string in the following JSON format:
     *                    { username: "username", password: "password"}
     */
    @Override
    public String userLogin(JSONObject json) throws ClientException {

        System.out.println(">>SERVERPROXY: userLogin has json: " + json);

        isLogin = true;
        String urlStr = "http://"+host+":"+port+"/user/login";
        return httpPost(urlStr, json.toString());
    }

    /**
     * 1) Creates a new user account
     * 2) Logs the caller in to the server as the new user, and sets their catan.user HTTP cookie
     *
     * @param json -username:string, password:string
     * @return Model in JSON
     * @pre username is not null, password is not null, The specified username is not already in use.
     * @post If there is no existing user with the specified username,
     * 1. A new user account has been created with the specified username and password.
     * 2. The server returns an HTTP 200 success response with “Success” in the body.
     * 3. The HTTP response headers set the catan.user cookie to contain the identity of the
     * logged­in player. The cookie uses ”Path=/”, and its value contains a url­encoded JSON object of
     * the following form: { “name”: STRING, “password”: STRING, “playerID”: INTEGER }. For
     * example, { “name”: “Rick”, “password”: “secret”, “playerID”: 14 }.
     * If there is already an existing user with the specified name, or the operation fails for any other
     * reason,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     */
    @Override
    public String userRegister(JSONObject json) throws ClientException {

        System.out.println(">>SERVERPROXY: userRegister has json: " + json);


        isRegister = true;
        String urlStr = "http://"+host+":"+port+"/user/register";
        return httpPost(urlStr, json.toString());
    }

    /**
     * Returns information about all of the current games on the server
     *
     * @return Model in JSON
     * @pre None
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response.
     * 2. The body contains a JSON array containing a list of objects that contain information
     * about the server’s games
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     */
    @Override
    public JSONArray gamesList() throws ClientException {//todo change to JSONArray return type
        String urlStr = "http://"+host+":"+port+"/games/list";
        String jsonStr = httpGet(urlStr);

        return new JSONArray(jsonStr);
    }

    /**
     * Creates a new game on the server.
     *
     * @param json - name:string, randomTiles:bool, randomNumbers:bool, randomPorts:bool
     * @return Model in JSON
     * @pre name != null; randomTiles, randomNumbers, and randomPorts contain valid boolean values
     * @post If the operation succeeds,
     * 1. A new game with the specified properties has been created
     * 2. The server returns an HTTP 200 success response.
     * 3. The body contains a JSON object describing the newly created game
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     */
    @Override
    public JSONObject gameCreate(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/games/create";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Adds the player to the specified game and sets their catan.game cookie
     *
     * @param json - gameID:int, color:string
     * @return Model in JSON
     * @pre 1. The user has previously logged in to the server (i.e., they have a valid catan.user HTTP
     * cookie).
     * 2. The player may join the game because
     * 2.a They are already in the game, OR
     * 2.b There is space in the game to add a new player
     * 3. The specified game ID is valid
     * 4. The specified color is valid (red, green, blue, yellow, puce, brown, white, purple,
     * orange)
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response with “Success” in the body.
     * 2. The player is in the game with the specified color (i.e. calls to /games/list method will
     * show the player in the game with the chosen color).
     * 3. The server response includes the “Set­cookie” response header setting the catan.game
     * HTTP cookie
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     */
    @Override
    public String gameJoin(JSONObject json) throws ClientException {
        isJoin = true;
        String urlStr = "http://"+host+":"+port+"/games/join";
        return httpPost(urlStr, json.toString());
    }

    /**
     * This method is for testing and debugging purposes. When a bug is found, you can use the
     * /games/save method to save the state of the game to a file, and attach the file to a bug report.
     * A developer can later restore the state of the game when the bug occurred by loading the
     * previously saved file using the /games/load method. Game files are saved to and loaded from
     * the server's saves/ directory.
     *
     * @param json - gameID:int, fileName:file
     * @return Model in JSON
     * @pre 1. The specified game ID is valid
     * 2. The specified file name is valid (i.e., not null or empty)
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response with “Success” in the body.
     * 2. The current state of the specified game (including its ID) has been saved to the
     * specified file name in the server’s saves/ directory
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message
     */
    @Override
    public String gameSave(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/games/save";
        return httpPost(urlStr, json.toString());
    }

    /**
     * This method is for testing and debugging purposes. When a bug is found, you can use the
     * /games/save method to save the state of the game to a file, and attach the file to a bug report.
     * A developer can later restore the state of the game when the bug occurred by loading the
     * previously saved file using the /games/load method. Game files are saved to and loaded from
     * the server's saves/ directory
     *
     * @param json - fileName:file
     * @return Model in JSON
     * @pre 1. A previously saved game file with the specified name exists in the server’s saves/
     * directory.
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response with “Success” in the body.
     * 2. The game in the specified file has been loaded into the server and its state restored
     * (including its ID).
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     *
     * todo: test this when the server provides the right response when a valid game name is posted
     */
    @Override
    public String gameLoad(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/games/load";
        return httpPost(urlStr, json.toString());
    }

    /**
     * Returns the current state of the game in JSON format.
     * In addition to the current game state, the returned JSON also includes a “version” number for
     * the client model. The next time /game/model is called, the version number from the
     * previously retrieved model may optionally be included as a query parameter in the request
     * (/game/model?version=N). The server will only return the full JSON game state if its version
     * number is not equal to N. If it is equal to N, the server returns “true” to indicate that the caller
     * already has the latest game state. This is merely an optimization. If the version number is
     * not included in the request URL, the server will return the full game state.
     *
     * no longer passing this value in: @param/modelVer - version:int
     * @return String that either equals "true" or JSON model in string format (todo: convert to json)
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * 2. If specified, the version number is included as the “version” query parameter in the
     * request URL, and its value is a valid integer
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response.
     * 2. The response body contains JSON data
     * a. The full client model JSON is returned if the caller does not provide a version
     * number, or the provide version number does not match the version on the server
     * b. “true” (true in double quotes) is returned if the caller provided a version number,
     * and the version number matched the version number on the server
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     * The format of the returned JSON can be found on the server’s Swagger page, or in the document
     * titled “client Model JSON Documentation”
     *
     * [Adam]
     *   note: this loads the whole game model (why is the method named gameModelVersion()?)
     *   todo: explain to group why return type changed from JSONObject to String
     */
    @Override
    public String gameModelVersion() throws ClientException {

        String urlStr = "http://"+host+":"+port+"/game/model";
        return httpGet(urlStr);
    }

    /**
     * Clears out the command history of the current game.
     * For the default games created by the server, this method reverts the game to the state
     * immediately after the initial placement round. For user­created games, this method reverts
     * the game to the very beginning (i.e., before the initial placement round).
     * This method returns the client model JSON for the game after it has been reset.
     * You must login and join a game before calling this method.
     *
     * @return Model in JSON
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * @post If the operation succeeds,
     * 1. The game’s command history has been cleared out
     * 2. The game’s players have NOT been cleared out
     * 3. The server returns an HTTP 200 success response.
     * 4. The body contains the game’s updated client model JSON
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message.
     * Note:
     * When a game is reset, the players in the game are maintained
     * this method can only be ran when a player has joined a game
     */
    @Override
    public JSONObject gameReset() throws ClientException {

        String urlStr = "http://"+host+":"+port+"/game/reset";
        return new JSONObject(httpGet(urlStr));
    }

    /**
     * Returns a list of commands that have been executed in the current game.
     * This method can be used for testing and debugging. The command list returned by this
     * method can be passed to the /game/command (POST) method to re­execute the commands
     * in the game. This would typically be done after calling /game/reset to clear out the game’s
     * command history. This is one way to capture the state of a game and restore it later. (See
     * the /games/save and /games/load methods which provide another way to save and restore
     * the state of a game.)
     * For the default games created by the server, this method returns a list of all commands that
     * have been executed after the initial placement round. For user­created games, this method
     * returns a list of all commands that have been executed since the very beginning of the game
     * (i.e., before the initial placement round).
     * You must login and join a game before calling this method
     *
     * @return a list of commands (for default games, all commands from the very beginning)
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
    public JSONArray getGameCommands() throws ClientException {//todo change to JSONArray return type

        String urlStr = "http://"+host+":"+port+"/game/commands";
        String arr = httpGet(urlStr);
        // todo: change when interacting with actual server
        if(!arr.equals("")) return new JSONArray(arr);
        else return null;
    }

    /**
     * Executes the specified command list in the current game.
     * This method can be used for testing and debugging. The command list returned by the
     * /game/command [GET] method is suitable for passing to this method.
     * This method returns the client model JSON for the game after the command list has been
     * applied.
     * You must login and join a game before calling this method
     *
     * @param json the list of commands to be executed
     * @return Model in JSON
     * @pre 1. The caller has previously logged in to the server and joined a game (i.e., they have
     * valid catan.user and catan.game HTTP cookies).
     * @post If the operation succeeds,
     * 1. The passed­in command list has been applied to the game.
     * 2. The server returns an HTTP 200 success response.
     * 3. The body contains the game’s updated client model JSON
     * If the operation fails,
     * 1. The server returns an HTTP 400 error response, and the body contains an error
     * message
     *
     * Note: I think the Swagger page doesn't correctly explain what is suppose to be done here
     *
     * this is a post method (even though the swagger page explains that it is a get method)
     */
    @Override
    public JSONObject executeGameCommands(JSONArray json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/game/commands";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Returns a list of supported AI player types.
     * Currently, LARGEST_ARMY is the only supported type.
     *
     * @return Model in JSON
     * @pre None
     * @post If the operation succeeds,
     * 1. The server returns an HTTP 200 success response.
     * 2. The body contains a JSON string array enumerating the different types of AI players.
     * These are the values that may be passed to the /game/addAI method.
     */
    @Override
    public JSONArray listAI() throws ClientException {
        String urlStr = "http://"+host+":"+port+"/game/listAI";
        String responseStr = httpGet(urlStr);
        return new JSONArray(responseStr);
    }

    /**
     * Adds an AI player to the current game.
     * You must login and join a game before calling this method
     *
     * @param json json object only containing the AI type
     * @return Model in JSON
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
    public String addAI(JSONObject json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/game/addAI";
        return httpPost(urlStr, json.toString());
    }

    /**
     * Sets the server’s logging level
     *
     * @param json - loggingLevel:LoggingLevel
     * @return todo - verify
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
    public String utilChangeLogLevel(JSONObject json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/util/changeLogLevel";
        return httpPost(urlStr, json.toString());
    }

    /**
     * Adds a message to the end of the chat
     *
     * @param json - playerIndex:int, content:string
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game.
     * @post The chat contains your message at the end
     */
    @Override
    public JSONObject sendChat(JSONObject json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/moves/sendChat";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the server what number was rolled so resources can be distributed, discarded or robbed.
     *
     * @param json - playerIndex:int, number:int(2-12)
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. It is your turn. The client model’s status is ‘Rolling’
     * @post The client model’s status is now in ‘Discarding’ or ‘Robbing’ or ‘Playing’
     */
    @Override
    public JSONObject rollNumber(JSONObject json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/moves/rollNumber";
        String responseStr = httpPost(urlStr, json.toString());
        return new JSONObject(responseStr);
    }

    /**
     * Ends the players turn.
     *
     * @param json - playerIndex:int
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game.
     * @post The cards in your new dev card hand have been transferred to your old dev card
     * hand. It is the next player’s turn
     */
    @Override
    public JSONObject finishTurn(JSONObject json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/moves/finishTurn";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells Server what cards to remove from the player's hand.
     *
     * @param json - playerIndex:int, discardedCards:ResourceList
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. The status of the client model is 'Discarding'.
     * You have over 7 cards. You have the cards you're choosing to discard.
     * @post You gave up the specified resources. If you're the last one to discard, the client model status changes to 'Robbing'
     */
    @Override
    public JSONObject discardCards(JSONObject json) throws ClientException{

        String urlStr = "http://"+host+":"+port+"/moves/discardCards";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells Server to build a road for the given player in the given location.
     *
     * @param json - playerIndex:int, roadLocation:HexLocation, free:bool
     * @return Model in JSON
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
    public JSONObject buildRoad(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/buildRoad";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells Server to build a settlement for the given player in the given location.
     *
     * @param json - playerIndex:int, vertexLocation:VertexObject, free:bool
     * @return Model in JSON
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
    public JSONObject buildSettlement(JSONObject json) throws ClientException{

        String urlStr = "http://"+host+":"+port+"/moves/buildSettlement";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells Server to build a city for the given player in the given location.
     *
     * @param json - playerIndex:int, vertexLocation:VertexObject
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. The city location is where you currently have a settlement.
     * You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city).
     * The city is on the map at the specified location.
     * You got a settlement back
     */
    @Override
    public JSONObject buildCity(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/buildCity";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells Server to send a trade offer to the other player.
     *
     * @param json - playerIndex:int, offer:ResourceList, receiver:int)
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. You have the resources you are offering.
     * @post The trade is offered to the other player (stored in the server model).
     */
    @Override
    public JSONObject offerTrade(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/offerTrade";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to trade the players' cards.
     *
     * @param json - playerIndex:int, willAccept:bool
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. You have been offered a domestic trade.
     * To accept the offered trade, you have the required resources
     * @post If you accepted, you and the player who offered swap the specified resources.
     * If you declined no resources are exchanged.
     * The trade offer is removed
     */
    @Override
    public JSONObject acceptTrade(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/acceptTrade";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to trade bank and player cards.
     *
     * @param json - playerIndex:int, ratio:int(2,3 or4), inputResource:Resource, outputResource:Resource
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. You have the resources you are giving.
     * For ratios less than 4, you have the correct port for the trade
     * @post The trade has been executed (the offered resources are in the bank, and the
     * requested resource has been received)
     */
    @Override
    public JSONObject maritimeTrade(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/maritimeTrade";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to move the robber and move the stolen card.
     *
     * @param json - playerIndex:int, location:HexLocation, victimIndex:int(-1,0,1,2,or 3)
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. The robber is not being kept in the same location.
     * If a player is being robbed (i.e., victimIndex != ­1), the player being robbed has
     * resource cards
     * @post The robber is in the new location. The player being robbed (if any) gave you one of his resource cards (randomly
     * selected)
     */
    @Override
    public JSONObject robPlayer(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/robPlayer";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to give the player a new Development Card.
     *
     * @param json - playerIndex:int
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. You have the required resources (1 ore, 1 wheat, 1 sheep).
     * There are dev cards left in the deck
     * @post You have a new card
     * - If it is a monument card, it has been added to your old devcard hand
     * - If it is a non­monument card, it has been added to your new devcard hand
     * (unplayable this turn)
     */
    @Override
    public JSONObject purchaseDevCard(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/buyDevCard";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to allow the player to rob another player.
     *
     * @param json - playerIndex:int, location:HexLocation, victimIndex:int(-1,0,1,2,or 3)
     * @return Model in JSON
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
    public JSONObject playSoldier(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/Soldier";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to allow the player to pick two resources from the Bank
     *
     * @param json - playerIndex:int, resource1:Resource, resource2:Resource
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game. The two specified resources are in the bank
     * @post You gained the two specified resources
     */
    @Override
    public JSONObject playYearOfPlenty(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/Year_of_Plenty";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to allow the player to build two roads for free.
     *
     * @param json - playerIndex:int, spot1:EdgeLocation, spot2:EdgeLocation
     * @return Model in JSON
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
    public JSONObject playRoadBuilding(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/Road_Building";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to allow the player to pick a resource to gather from the other players.
     *
     * @param json - playerIndex:int, resource:Resource
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game.
     * @post All of the other players have given you all of their resource cards of the specified
     * type
     */
    @Override
    public JSONObject playMonopoly(JSONObject json) throws ClientException {

        String urlStr = "http://"+host+":"+port+"/moves/Monopoly";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

    /**
     * Tells the Server to add one victory point to the player.
     *
     * @param json - playerIndex:int
     * @return Model in JSON
     * @pre Caller has already logged in to the server and joined a game.
     * You have enough monument cards to win the game (i.e., reach 10 victory points)
     * @post You gained a victory point.
     */
    @Override
    public JSONObject playMonument(JSONObject json) throws ClientException {
        String urlStr = "http://"+host+":"+port+"/moves/Monument";
        return new JSONObject(httpPost(urlStr, json.toString()));
    }

}
