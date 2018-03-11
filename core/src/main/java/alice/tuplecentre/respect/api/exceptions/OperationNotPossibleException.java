/*
 * TooManyPendingInputEventsException.java Created on September 20, 2003, 5:14
 * PM
 */
package alice.tuplecentre.respect.api.exceptions;

/**
 * Exception thrown when a requested operation cannot be carried out
 *
 * @author Alessandro Ricci
 */
public class OperationNotPossibleException extends RespectException {

    private static final long serialVersionUID = 6633704058075398146L;

    public OperationNotPossibleException() {
        super();
    }

    public OperationNotPossibleException(final String arg0) {
        super(arg0);
    }
}
