package alice.respect.api;

import alice.logictuple.*;

/**
 * A ReSpecT Tuple Centre Interface with blocking operations.
 * 
 * @author aricci
 *
 */
public interface IBlockingContext {
    
	void out(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;

	LogicTuple in(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;
		
	LogicTuple rd(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	LogicTuple inp(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;

	LogicTuple rdp(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;

	
}