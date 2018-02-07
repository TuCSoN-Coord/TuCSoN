package alice.tuplecentre.api.exceptions;

/**
 * Exception thrown when an unknown coordination operation is requested.
 *
 * @author Mattia Balducci, Alessia Papini
 *
 */
public class InvalidCoordinationOperationException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidCoordinationOperationException() {
        super();
    }

    public InvalidCoordinationOperationException(final String message) {
        super(message);
    }
}
