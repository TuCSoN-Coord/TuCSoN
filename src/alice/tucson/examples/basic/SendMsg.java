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
			
		TucsonTupleCentreId tid = new TucsonTupleCentreId(args[0]);
		
		Value dest = new Value(args[1]);
		Value msg = new Value(args[2]);

		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("agente1"), TucsonAgentLanguageManager.getCurLanguage());
		
		cnt.out(tid, new LogicTuple("msg", dest, msg), (Long) null);
		
		System.out.println("Message posted.");
	}
}
