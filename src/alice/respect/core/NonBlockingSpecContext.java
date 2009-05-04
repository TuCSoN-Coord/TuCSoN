package alice.respect.core;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.LogicTuple;
import alice.respect.api.AgentId;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.OperationNotPossibleException;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.OperationCompletionListener;

public class NonBlockingSpecContext implements INonBlockingSpecContext {

	private IRespectTC core;
    
    public NonBlockingSpecContext(IRespectTC core_){
        core=core_;
    }
	

	public IRespectOperation in_s(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.in_s(id,t,l);
		return op;
	}

	public IRespectOperation inp_s(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		// TODO Auto-generated method stub
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.inp_s(id,t,l);
		return op;
	}

	public IRespectOperation out_s(IId id, LogicTuple t, OperationCompletionListener l)
			throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.out_s(id,t,l);
		return op;
		
	}

	public IRespectOperation rd_s(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.rd_s(id,t,l);
		return op;
	}

	public IRespectOperation rdp_s(IId id, LogicTuple t,
			OperationCompletionListener l) throws InvalidLogicTupleException,
			OperationNotPossibleException {
		if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.rdp_s(id,t,l);
		return op;
	}

}
