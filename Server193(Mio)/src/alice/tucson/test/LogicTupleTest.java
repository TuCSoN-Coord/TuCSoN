package alice.tucson.test;

import alice.logictuple.*;
import alice.tucson.api.Tucson;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonContext;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.Node;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tuprolog.Prolog;

public class LogicTupleTest
{
	public static void main(String[] args) throws Exception
	{
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);

		TucsonContext ctx = Tucson.getContext(new TucsonAgentId("agenteDiProva"), TucsonAgentLanguageManager.getCurLanguage());

		TupleArgument[] list = new TupleArgument[3];

		list[0] = new Value("point", new Value(1.0), new Value(2.0));
		list[1] = new Value("point", new Value(5.0), new Value(4.0));
		list[2] = new Value("point", new Value(6.0), new Value(8.0));

		LogicTuple t = new LogicTuple("arco", new Value(10), new Value(11), new Value(list));

		// analogo al precedente, con parsing
		TucsonTupleCentreId tid = new TucsonTupleCentreId("test");
		ctx.out(tid, t, (Long) null);

		LogicTuple t1 = LogicTuple.parse("arco(10,11,[point(1.0,2.0), point(5.0,4.0), point(6.0,8.0)])");
		ctx.out(tid, t1, (Long) null);

		// recupero e accesso a liste
		LogicTuple t2 = ctx.in(tid, new LogicTuple("arco", new Var("X"), new Var("Y"), new Var("L")), (Long) null);

		TupleArgument v0 = t2.getArg(0);
		TupleArgument v1 = t2.getArg(1);

		TupleArgument[] plist = t2.getArg(2).toArray();

		for (int i = 0; i < plist.length; i++)
		{
			TupleArgument arg = plist[i];
			System.out.println(arg);
		}

		LogicTuple t0 = new LogicTuple("test", new Var(), new Var());

		LogicTuple t3 = new LogicTuple("test", new Value(1), new Value(2));

		if (t0.match(t3))
		{
			t0.propagate(new Prolog(), t3);
			System.out.println("Unification ok: " + t0);
		}
		else
		{
			System.out.println("No unification");
		}
		
		/*TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		 
		TucsonContext ctx = Tucson.getContext(new TucsonAgentId("pippo"),
		TucsonAgentLanguageManager.getCurLanguage());
		
		TucsonTupleCentreId tid = new TucsonTupleCentreId("engine");

		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("tuples.txt")); boolean exit = false;

			while (!exit) 
			{	 
				String line = br.readLine(); 
				if (line==null) 
					exit = false; 
				else
				{
					LogicTuple tuple = LogicTuple.parse(line);
					ctx.out(tid, tuple, (Long) null); 
				} 
			}
		}
		catch(Exception ex) 
		{
			ex.printStackTrace(); 
		}*/
	}
}
