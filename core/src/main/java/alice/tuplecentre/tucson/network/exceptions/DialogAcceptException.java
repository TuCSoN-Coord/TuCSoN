package alice.tuplecentre.tucson.network.exceptions;

/**
 * DialogAcceptException. Thrown if TuCSoN Node service can't handle a new
 * connection request.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class DialogAcceptException extends DialogException {

    /**
     * Exception thrown when something goes wrong while accepting a new dialog
     * connection
     */
    private static final long serialVersionUID = 1L;

    public DialogAcceptException(final String message) {
        super(message);
    }

    public DialogAcceptException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DialogAcceptException(final Throwable cause) {
        super(cause);
    }
}
