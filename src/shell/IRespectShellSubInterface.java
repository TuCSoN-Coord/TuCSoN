package shell;

import alice.logictuple.InvalidLogicTupleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;


public interface IRespectShellSubInterface {
	
	void focusOnTC(String tcname) throws InvalidTupleCentreIdException;
	
	String out(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String in(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String rd(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String inp(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String rdp(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String outS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String inS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String rdS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String inpS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String rdpS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException;
	
	String setSpec(String spec) throws InvalidSpecificationException, InvalidAgentIdException;
	
	String getSpec() throws InvalidAgentIdException;

}
