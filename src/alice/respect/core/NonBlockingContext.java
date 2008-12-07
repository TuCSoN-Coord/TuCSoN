package alice.respect.core;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.LogicTuple;
import alice.respect.api.AgentId;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.OperationNotPossibleException;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.OperationCompletionListener;

public class NonBlockingContext implements INonBlockingContext {

	private IRespectTC core;
    
    public NonBlockingContext(IRespectTC core_){
        core=core_;
    }
	

	public IRespectOperation in(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.in(id,t,l);
		return op;
	}

	public IRespectOperation inp(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		// TODO Auto-generated method stub
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.inp(id,t,l);
		return op;
	}

	public IRespectOperation out(IId id, LogicTuple t, OperationCompletionListener l)
			throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.out(id,t,l);
		return op;
		
	}

	public IRespectOperation rd(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.rd(id,t,l);
		return op;
	}

	public IRespectOperation rdp(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.rdp(id,t,l);
		return op;
	}

}
