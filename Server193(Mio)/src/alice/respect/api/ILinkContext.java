package alice.respect.api;

import alice.tuplecentre.core.TupleCentreOperation;

public interface ILinkContext {

	
	public void doOperation(TupleCentreId id, TupleCentreOperation op)throws OperationNotPossibleException;
}
