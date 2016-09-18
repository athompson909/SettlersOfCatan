package Client.model.messagemanager;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * Individual data for GameLog and Chat
 */
public class MessageLine {

    /**
     * The message to be passed to the MessageList
     */
    String message;

    /**
     * Sets message to the correct verbage
     *
     * @param m the input to be added to a MessageList
     */
    MessageLine (String m){
        message = m;
    }
}
