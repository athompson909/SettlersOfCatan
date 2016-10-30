package client;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * ClientUser is designed to be a small singleton that holds all important info about the user so that it can be
 * accessed anytime throughout the game. This info includes name, index, ID, and color.
 *
 * For example, the JoinGameView uses this info (packaged into a PlayerInfo object) to determine whether the user is already
 * added to a game or not.
 *
 * THIS IS A SINGLETON
 *
 * Created by Alise on 10/8/2016.
 */
public class ClientUser {

    //I think we set this depending on what order they join the game in
    private int index = -1;

    //We can get this string when they login
    private String name = "";

    //Their ID is given back by the server in a cookie format
    private int id = -1;

    //We can get this when they pick a color upon joining a game
    private CatanColor color = null;

    //keeps track of which game the clientUser is currently in
    private int currentGameID = 1;


    /**tracks whether the player has discarded this turn already */
    private boolean needToDiscard = true;

    //saves the GameInfo object for the current game we are added to
    private GameInfo currentAddedGameInfo;

    //used to mark whether WHITE is really available on SelectColorView or not. If the user created their own game,
    //they are added to it using the WHITE as a default color. But on the SelectColorView, white may actually be
    // an available color to pick when they are really starting the game.
    //So, this bool is used to help determine whether to enable the WHITE button or not.
    private boolean joinedWithDefaultColor = true; //assuming they did, so SelectColorView will let us choose WHITE after logging back in


    private Map<String, CatanColor> playerColors = new HashMap<>();


    //this PlayerInfo object just packages all 4 data items about the user (name, id, index, color) into an object,
    //specifically for JoinGameView at least
    //we might just build a PlayerInfo object inside JoinGameView, not right here
    private PlayerInfo localPlayerInfo;

    private static ClientUser instance = new ClientUser();

    public static ClientUser getInstance() {
        return instance;
    }

    private ClientUser(){}




    //GETTERS AND SETTERS

    public void setName(String username){
        name = username;
        System.out.println(">CLIENTUSER: setName to " + username);
    }
    public void setIndex(int playerIndex){
        index = playerIndex;
        System.out.println(">CLIENTUSER: setIndex to " + playerIndex);

    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        System.out.println(">CLIENTUSER: setID to " + id);

    }

    public CatanColor getColor() {
        return color;
    }

    public void setColor(CatanColor color) {
        this.color = color;
        System.out.println(">CLIENTUSER: setColor to " + color);

    }

    public PlayerInfo getLocalPlayerInfo() {

        PlayerInfo localPlayerInfoSoFar = new PlayerInfo();
        localPlayerInfoSoFar.setName(this.name);
        localPlayerInfoSoFar.setId(this.id);
        localPlayerInfoSoFar.setColor(this.color);
        localPlayerInfoSoFar.setPlayerIndex(this.index);

        return localPlayerInfoSoFar;
    }

    public void setLocalPlayerInfo(PlayerInfo localPlayerInfo) {
        this.localPlayerInfo = localPlayerInfo;
    }

    public static void setInstance(ClientUser instance) {
        ClientUser.instance = instance;
    }

    public int getCurrentGameID() {
        return currentGameID;
    }

    public void setCurrentGameID(int currentGameID) {
        this.currentGameID = currentGameID;
        System.out.println(">CLIENTUSER: setCurrGameID to " + currentGameID);
    }

    public GameInfo getCurrentAddedGameInfo() {
        return currentAddedGameInfo;
    }

    public void setCurrentAddedGameInfo(GameInfo currentAddedGameInfo) {
        this.currentAddedGameInfo = currentAddedGameInfo;
    }

    public boolean joinedWithDefaultColor() {
        return joinedWithDefaultColor;
    }

    public void setJoinedWithDefaultColor(boolean joinedWithDefaultColor) {
        this.joinedWithDefaultColor = joinedWithDefaultColor;
    }

    public Map<String, CatanColor> getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(Map<String, CatanColor> playerColors) {
        this.playerColors = playerColors;
    }

    public void setPlayerColors() {

        GameInfo[] gamesList = ClientFacade.getInstance().gamesList();
        List<PlayerInfo> players = gamesList[ClientUser.getInstance().getCurrentGameID()].getPlayers();
        for(PlayerInfo player : players) {
            playerColors.put(player.getName(), player.getColor());
        }
    }

    public void setNeedToDiscard(boolean discarded){
        needToDiscard = discarded;
    }

    public boolean getNeedToDiscard(){
        return needToDiscard;
    }


    public void resetPlayerColors(Player[] players){
        for(int i = 0; i < players.length; i++){
            playerColors.put(players[i].getName(), players[i].getColor());
        }
    }
}
