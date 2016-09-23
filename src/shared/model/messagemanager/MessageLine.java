package shared.model.messagemanager;

/**
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


    /**
     * Getter for String message
     * @return the String message contained in this MessageLine
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for String message
     * @param message the new string to save to this MessageLine
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
