package shared.model.commandmanager.game;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class SendChatCommand extends BaseCommand {
    /**
     * int 0-3 representing player
     */
    private int playerIndex;

    /**
     * content of message to add to chat
     */
    private String content;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "sendChat";

    /**
     * Creates empty Command
     */
    public SendChatCommand() {
    }

    /**
     *
     * @param playerIndex
     * @param content
     */
    public SendChatCommand(int playerIndex, String content){
        this.playerIndex = playerIndex;
        this.content = content;
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
     * Tells server to add this message to the end of the chat
     *\
     */
    @Override
    public String serverExec() {
        ClientModel model = IServerFacade.getInstance().sendChat(getUserId(), getGameId(), this);
        if(model != null) {
            return ServerTranslator.getInstance().clientModelToString(model);
        }else {
            return null;
        }
    }


    //Getters

    public int getPlayerIndex() {
        return playerIndex;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}
