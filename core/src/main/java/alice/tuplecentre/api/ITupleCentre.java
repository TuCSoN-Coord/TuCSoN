/*
 * Created on Feb 19, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuplecentre.api;

import alice.tuplecentre.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * @author Alessandro Ricci
 */
public interface ITupleCentre {

    /**
     *
     * @param who
     *            the identifier of the requestor of the operation
     * @param op
     *            the operation requested
     * @throws OperationNotPossibleException
     *             if the operation cannot be performed
     */
    void doOperation(IId who, AbstractTupleCentreOperation op)
            throws OperationNotPossibleException;
}
