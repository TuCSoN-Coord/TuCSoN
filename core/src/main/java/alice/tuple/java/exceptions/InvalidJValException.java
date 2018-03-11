package alice.tuple.java.exceptions;

/**
 * Exception thrown when an invalid JVal is used or created (e.g. null)
 *
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public class InvalidJValException extends Exception {

    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;

    public InvalidJValException() {
        super();
    }

    public InvalidJValException(final String message) {
        super(message);
    }
}
