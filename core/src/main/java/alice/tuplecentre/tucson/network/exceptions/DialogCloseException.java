package alice.tuplecentre.tucson.network.exceptions;

/**
 * DialogCloseException. Thrown if TuCSoN Node service can't handle a close
 * connection request.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class DialogCloseException extends DialogException {

    /**
     * Exception thrown when something goes wrong while closing a dialog
     * connection
     */
    private static final long serialVersionUID = 1L;

    public DialogCloseException(final String message) {
        super(message);
    }

    public DialogCloseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DialogCloseException(final Throwable cause) {
        super(cause);
    }
}
