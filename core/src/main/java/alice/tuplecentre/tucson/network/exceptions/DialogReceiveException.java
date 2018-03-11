package alice.tuplecentre.tucson.network.exceptions;

/**
 * DialogReceiveException. Thrown if TuCSoN Node service can't handle reception
 * of a message.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class DialogReceiveException extends DialogException {

    /**
     * Exception thrown when a TuCSoN receiving operation goes wrong
     */
    private static final long serialVersionUID = 1L;

    public DialogReceiveException(final String message) {
        super(message);
    }

    public DialogReceiveException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DialogReceiveException(final Throwable cause) {
        super(cause);
    }
}
