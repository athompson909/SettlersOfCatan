package shared.model.commandmanager.moves;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class RollDiceCommand implements BaseCommand {
    /**
     * number rolled
     */
    private int number;

    /**
     * the index of the player who rolled the dice
     */
    private int playerIndex;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "rollNumber";

    /**
     * Creates RollDiceCommand to send to client.ClientFacade. Sets data members
     * @param number
     */
    public RollDiceCommand(int number){
        this.number = number;
    }

    /**
     * Tells server the dice were rolled and to distribute/require discarding of resources
     */
    @Override
    public void serverExec(BaseCommand command){

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getType() {
        return type;
    }
}
