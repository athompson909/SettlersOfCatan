package model.messagemanager;

import java.util.List;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * handles the data for the chat and logs windows
 */
public class MessageLine {

    private MessageList chats;

    private MessageList logs;

    public MessageList getChats() {
        return chats;
    }

    public void setChats(MessageList chats) {
        this.chats = chats;
    }

    public MessageList getLogs() {
        return logs;
    }

    public void setLogs(MessageList logs) {
        this.logs = logs;
    }
}
