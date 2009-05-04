package alice.tucson.test;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TestReceive {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId(args[0]);
		Value dest = new Value(args[1]);
				
		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new AgentId("pippo")));
				
		while (true){
			LogicTuple msg = cnt.in(tid,new LogicTuple("msg",dest,new Var("Msg")));
			System.out.println("New message: "+msg.getArg(1));
		}
	}
}
