package alice.tuplecentre.tucson.network.exceptions;

/**
 * IllegalPortNumberException.
 *
 * @author Alessia Papini (mailto: alessia.papini@studio.unibo.it)
 * @author Mattia Balducci (mailto: mattia.balducci@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class IllegalPortNumberException extends RuntimeException {

    /**
     * Exception thrown when an invalid port number is used
     */
    private static final long serialVersionUID = 1L;

    public IllegalPortNumberException(final String arg0) {
        super(arg0);
    }
}
