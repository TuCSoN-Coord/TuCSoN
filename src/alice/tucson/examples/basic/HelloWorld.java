package alice.tucson.examples.basic;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

public class HelloWorld
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		
		TucsonTupleCentreId tid = null;		

		if (args.length == 0)
		{
			tid = new TucsonTupleCentreId("default@localhost");
		}
		else
		{
			tid = new TucsonTupleCentreId(args[0]);			
		}

		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("cli"), TucsonAgentLanguageManager.getCurLanguage());

		long now = System.currentTimeMillis();
		LogicTuple tuple = new LogicTuple("msg", new Value("Hello world!"), new Value("time", new Value(now)));

		cnt.out(tid, tuple, (Long) null);
		System.out.println("Tuple inserted: " + tuple);

		LogicTuple template = new LogicTuple("msg", new Var("Msg"), new Var("Time"));
		LogicTuple msg = cnt.in(tid, template, (Long) null);

		System.out.println("Tuple retrieved name: " + msg.getName());
		System.out.println("Msg argument: " + msg.getArg(0));
		System.out.println("Time argument: " + msg.getArg(1).getArg(0));
	}
}
