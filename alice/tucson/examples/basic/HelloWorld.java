package alice.tucson.examples.basic;

import alice.tucson.api.*;
import alice.logictuple.*;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.types.*;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;

public class HelloWorld
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonOpType.DEFLANGUAGE);
		
		TucsonTupleCentreId tid = null;		

		if (args.length == 0)
		{
			tid = new TucsonTupleCentreId("default@localhost");
		}
		else
		{
			tid = new TucsonTupleCentreId(args[0]);			
		}

		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new TucsonAgentId("cli"), TucsonOpType.DEFLANGUAGE));

		long now = System.currentTimeMillis();
		LogicTuple tuple = new LogicTuple("msg", new Value("Hello world!"), new Value("time", new Value(now)));

		cnt.out(tid, tuple);
		System.out.println("Tuple inserted: " + tuple);

		LogicTuple template = new LogicTuple("msg", new Var("Msg"), new Var("Time"));
		LogicTuple msg = (LogicTuple) cnt.in(tid, template);

		System.out.println("Tuple retrieved name: " + msg.getName());
		System.out.println("Msg argument: " + msg.getArg(0));
		System.out.println("Time argument: " + msg.getArg(1).getArg(0));
	}
}
