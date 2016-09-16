package model.messagemanager;

import java.awt.*;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class MessageManager {

    private String messageLine;

    private String source;//the player's username

    public Color getUserColor() {
        return Color.BLACK;
    }

    public String getMessageLine() {
        return messageLine;
    }

    public void setMessageLine(String messageLine) {
        this.messageLine = messageLine;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
