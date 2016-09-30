package shared.model.messagemanager;

/**
 * Individual list item for GameLog and Chat
 */
public class MessageLine {

    /**
     * The message to be passed to the MessageList
     */
    private String message;

    /**
     * The player who sent this MessageLine
     */
    private String source;

    /**
     * Sets message to the correct verbage
     *
     * @param msg the input to be added to a MessageList
     */
    public MessageLine (String msg, String src){
        message = msg;
        source = src;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "MessageLine{" +
                "message='" + message + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
