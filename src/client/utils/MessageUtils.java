package client.utils;

import client.misc.MessageView;

/**
 * Created by adamthompson on 10/17/16.
 */
public class MessageUtils {

    /**
     * Displays a customizable error message
     * @param messageView the messageView (make sure to cast from when passing in parameter IMessageView if necessary)
     * @param title message title
     * @param message message content
     */
    public static void showRejectMessage(MessageView messageView, String title, String message) {

        messageView.setTitle(title, 220);
        messageView.setMessage(message, 220);
        messageView.setCloseButton("OK");
        messageView.showModal();
    }
}
