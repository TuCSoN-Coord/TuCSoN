package alice.tucson.api;

import java.util.List;

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
	
	public void setTupleResultList(List<LogicTuple> tl);
	
	public List<LogicTuple> getTupleResultList();
	
	boolean isSuccessed();
	
	boolean isAllowed();
}
