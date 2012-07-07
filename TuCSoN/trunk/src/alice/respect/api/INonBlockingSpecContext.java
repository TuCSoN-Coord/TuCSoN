package alice.respect.api;

import alice.logictuple.*;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.OperationCompletionListener;

/**
 * A ReSpecT Tuple Centre Interface with non blocking specification operations.
 * 
 * @author aricci
 *
 */
public interface INonBlockingSpecContext {
    
	IRespectOperation no_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	IRespectOperation nop_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	IRespectOperation out_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;

	IRespectOperation in_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
		
	IRespectOperation rd_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	IRespectOperation inp_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;

	IRespectOperation rdp_s(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	IRespectOperation get_s(IId aid, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	IRespectOperation set_s(IId aid, RespectSpecification spec, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;

	/*void setSpec(IId aid, RespectSpecification spec) throws InvalidSpecificationException;
	
	RespectSpecification getSpec(IId aid); */  

}