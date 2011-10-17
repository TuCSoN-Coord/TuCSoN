package alice.respect.core;

import alice.respect.api.ILinkContext;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.TupleCentreId;

import alice.tuplecentre.core.TupleCentreOperation;

public class LinkContext implements ILinkContext {

	private RespectVM vm;
	
	public LinkContext(RespectVM vm){
		
		this.vm = vm;
	}
	
	public void doOperation(TupleCentreId id, TupleCentreOperation op) throws OperationNotPossibleException {
		
		vm.doOperation(id, (RespectOperation)op);

	}

}
