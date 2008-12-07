package alice.tucson.types;


import alice.logictuple.Value;

import alice.tucson.service.TucsonTCContainer;

public class TucsonValue extends TucsonTupleArgument{

	public TucsonValue(String val){
		String lang = TucsonTCContainer.getCurLanguage().getName();
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			super.t = new Value(val); 
		}
		
	}

	
}
