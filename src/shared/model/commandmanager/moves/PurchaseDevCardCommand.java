package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PurchaseDevCardCommand implements BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "buyDevCard";
    /**
     * Creates PurchaseDevCardCommand to send to client.ClientFacade. sets player Index
     * @param playerIndex
     */
    public PurchaseDevCardCommand(int playerIndex){
        this.playerIndex = playerIndex;
    }

    /**
     * Tells server to give player new DevCard
     */
    @Override
    public void serverExec(BaseCommand command){

    }

    //Getters
    public String getType() {
        return type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
