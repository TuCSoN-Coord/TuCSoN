package alice.tucson.test;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

public class TestReceive
{
	public static void main(String[] args) throws Exception
	{
		// Viene utilizzato "ReSpecT 2.2"
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);

		TucsonTupleCentreId tid = new TucsonTupleCentreId("default");
		Value dest = new Value("default@localhost");

		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("pippo"), TucsonAgentLanguageManager.getCurLanguage());

		while (true)
		{
			LogicTuple msg = cnt.in(tid, new LogicTuple("msg", dest, new Var("Msg")), (Long) null);
			
			System.out.println("New message: " + msg.getArg(1));
		}
	}
}
