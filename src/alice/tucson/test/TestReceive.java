package alice.tucson.test;

import alice.respect.api.AgentId;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.*;
import alice.logictuple.*;

public class TestReceive {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId("prova@localhost");
		Value dest = new Value("tc_prova");
				
		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new AgentId("receiver"), "ReSpecT 2.2"));


		LogicTuple msg = (LogicTuple) cnt.in(tid,new LogicTuple("msg",dest,new Var("Msg")));
		System.out.println("New message: "+msg.getArg(1));
		System.exit(0);
	}
}
