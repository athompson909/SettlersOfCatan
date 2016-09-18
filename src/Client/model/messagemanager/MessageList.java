package Client.model.messagemanager;

import java.util.List;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * GameLog and Chat
 */
public class MessageList {

    /**
     * A list of all messsages or logs sent during the game
     */
    private List<MessageLine> lines;

    /**
     * Returns the list of lines within the MessageList
     *
     * @return all lines within the MessageList
     */
    public List<MessageLine> getLines() {
        return lines;
    }

    /**
     * Adds a new line to the list of messages along with
     * the ID and color of the contributing player
     *
     * @param M message to be added to MessageList
     */
    public void insertMessageLine(MessageLine M) {

    }

    /**
     * Updates this MessageList to match the updated list
     * returned from the ClientModel on the server
     *
     * @param M The updated MessageList
     */
    public void update(MessageList M) {
        this.lines = M.lines;
    }
}
