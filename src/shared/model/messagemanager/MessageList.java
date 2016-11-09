package shared.model.messagemanager;

import java.util.ArrayList;
import java.util.List;

/**
 * GameLog and Chat
 */
public class MessageList {

    /**
     * A list of all messsages or logs sent during the game
     */
    private List<MessageLine> lines = new ArrayList<>();

    public void setLines(List<MessageLine> lines) {
        this.lines = lines;
    }

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
        lines.add(M);
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


    @Override
    public String toString() {
        return "MessageList{" +
                "lines=" + lines +
                '}';
    }
}
