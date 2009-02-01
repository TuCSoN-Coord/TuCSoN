package alice.tucson.translator;

import alice.respect.api.AgentId;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.types.ITranslator;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTuple;
import alice.tucson.types.TucsonTupleCentreId;

public class RespecT22to24 implements ITranslator{

	public RespecT22to24(){
		
	}

	public String[] getInfo() {
		String[] res = new String[2];
		//Language from
		res[0] = TucsonOpType.RESPECT22;
		//Language to
		res[1] = TucsonOpType.RESPECT24;
		return res;
	}
	
	public TucsonTupleCentreId tupleCentreIdTranslateSyntax(Object languageTo, Object id) {
		try {
			return new TucsonTupleCentreId(((TupleCentreId)id).toString());
		} catch (TucsonInvalidTupleCentreIdException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public TucsonAgentId agentIdTranslateSyntax(Object languageTo, Object id) {
		try {
			return new TucsonAgentId(((AgentId)id).toString());
		} catch (TucsonInvalidAgentIdException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TucsonTuple tupleTranslateSyntax(Object languageTo, Object id) {
		return null;
	}
}
