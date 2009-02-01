package alice.tucson.types;


import alice.logictuple.Value;

import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;

public class TucsonValue extends TucsonTupleArgument{

	public TucsonValue(String val){
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		if (lang==null){
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			super.t = new Value(val); 
		}
		
	}

	
}
