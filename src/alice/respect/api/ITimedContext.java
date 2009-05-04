package alice.respect.api;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.LogicTuple;

/**
 * Interface to a ReSpecT Tuple Centre with timing functionalities.
 * 
 * @author aricci
 *
 */
public interface ITimedContext {

	void out(AgentId id, LogicTuple t, long ms)
			throws InvalidLogicTupleException, OperationNotPossibleException,
			OperationTimeOutException;

	LogicTuple in(AgentId id, LogicTuple t, long ms)
			throws InvalidLogicTupleException, OperationNotPossibleException,
			alice.respect.api.OperationTimeOutException;

	LogicTuple rd(AgentId id, LogicTuple t, long ms)
			throws InvalidLogicTupleException, OperationNotPossibleException,
			OperationTimeOutException;

	LogicTuple inp(AgentId id, LogicTuple t, long ms)
			throws InvalidLogicTupleException, OperationNotPossibleException,
			OperationTimeOutException;

	LogicTuple rdp(AgentId id, LogicTuple t, long ms)
			throws InvalidLogicTupleException, OperationNotPossibleException,
			OperationTimeOutException;

}