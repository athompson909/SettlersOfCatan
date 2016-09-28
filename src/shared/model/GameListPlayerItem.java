package shared.model;

import com.google.gson.annotations.SerializedName;
import shared.definitions.CatanColor;

/**
 * This class represents a player on the GameList view, where the user is viewing all the available games to join.
 *
 * Each game on the list of available games (GameListItem) will have up to four GameListPlayerItems.
 * If no one has joined yet, that spot will be null/empty.
 *
 * This is really just to make it easier to parse the server's /games/list response, and hopefully
 * to make it easier to access/display info about the available games in the actual JoinGame view.
 *
 * Created by Sierra on 9/27/16.
 */
public class GameListPlayerItem {


    /**
     * The color of the player - this is relative to each game
     */
    private CatanColor color;

    /**
     * the player's name
     */
    private String name;


    /**
     * the player's ID - I don't think this is relative to each game, but rather unique across all active games
     */
    @SerializedName("id")
    private int playerID;


    //Constructor
    public GameListPlayerItem(CatanColor clr, String nm, int pID) {
        color = clr;
        name = nm;
        playerID = pID;
    }




    public CatanColor getColor() {
        return color;
    }

    public void setColor(CatanColor color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
