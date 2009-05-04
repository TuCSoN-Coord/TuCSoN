package alice.tucson.test;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TestSend {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId(args[0]);
		Value dest = new Value(args[1]);
		Value msg = new Value(args[2]);
				
		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new AgentId("paperino")));
		cnt.out(tid,new LogicTuple("msg",dest,msg));
	}
}
