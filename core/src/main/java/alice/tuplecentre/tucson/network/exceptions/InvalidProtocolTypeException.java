package alice.tuplecentre.tucson.network.exceptions;

/**
 * InvalidProtocolTypeException. Thrown if connection protocol is not supported
 * yet.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class InvalidProtocolTypeException extends Exception {

    /**
     * Exception thrown when an invalid protocol type is used
     */
    private static final long serialVersionUID = 1L;

    public InvalidProtocolTypeException(final String arg0) {
        super(arg0);
    }
}
