package examples.basic;
import alice.logictuple.*;
import alice.tucson.api.*;


public class HelloWorld
{
	public static void main(String[] args) throws Exception
	{
			
		TucsonTupleCentreId tid = new TucsonTupleCentreId("box"); //NEEEEEEEEEEEEEEEWWWW		
				
		TucsonAgentId aa = new TucsonAgentId("tonio");
		TucsonContext cnt = Tucson.getContext(aa, null);
			
		long now = System.currentTimeMillis();
		LogicTuple tuple = new LogicTuple("msg", new Value("Hello world!"), new Value("time", new Value(""+now)));
				
		cnt.out(tid, tuple, (Long) null);
		System.out.println("Tuple inserted: " + tuple);

		LogicTuple template = new LogicTuple("msg", new Var("Msg"), new Var("Time"));
		LogicTuple msg = cnt.rd(tid, template, (Long) null);

		System.out.println("Tuple retrieved name: " + msg.getName());
		System.out.println("Msg argument: " + msg.getArg(0));
		System.out.println("Time argument: " + msg.getArg(1).getArg(0)); 
		
	}
}
