package alice.respect.api;

import alice.logictuple.LogicTuple;

/**
 * ReSpecT Operation Interface.
 * 
 * @author aricci
 * 
 */
public interface IRespectOperation extends alice.tuplecentre.api.ITupleCentreOperation
{
	/**
	 * Gets the result of the operation,
	 * 
	 * @return
	 */
	LogicTuple getLogicTupleResult();

	/**
	 * Gets the argument of the operation
	 * 
	 * @return
	 */
	LogicTuple getLogicTupleArgument();

}
