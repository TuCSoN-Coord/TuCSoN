package alice.tuplecentre.respect.api;

import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

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
