package model;

/**
 * Created by zsq on 16/12/2.
 */
public class OwnException extends Exception {

    private static final long serialVersionUID = 4753128611475635065L;

    public OwnException() {
    }

    public OwnException(String message) {
        super(message);
    }

    public OwnException(Throwable cause) {
        super(cause);
    }
}
