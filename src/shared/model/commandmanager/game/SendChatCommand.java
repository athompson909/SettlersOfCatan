package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class SendChatCommand implements BaseCommand {
    /**
     * int 0-3 representing player
     */
    private int playerIndex;

    /**
     * content of message to add to chat
     */
    private String content;

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
     * Tells server to add this message to the end of the chat
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
