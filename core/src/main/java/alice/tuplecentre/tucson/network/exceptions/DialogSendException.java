package alice.tuplecentre.tucson.network.exceptions;

/**
 * DialogSendException. Thrown if TuCSoN Node service can't handle sending of a
 * message.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class DialogSendException extends DialogException {

    /**
     * Exception thrown when a TuCSoN sending operation goes wrong
     */
    private static final long serialVersionUID = 1L;

    public DialogSendException(final String message) {
        super(message);
    }

    public DialogSendException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DialogSendException(final Throwable cause) {
        super(cause);
    }
}
