package alice.respect.core;

import alice.respect.api.ILinkContext;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.UnreachableNodeException;
import alice.tucson.service.InterTupleCentreContext;
import alice.tucson.service.InterTupleCentreContextStub;
import alice.tucson.types.TucsonTupleCentreId;
import alice.tuplecentre.core.TupleCentreOperation;

public class RespectInterTupleCentreContextStub implements ILinkContext{
	private InterTupleCentreContext helper;
	private alice.tuplecentre.api.TupleCentreId idTo;
	private String lang;
	
	public RespectInterTupleCentreContextStub(alice.tuplecentre.api.TupleCentreId id, String language){
		idTo = id;
		lang = language;		
	}
	
	public void doOperation(TupleCentreId id, TupleCentreOperation op)
			throws OperationNotPossibleException {
		try {
			helper = new InterTupleCentreContextStub(new TucsonTupleCentreId(id),lang);
		} catch (TucsonInvalidTupleCentreIdException e1) {
			e1.printStackTrace();
		}
		try {
			helper.doOperation(idTo, op.getType(), ((RespectOperation)op).getLogicTupleResult(), lang);
		} catch (OperationNotAllowedException e) {
			e.printStackTrace();
			//TODO gestire eccezioni
		} catch (UnreachableNodeException e) {
			e.printStackTrace();
			//TODO gestire eccezioni
		} catch (LanguageNotFoundException e) {
		}
		
	}

}
