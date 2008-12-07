package alice.tucson.types;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.respect.api.AgentId;
import alice.respect.api.InvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.service.TucsonTCContainer;

public class TucsonTuple {
	private Object tuple;
	
	public TucsonTuple(String t, TucsonTupleArgument[] list){
		
		String lang = TucsonTCContainer.getCurLanguage().getName();
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			TupleArgument[] list2 = new TupleArgument[list.length];
			for (int i=0; i<list.length; i++){
				list2[i] = (TupleArgument) list[i].getInternalTupleArgument();
			}
			tuple = new LogicTuple(t, list2);
		}
	}
	
	public Object getInternalTuple(){
		return tuple;
	}
	
	

}
