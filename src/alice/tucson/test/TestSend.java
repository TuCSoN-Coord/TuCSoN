package alice.tucson.test;

import alice.respect.api.AgentId;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.*;
import alice.logictuple.*;

public class TestSend {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId("prova@localhost");
		Value dest = new Value("tc_prova");
		Value msg = new Value("Message test TuCSoN 1.9 + ReSpecT 2.2");
				
		TucsonContextSynch cnt = new TucsonContextSynch(Tucson.getContext(new AgentId("writer"), "ReSpecT 2.2"));
		cnt.out(tid,new LogicTuple("msg",dest,msg));
		System.out.println("Tuple send correctly");
		System.exit(0);
	}
}
