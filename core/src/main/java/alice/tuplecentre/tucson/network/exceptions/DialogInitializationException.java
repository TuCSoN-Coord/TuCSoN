package alice.tuplecentre.tucson.network.exceptions;

/**
 * DialogInitializationException. Thrown if TuCSoN Node service can't create a
 * TCP connection.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class DialogInitializationException extends DialogException {

    /**
     * Exception thrown when something goes wrong while creating a new
     * ServerSocket
     */
    private static final long serialVersionUID = 1L;

    public DialogInitializationException(final String message) {
        super(message);
    }

    public DialogInitializationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

    public DialogInitializationException(final Throwable cause) {
        super(cause);
    }
}
