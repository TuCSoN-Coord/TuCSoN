package alice.respect.api;

import alice.logictuple.*;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.OperationCompletionListener;

/**
 * A ReSpecT Tuple Centre Interface with blocking operations.
 * 
 * @author aricci
 *
 */
public interface INonBlockingContext {
    
	IRespectOperation out(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;

	IRespectOperation in(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
		
	IRespectOperation rd(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;
	
	IRespectOperation inp(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;

	IRespectOperation rdp(IId aid, LogicTuple t, OperationCompletionListener l) throws InvalidLogicTupleException, OperationNotPossibleException;

	/*void setSpec(IId aid, RespectSpecification spec) throws InvalidSpecificationException;
	
	RespectSpecification getSpec(IId aid); */  

}