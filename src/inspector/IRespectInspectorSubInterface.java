package inspector;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.InvalidTupleOperationException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;

public interface IRespectInspectorSubInterface {
	
	void focusOnTC(String tcname) throws InvalidTupleCentreIdException;
	
	String[] getTuples(String filter) throws InvalidLogicTupleException;
	
	void addTuple(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	void removeTuple(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String getSpec();
	
	void setSpec(String spec) throws InvalidSpecificationException;
	
	String[] getSpecs(String filtre) throws InvalidLogicTupleException;
	
	void addSpec(String reaction) throws InvalidSpecificationException, InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	void removeSpec(String reaction) throws InvalidSpecificationException, InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String[] getQueries(String query) throws InvalidLogicTupleException, InvalidTupleOperationException;
	
	void cancelQuery(String query) throws InvalidLogicTupleException, InvalidTupleOperationException;
	
	String[] getReactions(String reaction) throws InvalidLogicTupleException;
	
	void stopTC() throws OperationNotPossibleException;
	
	void nextTCstep() throws OperationNotPossibleException;
	
	void resumeTC() throws OperationNotPossibleException;

}
