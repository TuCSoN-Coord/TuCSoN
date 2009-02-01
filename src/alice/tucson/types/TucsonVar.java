package alice.tucson.types;

import alice.logictuple.InvalidVarNameException;
import alice.logictuple.Var;
import alice.tucson.api.exception.TucsonInvalidVarNameException;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;

public class TucsonVar extends TucsonTupleArgument{
	
	public TucsonVar(String var) throws TucsonInvalidVarNameException{
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		if (lang==null){
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			try {
				super.t = new Var(var);
			} catch (InvalidVarNameException e) {
				throw new TucsonInvalidVarNameException();
			} 
		}
		
	}
}
