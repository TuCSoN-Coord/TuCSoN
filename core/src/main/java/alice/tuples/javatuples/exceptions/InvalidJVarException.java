/**
 * InvalidJVarException.java
 */
package alice.tuples.javatuples.exceptions;

/**
 *
 * Exception thrown when an invalid JVar is used or created (e.g. null)
 *
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 *
 */
public class InvalidJVarException extends Exception {

    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;

    public InvalidJVarException() {
        super();
    }

    public InvalidJVarException(final String message) {
        super(message);
    }
}
