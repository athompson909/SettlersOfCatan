package Client.model.messagemanager;

import java.awt.*;

/**
 * Manages GameLog and Chat
 */
public class MessageManager {

    /**
     * The object containing all chats sent throughout the game
     */
    private MessageList chat;

    /**
     * The object containing all logs which have occurred thus far
     */
    private MessageList log;

    /**
     * Initiated the updating of the Chat/Log pieces of the model
     *
     * Calls the update() methods on its various data members to
     * ensure that they are up-to-date with the latest version of
     * model received from the server
     *
     * @param M The most up-to-date MessageManager from the updated ClientModel
     * @return True if the update resolved
     */
    private boolean update(MessageManager M) {
        return true;
    }

}
