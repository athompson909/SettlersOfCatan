package shared.model.commandmanager.moves;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import shared.model.commandmanager.BaseCommand;
import shared.definitions.ResourceType;

/**
 * Created by Alise on 9/18/2016.
 */
public class MaritimeTradeCommand extends BaseCommand {

    /**
     * Index of player involved in maritime trade
     */
    private int playerIndex;
    /**
     * ratio of trade associated with port (or 4:1) if no port
     */
    private int ratio;
    /**
     * ResourceType being given to the bank
     */
    @SerializedName("inputResource")
    private ResourceType toTrade;

    /**
     * ResourceType being received from bank
     */
    @SerializedName("outputResource")
    private ResourceType toReceive;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "maritimeTrade";


    /**
     * Creates MaritimeTradeCommand object to be sent to client.ClientFacade
     * for translation into JSON
     *
     * sets data members
     *
     * @param index
     * @param rat
     * @param inputResource
     * @param outputResource
     */
    public MaritimeTradeCommand(int index, int rat, ResourceType inputResource, ResourceType outputResource){
        this.playerIndex = index;
        this.ratio = rat;
        this.toTrade = inputResource;
        this.toReceive = outputResource;
    }

    /**
     * Calls all necessary update functions
     *
     * Calls Player update methods to reflect change in hand
     * Calls ResourceTypeBan update methods to reflect change in
     * recources in bank
     *
     * @param BC
     */
    @Override
    public JSONObject serverExec(BaseCommand BC) {

        return null;
    }


    //Getters and Setters
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public ResourceType getToTrade() {
        return toTrade;
    }

    public void setToTrade(ResourceType toTrade) {
        this.toTrade = toTrade;
    }

    public ResourceType getToReceive() {
        return toReceive;
    }

    public void setToReceive(ResourceType toReceive) {
        this.toReceive = toReceive;
    }

    public String getType() {
        return type;
    }

}
