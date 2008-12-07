package alice.tucson.types;

import alice.tuplecentre.core.TupleCentreOperation;

public interface ITucsonOperationCompletionListener {
	public void operationCompleted(TucsonTupleCentreOperation op);
}
