package server;

import client.data.GameInfo;
import shared.model.ClientModel;

/**
 * Created by adamthompson on 11/4/16.
 */
public class Game {

    /**
     * Game info object for the current game.
     */
    private GameInfo gameInfo;

    /**
     * Client model for the game.
     */
    private ClientModel clientModel;



    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }
}
