package alice.tucson.api;

import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.*;

public interface ITucsonOperation extends ITupleCentreOperation
{
	/**
	 * Gets the result LogicTuple of the operation,
	 * 
	 * @return
	 */
	LogicTuple getLogicTupleResult();

	/**
	 * Gets the argument LogicTuple of the operation
	 * 
	 * @return
	 */
	LogicTuple getLogicTupleArgument();
	
	String getSpecResult() throws InvalidTupleOperationException;
	
	boolean isSuccess();
}
