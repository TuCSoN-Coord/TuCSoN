package alice.tucson.examples.basic;

import alice.tucson.api.*;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;
import alice.logictuple.*;

public class SendMsg
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonOpType.DEFLANGUAGE);
			
		TucsonTupleCentreId tid = new TucsonTupleCentreId(args[0]);
		
		Value dest = new Value(args[1]);
		Value msg = new Value(args[2]);

		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new TucsonAgentId("agente1"), TucsonOpType.DEFLANGUAGE));		

		cnt.out(tid, new LogicTuple("msg", dest, msg));
		
		System.out.println("Message posted.");
	}
}
