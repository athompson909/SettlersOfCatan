package client;

import client.data.GameInfo;
import exceptions.ClientException;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.model.ClientModel;
import shared.model.ClientUpdateManager;
import shared.model.JSONTranslator;
import shared.model.commandmanager.BaseCommand;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.player.Player;

import java.util.List;

/**
 * THIS IS A SINGLETON
 *
 * Created by Alise on 9/17/2016.
 *
 */
public class ClientFacade {

    /**
     * Translates to and from JSON
     */
    private JSONTranslator jsonTranslator = new JSONTranslator();

    /**
     * Used to update the model
     */
    private ClientUpdateManager clientUpdateManager;

    /**
     * Used to communicate with the server
     */
    private IServerProxy serverProxy;

    private String gameModelVersionStr;

    private int version = -1;

    private static ClientFacade instance = new ClientFacade();

    public static ClientFacade getInstance() {
        return instance;
    }

    public void setValues(IServerProxy serverProxy, ClientModel model) {
        this.serverProxy = serverProxy;
        clientUpdateManager = new ClientUpdateManager(model);
    }

    /**
     * todo: program version number checking
     * right now the model is being constantly updated
     * *this function is not called as a result of ServerPoller - run- fetchNewModel()
     *
     * sends updated model to the updateManager to delegate updates
     * @param updatedClientModel - model returned by server
     */
    public void sendUpdatedModel(ClientModel updatedClientModel) {

        System.out.println("SendUpdatedModel: newModelVer= " + updatedClientModel.getVersion() + "\n");
        System.out.println("==================== NEW STATUS: " + updatedClientModel.getTurnTracker().getStatus() + "====================");
        if(version != updatedClientModel.getVersion()) {
            clientUpdateManager.setCurrentModel(Client.getInstance().getClientModel());
            clientUpdateManager.delegateUpdates(updatedClientModel);
            version = updatedClientModel.getVersion();

            Player[] players = Client.getInstance().getClientModel().getPlayers();
            if(players[3] != null) {Client.getInstance().setStartGame(false);}
        }
        else if(Client.getInstance().getStartGame()) {
            clientUpdateManager.setCurrentModel(Client.getInstance().getClientModel());
            clientUpdateManager.delegateUpdates(updatedClientModel);
            version = updatedClientModel.getVersion();
            Client.getInstance().setStartGame(false);
        }
    }

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
     * @param loginCommand - contains info needed to login
     * @return boolean - true if logged in correctly; false if there were problems
     */
    public boolean userLogin(LoginCommand loginCommand){
        JSONObject json = jsonTranslator.loginCmdToJSON(loginCommand);

        System.out.println(">CLIENTFACADE: USERLOGIN - trying with server");

        try {
            String response = serverProxy.userLogin(json);

            System.out.println(">CLIENTFACADE: USERLOGIN: server said " + response);

            if(response.equals("Success")) {
               // System.out.println(">CLIENTFACADE: USERLOGIN: server was happy");
                //save username
                ClientUser.getInstance().setName(loginCommand.getUsername());
                return true;
            }
            else {
               // System.out.println(">CLIENTFACADE: USERLOGIN: server sad :(");
                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
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
     * @param registerCommand
     * @return boolean - true if the user was registered; false otherwise
     */
    public boolean userRegister(RegisterCommand registerCommand){
        JSONObject json = jsonTranslator.registerCmdToJSON(registerCommand);

        System.out.println(">CLIENTFACADE: USERREGISTER- trying with server");

        try {
            String response = serverProxy.userRegister(json);

            System.out.println(">CLIENTFACADE: USERREGISTER: server said " + response);

            if(response.equals("Success")){
              //  System.out.println(">CLIENTFACADE: USERREGISTER: server was happy");

                ClientUser.getInstance().setName(registerCommand.getUsername());
                return true;
            }
            else {
             //   System.out.println(">CLIENTFACADE: USERREGISTER: server sad :(");

                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
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
     * @return List - a list of GameListItems containing information about each game on the Server
     */
    public GameInfo[] gamesList(){
        try {
            JSONArray response = serverProxy.gamesList();
            GameInfo[] games = jsonTranslator.gamesListResponseFromJSON(response);
            return games;
        }catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
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
     * @param gameCreateCommand - item holding information needed to create a new game
     * @return GameInfo - an item containing information about the game just created on the server
     */
    public GameInfo gameCreate(GameCreateCommand gameCreateCommand){
        JSONObject json = jsonTranslator.gameCreateCmdToJSON(gameCreateCommand);
        try {
            JSONObject response = serverProxy.gameCreate(json);
            GameInfo game = jsonTranslator.gameInfoFromJSON(response);
//            ClientModel clientModel = jsonTranslator.modelFromJSON(response);
//            sendUpdatedModel(clientModel);
            return game;
        }
        catch (ClientException e) {
            e.printStackTrace();
            return null;
        }

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
     * @param gameJoinCommand - holds info needed to join a game
     * @return boolean - true if the game was successfully joined
     */
    public boolean gameJoin(GameJoinCommand gameJoinCommand){
        JSONObject json = jsonTranslator.gameJoinCmdToJSON(gameJoinCommand);
        System.out.println(">>CLIENTFACADE: gameJoin: json to send= " + json);

        try {
            String response = serverProxy.gameJoin(json);
            System.out.println(">>CLIENTFACADE: server response= " + response);

            if(response.equals("Success")){
                System.out.println(">>CLIENTFACADE: gameJoin: join worked");
                gameModelVersion(); // ************* THIS IS WHERE THE MODEL IS FIRST INSTANTIATED ****************
                serverProxy.startPoller();
                return true;
            } else {
                System.out.println(">>CLIENTFACADE: gameJoin: join FAILED");
                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Called when the user creates a game and needs to be added to it. At that point, they haven't chosen
     * a color yet, so we add them to the game with the default color WHITE.
     * When they click Re-Join on the GameList view, they will be asked to pick a new color.
     * The server will then re-add them to that game with the new color they just picked. Holla
     * @param gameJoinCommand
     * @return
     */
    public boolean gameJoinWithDefaultColor(GameJoinCommand gameJoinCommand){
        JSONObject json = jsonTranslator.gameJoinCmdToJSON(gameJoinCommand);
        System.out.println(">>CLIENTFACADE: gameJoinW/DefaultClr: json to send= " + json);

        try {
            String response = serverProxy.gameJoin(json);
            System.out.println(">>CLIENTFACADE: server response= " + response);

            if(response.equals("Success")){
                System.out.println(">>CLIENTFACADE: gameJoinWD: join worked");
                //we don't want to get the model yet, not until PlayerWaitingView is done!
                return true;
            } else {
                System.out.println(">>CLIENTFACADE: gameJoinWD: join FAILED");
                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }

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
     * @param command - contains info needed to save a game
     * @return boolean - true if game successfully saved
     */
    public boolean gameSave(GameSaveCommand command){
        JSONObject json = jsonTranslator.gameSaveCmdToJSON(command);
        try {
            String response = serverProxy.gameSave(json);
            if(response.equals("Success")){
                return true;
            }else{
                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
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
     * @param command - holds info needed to load a game
     * @return boolean - true if game successfully loaded
     */
    public boolean gameLoad(GameLoadCommand command){
        JSONObject json = jsonTranslator.gameLoadCmdToJSON(command);
        try {
            String response = serverProxy.gameLoad(json);
            if(response.equals("Success")){

                return true;
            }else{
                return false;
            }
        }catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
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
    titled “client Model JSON Documentation”
     */
    public void gameModelVersion() {

        JSONObject jsonNewModel;
        try {
            String jsonNewModelStr = serverProxy.gameModelVersion();
            gameModelVersionStr = jsonNewModelStr;

            if(gameModelVersionStr.equals("error, exception thrown")) throw new ClientException(); // todo: delete

            jsonNewModel = new JSONObject(jsonNewModelStr);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);

            //try saving the ClientUser index right here============
            Player[] players = updatedModel.getPlayers();
            for(int i = 0; i < 4; i++){
                if(players[i].getName().equals(ClientUser.getInstance().getName())){
                    ClientUser.getInstance().setIndex(i);
                    break;
                }
            }
            System.out.println(">> CLIENTFACADE: sendUpdatedModel called");
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
            return;
        }
    }

    public String getGameModelVersionStr() {
        return gameModelVersionStr;
    }

    public void setGameModelVersionStr(String gameModelVersionStr) {
        this.gameModelVersionStr = gameModelVersionStr;
    }

    /**
     *Clears out the command history of the current game.
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
     */
    public void gameReset() {
        try {
            JSONObject jsonModel = serverProxy.gameReset();
            ClientModel resetModel = jsonTranslator.modelFromJSON(jsonModel);
            sendUpdatedModel(resetModel);
        }catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Returns a list of commands that have been executed in the current game.
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
     */
    public void getGameCommands(){
        try {
            JSONArray response = serverProxy.getGameCommands();
            List<BaseCommand> list = jsonTranslator.commandsListFromJSON(response);
            System.out.println(list);
        }catch(ClientException e){
            e.printStackTrace();
        }
    }

    /**
     *Executes the specified command list in the current game.
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
     */
    public void executeGameCommands(List<BaseCommand> allExecutedCommands){
        JSONArray jsonArray = jsonTranslator.commandsListToJSON(allExecutedCommands);
        try {
            JSONObject response = serverProxy.executeGameCommands(jsonArray);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(response);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Returns a list of supported AI player types.
     Currently, LARGEST_ARMY is the only supported type.
     * @pre None
     * @post If the operation succeeds,
    1. The server returns an HTTP 200 success response.
    2. The body contains a JSON JSONObject array enumerating the different types of AI players.
    These are the values that may be passed to the /game/addAI method.
     * @return list of AIs to pick from
     */
    public String[] listAI(){
        try {
            JSONArray response = serverProxy.listAI();
            String[] AIs = jsonTranslator.listAIResponseFromJSON(response);
            return AIs;
        }catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

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
     * @param command - contains info needed to add AI to a game
     * @return boolean - true if AI added successfully
     */
    public boolean addAI(AddAICommand command){
        JSONObject json = jsonTranslator.addAICmdToJSON(command);
        try {
            String response = serverProxy.addAI(json);
            if(response.equals("Success")){
          //      System.out.println(">CLIENTFACADE: addAI: server said success");

                return true;
            }else{
          //      System.out.println(">CLIENTFACADE: addAI: server said FAIL :( ");
                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *Sets the server’s logging level
     * @pre 1.The caller specifies a valid logging level. Valid values include: SEVERE, WARNING,
    INFO, CONFIG, FINE, FINER, FINEST
     * @post If the operation succeeds,
    1. The server returns an HTTP 200 success response with “Success” in the body.
    2. The Server is using the specified logging level
    If the operation fails,
    1. The server returns an HTTP 400 error response, and the body contains an error
    message.
     * @param command - contains info needed to change log levels
     * @return boolean - true if log level successfully changed
     */
    public boolean utilChangeLogLevel(UtilChangeLogLevelCommand command){

        JSONObject json = jsonTranslator.utilChangeLogLevelCmdToJSON(command);
        try {
            String response = serverProxy.utilChangeLogLevel(json);
            if(response.equals("Success")){
                return true;
            }else{
                return false;
            }
        }
        catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }



    //Move

    /**
     * Adds a message to the end of the chat
     * @pre Caller has already logged in to the server and joined a game.
     * @post The chat contains your message at the end
     * @param command - message to add to chat
     */
    public void sendChat(SendChatCommand command){
        JSONObject jsonToSend = jsonTranslator.sendChatCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.sendChat(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells the server what number was rolled so resources can be distributed, discarded or robbed.
     * @pre Caller has already logged in to the server and joined a game. It is your turn. The client model’s status is ‘Rolling’
     * @post The client model’s status is now in ‘Discarding’ or ‘Robbing’ or ‘Playing’
     * @param command - number rolled
     */
    public void rollNumber(RollDiceCommand command){
        JSONObject jsonToSend = jsonTranslator.rollDiceCmdToJSON(command);
        try {

            JSONObject jsonNewModel = serverProxy.rollNumber(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Ends the players turn.
     * @pre Caller has already logged in to the server and joined a game.
     * @post The cards in your new dev card hand have been transferred to your old dev card
    hand. It is the next player’s turn
     * @param command - contains info needed to finish your turn
     */
    public void finishTurn(FinishTurnCommand command){
        JSONObject jsonToSend = jsonTranslator.finishTurnCmdToJSON(command);
        try {

            JSONObject jsonNewModel = serverProxy.finishTurn(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells Server what cards to remove from the player's hand.
     * @pre Caller has already logged in to the server and joined a game. The status of the client model is 'Discarding'.
     * You have over 7 cards. You have the cards you're choosing to discard.
     * @post You gave up the specified resources. If you're the last one to discard, the client model status changes to 'Robbing'
     * @param command - contains list of cards to discard
     */
    public void discardCards(DiscardCommand command){
        JSONObject jsonToSend = jsonTranslator.discardCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.discardCards(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells Server to build a road for the given player in the given location.
     * @pre Caller has already logged in to the server and joined a game. The road location is open.
     * The road location is connected to another road owned by the player. The road location is not on water.
     * You have the required resources (1 wood, 1 brick; 1 road).
     * Setup round: Must be placed by settlement owned by the player with no adjacent
    road
     * @post You lost the resources required to build a road (1 wood, 1 brick; 1 road).
     * The road is on the map at the specified location.
     * If applicable, “longest road” has been awarded to the player with the longest road
     * @param command - contains location to build road on
     */
    public void buildRoad(BuildRoadCommand command){
        JSONObject jsonToSend = jsonTranslator.buildRoadCmdToJSON(command);
        try {

            JSONObject jsonNewModel = serverProxy.buildRoad(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

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
     * @param command - contains settlement location info
     */
    public void buildSettlement(BuildSettlementCommand command){
        JSONObject jsonToSend = jsonTranslator.buildSettlementCmdToJSON(command);
        try {

            JSONObject jsonNewModel = serverProxy.buildSettlement(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells Server to build a city for the given player in the given location.
     * @pre Caller has already logged in to the server and joined a game. The city location is where you currently have a settlement.
     * You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city).
     * The city is on the map at the specified location.
     * You got a settlement back
     * @param command - contains city location info
     */
    public void buildCity(BuildCityCommand command){
        JSONObject jsonToSend = jsonTranslator.buildCityCmdToJSON(command);
        try {

            JSONObject jsonNewModel = serverProxy.buildCity(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells Server to send a trade offer to the other player.
     * @pre Caller has already logged in to the server and joined a game. You have the resources you are offering.
     * @post The trade is offered to the other player (stored in the server model).
     * @param command - contains trade offer info
     */
    public void offerTrade(OfferTradeCommand command){
        //Todo I THINK THIS ONE IS GOING TO BE A LITTLE DIFFERENT THAN THE OTHERS
        // It will display an offer screen to the other user and they have to create
        // an acceptTradeCommand object to send back... -Steph
        JSONObject jsonToSend = jsonTranslator.offerTradeCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.offerTrade(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells the Server to trade the players' cards.
     * @pre Caller has already logged in to the server and joined a game. You have been offered a domestic trade.
     * To accept the offered trade, you have the required resources
     * @post If you accepted, you and the player who offered swap the specified resources.
     * If you declined no resources are exchanged.
     * The trade offer is removed
     * @param command - info needed to accept trade
     */
    public void acceptTrade(AcceptTradeCommand command){
        JSONObject jsonToSend = jsonTranslator.acceptTradeCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.acceptTrade(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells the Server to trade bank and player cards.
     * @pre Caller has already logged in to the server and joined a game. You have the resources you are giving.
     * For ratios less than 4, you have the correct port for the trade
     * @post The trade has been executed (the offered resources are in the bank, and the
    requested resource has been received)
     * @param command - contains info to trade with bank
     */
    public void maritimeTrade(MaritimeTradeCommand command){
        JSONObject jsonToSend = jsonTranslator.maritimeTradeCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.maritimeTrade(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells the Server to move the robber and move the stolen card.
     * @pre Caller has already logged in to the server and joined a game. The robber is not being kept in the same location.
     * If a player is being robbed (i.e., victimIndex != ¬1), the player being robbed has
    resource cards
     * @post The robber is in the new location. The player being robbed (if any) gave you one of his resource cards (randomly
    selected)
     * @param command - contains info needed to rob a player
     */
    public void robPlayer(RobPlayerCommand command){
        JSONObject jsonToSend = jsonTranslator.robPlayerCmdToJSON(command);
        try {

            JSONObject jsonNewModel = serverProxy.robPlayer(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells the Server to give the player a new Development Card.
     * @pre Caller has already logged in to the server and joined a game. You have the required resources (1 ore, 1 wheat, 1 sheep).
     * There are dev cards left in the deck
     * @post You have a new card
    - If it is a monument card, it has been added to your old devcard hand
    - If it is a non¬monument card, it has been added to your new devcard hand
    (unplayable this turn)
     * @param command - info needed to buy dev card
     */
    public void purchaseDevCard(PurchaseDevCardCommand command){
        JSONObject jsonToSend = jsonTranslator.purchaseDevDardCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.purchaseDevCard(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

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
     * @param command - contains info needed to play soldier card
     */
    public void playSoldier(PlaySoldierCommand command){
        JSONObject jsonToSend = jsonTranslator.playSoldierCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.playSoldier(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells the Server to allow the player to pick two resources from the Bank
     * @pre Caller has already logged in to the server and joined a game. The two specified resources are in the bank
     * @post You gained the two specified resources
     * @param command - contains info to play year of plenty card
     */
    public void playYearOfPlenty(PlayYearOfPlentyCommand command){
        JSONObject jsonToSend = jsonTranslator.playYearOfPlentyCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.playYearOfPlenty(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells the Server to allow the player to build two roads for free.
     * @pre Caller has already logged in to the server and joined a game. The first road location (spot1) is connected to one of your roads..
     * The second road location (spot2) is connected to one of your roads or to the first
    road location (spot1).
    Neither road location is on water.
    You have at least two unused roads
     * @post You have two fewer unused roads.
     * Two new roads appear on the map at the specified locations.
     * If applicable, “longest road” has been awarded to the player with the longest road
     * @param command - info to play road building card
     */
    public void playRoadBuilding(PlayRoadBuilderCommand command){
        JSONObject jsonToSend = jsonTranslator.playRoadBuilderCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.playRoadBuilding(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells the Server to allow the player to pick a resource to gather from the other players.
     * @pre Caller has already logged in to the server and joined a game.
     * @post All of the other players have given you all of their resource cards of the specified
    type
     * @param command - info to play monopoly card
     */
    public void playMonopoly(PlayMonopolyCommand command){
        JSONObject jsonToSend = jsonTranslator.playMonopolyCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.playMonopoly(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     *Tells the Server to add one victory point to the player.
     * @pre Caller has already logged in to the server and joined a game.
     * You have enough monument cards to win the game (i.e., reach 10 victory points)
     * @post You gained a victory point.
     * @param command - info to play monument card
     */
    public void playMonument(PlayMonumentCommand command){
        JSONObject jsonToSend = jsonTranslator.playMonumentCmdToJSON(command);
        try {
            JSONObject jsonNewModel = serverProxy.playMonument(jsonToSend);
            ClientModel updatedModel = jsonTranslator.modelFromJSON(jsonNewModel);
            sendUpdatedModel(updatedModel);
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
