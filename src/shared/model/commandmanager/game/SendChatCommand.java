package shared.model.commandmanager.game;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerFacade;
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
        JSONObject sendChatJSON = new JSONObject(getRequest());
        playerIndex = sendChatJSON.getInt("playerIndex");
        content = sendChatJSON.getString("content");

        setUserIdFromCookie();
        SendChatCommand command = new SendChatCommand(playerIndex, content);
        command.setUserId(getUserId());
        command.setGameId(getGameId());
        ClientModel model = IServerFacade.getInstance().sendChat(getUserId(), getGameId(), command);
        if(model != null) {
            model.incrementVersion();
            IServerFacade.getInstance().logCommand(getGameId(), command);
        }
        return (model != null) ? ServerTranslator.getInstance().clientModelToString(model) : null;
    }

    @Override
    public boolean reExecute(int gameID){
        int userId = getUserId();//getUserIdFromIndex(playerIndex, gameID);
        ClientModel model = IServerFacade.getInstance().sendChat(userId, gameID, this);
        if(model != null) {
            model.incrementVersion();

            return true; //it worked
        }
        else{
            System.out.println(">SENDCHATCMD: reExec(): couldn't re-execute!");
            return false;
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
