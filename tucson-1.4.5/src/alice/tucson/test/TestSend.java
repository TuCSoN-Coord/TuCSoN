package alice.tucson.test;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TestSend {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId("prova@localhost");
		Value dest = new Value("tc_prova");
		Value msg = new Value("messaggio");
				
		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new AgentId("paperino")));
		cnt.out(tid,new LogicTuple("msg",dest,msg));
	}
}
