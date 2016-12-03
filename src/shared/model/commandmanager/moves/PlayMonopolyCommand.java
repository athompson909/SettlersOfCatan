package shared.model.commandmanager.moves;

import shared.shared_utils.Converter;
import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.definitions.ResourceType;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayMonopolyCommand extends BaseCommand {

    /**
     * Index of player playing Monopoly
     */
    private int playerIndex;
    /**
     * Resource player is calling monopoly on
     */
    private ResourceType resource;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "Monopoly";

    /**
     * Creates empty PlayMonopolyCommand
     */
    public PlayMonopolyCommand() {
    }

    /**
     * Creates PlayMonopolyCommand object to be sent to client.ClientFacade
     * for translating into JSON
     *
     * sets data members
     *
     * @param index
     * @param res
     */
    public PlayMonopolyCommand(int index, ResourceType res){
        playerIndex = index;
        resource = res;
    }

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONBoth();
    }


    /**
     * Calls all necessary model update methods
     *
     * Calls Player update methods to reflect changes in new and old devCard lists
     * Calls Player update methods to reflect changes in (potentially) each player's hand
     *
     */
    @Override
    public String serverExec() {
        JSONObject monopolyJSON = new JSONObject(getRequest());
        playerIndex = monopolyJSON.getInt("playerIndex");
        resource = Converter.stringToResourceType(monopolyJSON.getString("resource"));

        PlayMonopolyCommand command = new PlayMonopolyCommand(playerIndex, resource);
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().playMonopoly(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
            model.addLog(" played a monopoly card", getUserId());
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

//Getters
    public String getType() {
        return type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public ResourceType getResource() {
        return resource;
    }
}
