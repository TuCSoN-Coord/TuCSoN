package alice.tucson.types;

import alice.logictuple.InvalidVarNameException;
import alice.logictuple.Var;
import alice.tucson.api.exception.TucsonInvalidVarNameException;
import alice.tucson.service.TucsonTCContainer;

public class TucsonVar extends TucsonTupleArgument{
	
	public TucsonVar(String var) throws TucsonInvalidVarNameException{
		String lang = TucsonTCContainer.getCurLanguage().getName();
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			try {
				super.t = new Var(var);
			} catch (InvalidVarNameException e) {
				throw new TucsonInvalidVarNameException();
			} 
		}
		
	}
}
