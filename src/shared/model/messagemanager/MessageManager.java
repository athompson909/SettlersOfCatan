package shared.model.messagemanager;

/**
 * Manages GameLog and Chat
 */
public class MessageManager {

    /**
     * The object containing all chats sent throughout the game
     */
    private MessageList chat = new MessageList();

    /**
     * The object containing all logs which have occurred thus far
     */
    private MessageList log = new MessageList();

    //constructor
    public MessageManager() {
    //    MessageLine disabledChat = new MessageLine("To ensure secrecy, chatting is disabled during setup rounds.", "");
    //    chat.insertMessageLine(disabledChat);
    }


    /**
     * Initiated the updating of the Chat/Log pieces of the model
     *
     * Calls the update() methods on its various data members to
     * ensure that they are up-to-date with the latest version of
     * model received from the server
     *
     * @param newMessageMgr The most up-to-date MessageManager from the updated ClientModel
     */
    public void updateMessageManager(MessageManager newMessageMgr) {
        setChat(newMessageMgr.getChat());
        setLog(newMessageMgr.getLog());
    }

    //GETTERS
    public MessageList getChat() {return chat;}
    public MessageList getLog() {return log;}

    //SETTERS
    public void setChat(MessageList newChat) {
        chat = newChat;
    }
    public void setLog(MessageList newLog) {
        log = newLog;
    }

}
