package alice.tucson.examples.basic;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

public class SendMsg
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
			
		TucsonTupleCentreId tid = new TucsonTupleCentreId("box");
		
		Value dest = new Value("mario");
		Value msg = new Value("ciao ciao");

		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("agente1"), TucsonAgentLanguageManager.getCurLanguage());
		
		cnt.out(tid, new LogicTuple("msg", dest, msg), (Long) null);
		
		System.out.println("Message posted.");
	}
}
