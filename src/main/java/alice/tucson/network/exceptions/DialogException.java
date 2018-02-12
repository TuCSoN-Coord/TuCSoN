package alice.tucson.network.exceptions;

/**
 *
 * Dialog root base Exception class
 *
 * @author Saverio Cicora
 *
 */
public class DialogException extends Exception {

    private static final long serialVersionUID = 1L;

    public DialogException() {
        super();
    }

    public DialogException(final String message) {
        super(message);
    }

    public DialogException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DialogException(final Throwable cause) {
        super(cause);
    }
}
