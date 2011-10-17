package alice.tucson.types;

import alice.logictuple.LogicTuple;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exception.TucsonInstantiationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidLogicTupleException;
import alice.tucson.api.exception.TucsonInvalidSpecificationException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;

public interface IContainerResolver
{
	public boolean tupleCentreCreator(TucsonTupleCentreId id, int qSize) throws TucsonInstantiationNotPossibleException;
	
	public Object tupleCentreProvider(TucsonTupleCentreId id) throws TucsonInstantiationNotPossibleException;
	
	public ITupleCentreOperation doNonBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException;
	
	public ITupleCentreOperation doNonBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException,
			TucsonInvalidSpecificationException;
	
	public Object doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, Object o)
			throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException, TucsonInvalidSpecificationException;
	
	public Object doBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t)
		throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException, TucsonInvalidSpecificationException;
	
	public Object doManagementOperation(int type, TucsonTupleCentreId tid, Object obj) 
		throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException;
}
