package Exceptions;

/**
 * Created by Stephanie on 9/19/16.
 */

public class ClientException extends Exception {

    public ClientException() {
        return;
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable throwable) {
        super(throwable);
    }

    public ClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
