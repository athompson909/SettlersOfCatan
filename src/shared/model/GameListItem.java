package shared.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * This class represents the Game items that are listed whenever a user clicks "join game" -
 * it is also the type that the server returns (in a JSONArray) when asked for /games/list
 *
 * This is really just to make it easier to parse the server's /games/list response, and hopefully
 * to make it easier to access/display info about the available games in the actual JoinGame view.
 *
 * Created by Sierra on 9/27/16.
 */
public class GameListItem {


    /**
     * the title of the game
     */
    private String title;

    /**
     * the ID of the game
     */
    @SerializedName("id")
    private int gameID;

    /**
     * the list of players currently joined in this game - null if no one's joined that spot yet
     * max size is 4!! (indexes 0-3)
     */
    private ArrayList<GameListPlayerItem> players;

    //Constructor
    public GameListItem(String ttl, int id, ArrayList<GameListPlayerItem> plyrs) {
        title = ttl;
        gameID = id;
        players = plyrs;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ArrayList<GameListPlayerItem> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<GameListPlayerItem> players) {
        this.players = players;
    }
}
