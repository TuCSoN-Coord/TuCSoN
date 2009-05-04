package alice.tucson.test;

import alice.tucson.api.*;
import alice.logictuple.*;

import java.io.*;

public class LogicTupleTest {

	public static void main(String[] args) throws Exception {
		
		/*
        TucsonContext ctx = Tucson.enterDefaultContext();
		TupleArgument[] list = new TupleArgument[3];
		list[0] = new Value("point", new Value(1.0), new Value(2.0));
		list[1] = new Value("point", new Value(5.0), new Value(4.0));
		list[2] = new Value("point", new Value(6.0), new Value(8.0));
		
		LogicTuple t = new LogicTuple("arco",
										new Value(10),
										new Value(11),
										new Value(list));
										
		// analogo al precedente, con parsing 
		TupleCentreId tid = new TupleCentreId("test");
		ctx.out(tid,t);
 			
		LogicTuple t1 = LogicTuple.parse("arco(10,11,[point(1.0,2.0), point(5.0,4.0), point(6.0,8.0)])");
		ctx.out(tid,t1);
		
		// recupero e accesso a liste
		LogicTuple t2 = ctx.in(tid,new LogicTuple("arco", new Var("X"),new Var("Y"), new Var("L")));
		
		TupleArgument v0 = t2.getArg(0);
		TupleArgument v1 = t2.getArg(1);
		TupleArgument[] plist = t2.getArg(2).toArray();
		for (int i = 0; i<plist.length; i++){
			TupleArgument arg = plist[i];
			System.out.println(arg);
		}*/
		
        /*
		LogicTuple t0 = new LogicTuple("test",new Var(),new Var());
		
		LogicTuple t1 = new LogicTuple("test",new Value(1),new Value(2));
		
		if (t0.match(t1)){
			t0.propagate(t1);
			System.out.println("Unification ok: "+t0);
		} else {
			System.out.println("No unification");
		}
		*/

        TucsonContext ctx = Tucson.getContext(new AgentId("pippo"));
        TupleCentreId tid = new TupleCentreId("engine");
        try {
            BufferedReader br = new BufferedReader(new FileReader("tuples.txt"));
            boolean exit = false;
            while (!exit){
                String line = br.readLine();
                if (line==null){
                    exit = false;
                } else {
                        LogicTuple tuple = LogicTuple.parse(line);
                        ctx.out(tid,tuple);
                }
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
		
	}
}
