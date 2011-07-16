package alice.tucson.test;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

public class TestSend
{
	public static void main(String[] args) throws Exception
	{
		// Viene utilizzato "ReSpecT 2.2"
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);

		TucsonTupleCentreId tid = new TucsonTupleCentreId(args[0]);
		Value dest = new Value(args[1]);
		Value msg = new Value(args[2]);
	
		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("paperino"), TucsonAgentLanguageManager.getCurLanguage());
		
		cnt.out(tid, new LogicTuple("msg", dest, msg), (Long) null);	
	}
}
