package alice.tucson.examples.basic;

import alice.tucson.api.*;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;
import alice.logictuple.*;

public class ReceiveMsg
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonOpType.DEFLANGUAGE);
		
		TucsonTupleCentreId tid = new TucsonTupleCentreId(args[0]);
		
		Value dest = new Value(args[1]);

		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new TucsonAgentId("agente2"), TucsonOpType.DEFLANGUAGE));		

		LogicTuple msg = (LogicTuple) cnt.in(tid, new LogicTuple("msg", dest, new Var("Msg")));
		
		System.out.println("New message: " + msg.getArg(1));
	}
}
