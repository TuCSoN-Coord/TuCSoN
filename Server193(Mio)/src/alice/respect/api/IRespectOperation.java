package alice.respect.api;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.Tuple;

/**
 * ReSpecT Operation Interface.
 * 
 * @author aricci
 *
 */
public interface IRespectOperation extends alice.tuplecentre.api.ITupleCentreOperation {

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
	boolean isTime();
	boolean isGetEnv();
	boolean isSetEnv();
	boolean isEnv();
	
	
	/**
	 * Gets the results of a get operation
	 */
	public List<LogicTuple> getTupleResultList();

}
