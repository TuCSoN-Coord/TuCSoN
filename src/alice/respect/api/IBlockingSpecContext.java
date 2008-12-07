package alice.respect.api;

import alice.logictuple.*;

/**
 * A ReSpecT Tuple Centre Interface with blocking specification operations.
 * 
 * @author aricci
 *
 */
public interface IBlockingSpecContext {
    
	void out_s(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;

	LogicTuple in_s(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;
		
	LogicTuple rd_s(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	LogicTuple inp_s(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;

	LogicTuple rdp_s(AgentId aid, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException;

}