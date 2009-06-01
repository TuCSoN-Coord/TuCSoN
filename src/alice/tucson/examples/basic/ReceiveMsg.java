package alice.tucson.examples.basic;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

public class ReceiveMsg
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		
		TucsonTupleCentreId tid = new TucsonTupleCentreId(args[0]);
		
		Value dest = new Value(args[1]);

		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("agente2"), TucsonAgentLanguageManager.getCurLanguage());		

		LogicTuple msg = cnt.in(tid, new LogicTuple("msg", dest, new Var("Msg")), (Long) null);
		
		System.out.println("New message: " + msg.getArg(1));
	}
}
