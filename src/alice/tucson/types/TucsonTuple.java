package alice.tucson.types;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;


public class TucsonTuple {
	private Object tuple;
	
	public TucsonTuple(String t, TucsonTupleArgument[] list){
		
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		if (lang==null){
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
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
