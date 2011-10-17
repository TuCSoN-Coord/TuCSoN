/*
 * Created on Feb 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuplecentre.api;

import alice.tuplecentre.core.OperationNotPossibleException;
import alice.tuplecentre.core.TupleCentreOperation;

/**
 * @author aricci
 *
 */
public interface ITupleCentre {

	void doOperation(IId who, TupleCentreOperation op) throws OperationNotPossibleException;

}
