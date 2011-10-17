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

		TucsonTupleCentreId tid = new TucsonTupleCentreId("default");//before args[0] "default"
		Value dest = new Value("default@localhost");//args[1] "default@localhost"
		Value msg = new Value("prova");//arg[2] "prova"
	
		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("paperino"), TucsonAgentLanguageManager.getCurLanguage());
		
		cnt.out(tid, new LogicTuple("msg", dest, msg), (Long) null);
	}
}
