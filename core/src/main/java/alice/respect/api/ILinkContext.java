package alice.respect.api;

import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public interface ILinkContext {

    /**
     *
     * @param id
     *            the tuple centre target of the operation
     * @param op
     *            the invoked operation
     * @throws OperationNotPossibleException
     *             if the operation cannot be carried out
     */
    void doOperation(TupleCentreId id, AbstractTupleCentreOperation op)
            throws OperationNotPossibleException;
}
